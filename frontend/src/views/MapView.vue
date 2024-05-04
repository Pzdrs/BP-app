<script setup>
import {onMounted, ref} from "vue";
import {useDataPointStore} from "@/stores/datapoint.store";
import {useDataSourceStore} from "@/stores/datasource.store";
import bulmaCalendar from "bulma-calendar";
import NoDataSourcesAdoptedYetMessage from "@/components/message/NoDataSourcesAdoptedYetMessage.vue";
import {useToast} from "vue-toast-notification";
import {distance, getPopUpHTML} from "@/utils/dataPoint";
import {getDisplayName} from "@/utils/dataSource";
import {Chart, registerables} from "chart.js";
import DataPointBreakdownChart from "@/components/DataPointBreakdownChart.vue";

const loading = ref(false);

let map = null;

let timeframe = [];

let lastDataPoint;

let progress = ref(false);

let follow = ref(true);
let selectedDataSourcesCount = ref(0);

const dataPointStore = useDataPointStore();
const dataSourceStore = useDataSourceStore();

const $toast = useToast({position: 'top-right'});

dataSourceStore.loadDataSources();
dataSourceStore.loadGroups();

async function submit(event) {
  function drawSegment(point, previousPoint, dataSource) {
    let dataPoint = L.circle(point, {
      color: dataSource.color,
      fillOpacity: 1,
      radius: 1
    }).addTo(map);
    if (follow.value) map.flyTo([point.lat, point.lng], 18);

    dataPoint.bindPopup(getPopUpHTML(point, previousPoint))

    if (distance(point, previousPoint) > 10) return;
    L.polyline([
      [previousPoint.lat, previousPoint.lng],
      [point.lat, point.lng]
    ], {color: dataSource.color}).addTo(map);
  }

  const timeStart = new Date();

  const startDate = new Date(event.target.from.value)
  const endDate = new Date(event.target.to.value)
  const selectedDataSources = Array.from(document.querySelector('#data-sources').querySelectorAll('input[type="checkbox"]:checked')).map(checkbox => checkbox.getAttribute('data-source-id'));

  if (selectedDataSources.length === 0) {
    $toast.info('Select at least one data source');
    return;
  }

  loading.value = true;

  dataPointStore.clear();

  for (let selectedDataSource of selectedDataSources) {
    const dataSource = dataSourceStore.getDataSourceById(selectedDataSource);
    await dataPointStore.fetchDataPoints(selectedDataSource, startDate, endDate).then(dataPoints => {
      if (dataPoints.length === 0) {
        $toast.info(`No data points from <b>${dataSource.name}</b> in the selected time range`);
        return;
      }

      $toast.success(`Fetched ${dataPoints.length} data points from <b>${dataSource.name}</b>`);

      const renderTimeStart = new Date();
      for (let i = 0; i < dataPoints.length; i++) {
        const point = dataPoints[i];
        lastDataPoint = i === 0 ? {
          lat: point.lat,
          lng: point.lng,
          speed: 0
        } : dataPoints[i - 1];

        drawSegment(point, lastDataPoint, dataSource);
      }
      const renderTimeEnd = new Date();
      console.log(`Rendering ${dataSource.name} data points took ${renderTimeEnd - renderTimeStart}ms`);
    });
  }

  const timeEnd = new Date();

  console.log(`Fetching and drawing data points took ${timeEnd - timeStart}ms`);

  dataPointStore.listen(selectedDataSources, (point) => {
    if (lastDataPoint == null) {
      lastDataPoint = {
        lat: point.lat,
        lng: point.lng,
        speed: 0
      }
    }
    const dataSource = dataSourceStore.getDataSourceById(point.source);
    drawSegment(point, lastDataPoint, dataSource);
    lastDataPoint = point;
  })

  loading.value = false;
  progress.value = true;
  window.location.href = '#map';
}

function clear() {
  dataPointStore.clear();
  dataPointStore.eventSource.close();

  document.querySelectorAll('#data-sources input[type="checkbox"]').forEach(checkbox => checkbox.checked = false);

  // Reset map
  map.eachLayer(layer => {
    if (layer instanceof L.Polyline || layer instanceof L.Circle) {
      map.removeLayer(layer);
    }
  });

  setupCalendars();
  progress.value = false;
}

function setupCalendars() {
  timeframe.forEach(calendar => {
    if (calendar.element.name === 'from') {
      calendar.startTime = '00:00';
    }

    if (calendar.element.name === 'to') {
      calendar.startTime = '23:59';
    }

    calendar.startDate = new Date();

    calendar.save()
  })
}

function dataSourceChanged(e) {
  if (e.target.checked) {
    selectedDataSourcesCount.value++;
    if (selectedDataSourcesCount.value > 1) {
      follow.value = false;
    }
  } else {
    selectedDataSourcesCount.value--;
  }
}


onMounted(() => {
  map = L.map('map').setView([0, 0], 2);

  // Try to get the user's location
  navigator.geolocation.getCurrentPosition(pos => {
    map.setView([pos.coords.latitude, pos.coords.longitude], 9);
  })

  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(map);

  timeframe.push(...bulmaCalendar.attach('[type="datetime-local"]', {
    type: 'datetime',
    color: 'info',
    validateLabel: 'Apply',
  }));

  setupCalendars();
});

</script>

<template>
  <form id="map-settings" class="mb-5" @submit.prevent="submit">
    <div class="columns">
      <div id="data-sources" class="column is-6">
        <p class="is-size-4">Data sources</p>
        <hr class="my-2">
        <NoDataSourcesAdoptedYetMessage v-if="dataSourceStore.getAdoptedDataSources.length === 0"/>
        <div v-for="dataSource in dataSourceStore.getAdoptedDataSources" :key="dataSource.id" class="field">
          <label class="checkbox">
            <input type="checkbox" @change="dataSourceChanged" :style="{'accent-color': dataSource.color}"
                   :data-source-id="dataSource.id">
            {{ getDisplayName(dataSource) }}
          </label>
        </div>
      </div>
      <div class="column is-6">
        <p class="is-size-4">Timeframe</p>
        <hr class="my-2">
        <DataPointBreakdownChart/>
        <div class="is-flex is-justify-content-space-around">
          <input name="from" type="datetime-local">
          <input name="to" type="datetime-local">
        </div>
      </div>
    </div>
    <div class="field is-flex is-justify-content-flex-end">
      <div class="buttons">
        <div class="field pr-3">
          <input id="switchOutlinedDefault" @click="e=>follow=e.target.checked" :checked="follow"
                 :disabled="selectedDataSourcesCount > 1" type="checkbox" name="switchOutlinedDefault"
                 class="switch is-outlined" checked="checked">
          <label for="switchOutlinedDefault">Follow</label>
        </div>

        <button class="button is-danger" @click.prevent="clear" :disabled="!progress">
      <span class="icon">
        <i class="fa-solid fa-xmark"></i>
      </span>
          <span>Clear</span>
        </button>

        <button class="button is-primary" :class="loading ? 'is-loading':''" :disabled="progress">
      <span class="icon">
        <i class="fa-solid fa-check"></i>
      </span>
          <span>Apply</span>
        </button>
      </div>
    </div>
  </form>
  <div id="map"></div>
</template>

<style scoped>
#map {
  height: 85vh;
}

</style>
