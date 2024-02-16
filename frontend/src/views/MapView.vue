<script setup>
import {onMounted, ref} from "vue";
import {useDataPointStore} from "@/stores/datapoint.store";
import {useDataSourceStore} from "@/stores/datasource.store";
import bulmaCalendar from "bulma-calendar";
import NoDataSourcesAdoptedYetMessage from "@/components/message/NoDataSourcesAdoptedYetMessage.vue";
import {useToast} from "vue-toast-notification";
import {getPopUpHTML, mockPopUp} from "@/utils/dataPoint";

const loading = ref(false);

let map = null;

let timeframe = [];

const dataPoints = L.layerGroup();

const dataPointStore = useDataPointStore();
const dataSourceStore = useDataSourceStore();

const $toast = useToast({position: 'top-right'});

dataSourceStore.loadDataSources();
dataSourceStore.loadGroups();

async function submit(event) {
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
    await dataPointStore.fetchDataPoints(selectedDataSource, startDate, endDate).then(() => {
      if (dataPointStore.dataPoints.length === 0) {
        $toast.info(`No data points from <b>${dataSource.name}</b> in the selected time range`);
        return;
      }

      $toast.success(`Fetched ${dataPointStore.dataPoints.length} data points from <b>${dataSource.name}</b>`);

      for (let i = 0; i < dataPointStore.dataPoints.length; i++) {
        const point = dataPointStore.dataPoints[i];
        const prevPoint = i === 0 ? {speed: 0} : dataPointStore.dataPoints[i - 1];

        let dataPoint = L.circle(point, {
          color: dataSource.color,
          fillOpacity: 1,
          radius: 1
        }).addTo(map);

        dataPoint.bindPopup(getPopUpHTML(point, prevPoint))

        dataPoints.addLayer(dataPoint);
      }

      L.polyline(dataPointStore.getLatLngs, {color: dataSource.color}).addTo(map);
    });
  }

  loading.value = false;
  window.location.href = '#map';
}

function clear() {
  dataPointStore.clear();

  // Reset map
  map.eachLayer(layer => {
    if (layer instanceof L.Polyline || layer instanceof L.Circle) {
      map.removeLayer(layer);
    }
  });

  setupCalendars();
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
        <NoDataSourcesAdoptedYetMessage v-if="dataSourceStore.dataSources.length === 0"/>
        <div v-for="dataSource in dataSourceStore.dataSources" :key="dataSource.id" class="field">
          <label class="checkbox">
            <input type="checkbox" :style="{'accent-color': dataSource.color}" :data-source-id="dataSource.id">
            {{ dataSource.name }}
          </label>
        </div>
      </div>
      <div class="column is-6">
        <p class="is-size-4">Timeframe</p>
        <hr class="my-2">
        <div class="is-flex is-justify-content-space-around">
          <input name="from" type="datetime-local">
          <input name="to" type="datetime-local">
        </div>
      </div>
    </div>
    <div class="field is-flex is-justify-content-flex-end">
      <div class="buttons">
        <button class="button is-danger" @click.prevent="clear">
      <span class="icon">
        <i class="fa-solid fa-xmark"></i>
      </span>
          <span>Clear</span>
        </button>

        <button class="button is-primary" :class="loading ? 'is-loading':''">
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
