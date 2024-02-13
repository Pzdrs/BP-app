<script setup>
import {onMounted, ref} from "vue";
import {useDataPointStore} from "@/stores/datapoint.store";
import {useDataSourceStore} from "@/stores/datasource.store";
import bulmaCalendar from "bulma-calendar";
import NoDataSourcesAdoptedYetMessage from "@/components/message/NoDataSourcesAdoptedYetMessage.vue";
import {localizeDateTime} from "@/utils/dates";
import {useToast} from "vue-toast-notification";

const loading = ref(false);

let map = null;


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
      console.log(`Fetched ${dataPointStore.dataPoints.length} data points from ${dataSource.name}`)

      dataPointStore.dataPoints.forEach(point => {
        let dataPoint = L.circle(point, {
          color: dataSource.color,
          fillOpacity: 1,
          radius: 1
        }).addTo(map);

        dataPoint.bindPopup(`
      <div>
    <strong>${point.id}</strong>
    <hr class="my-1">
    <div class="is-flex is-justify-content-space-around">
    <span>
        <i class="fa-solid fa-arrows-left-right" title="Longitude"></i>
        ${point.lng}
    </span>
    <span>
        <i class="fa-solid fa-arrows-up-down" title="Latitude"></i>
        ${point.lat}
    </span>

</div>
<div class="is-flex is-justify-content-space-around mt-1">
    <span>
        <i class="fa-solid fa-arrows-up-to-line" title="Altitude"></i>
        ${point.alt}m
    </span>
</div>
   <div class="is-flex is-justify-content-space-around my-2">
    <span class="tag is-info">
      <i class="fa-solid fa-clock mr-1"></i>
        ${localizeDateTime(point.timestamp)}
    </span>
</div>
<div class="is-flex is-justify-content-space-around">
 <span class="tag is-warning">
      <i class="fa-solid fa-truck-fast mr-1"></i>
        <b class="pr-1">0</b> km/h
    </span>
    <span class="tag is-primary">
      <i class="fa-regular fa-compass mr-1"></i>
        West
    </span>
</div>
  </div>
    `)
      })

      L.polyline(dataPointStore.getLatLngs, {color: dataSource.color}).addTo(map);
    });
  }

  loading.value = false;
  window.location.href = '#map';
}

function clear() {
  dataPointStore.clear();
}


onMounted(() => {
  map = L.map('map').setView([50.433125, 14.630015], 19);
  L.tileLayer('https://tile.openstreetmap.org/{z}/{x}/{y}.png', {
    maxZoom: 19,
    attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(map);

  bulmaCalendar.attach('[type="datetime-local"][name="from"]', {
    type: 'datetime',
    color: 'info',
    startDate: new Date(),
    validateLabel: 'Apply',
  });

  bulmaCalendar.attach('[type="datetime-local"][name="to"]', {
    type: 'datetime',
    color: 'info',
    startDate: new Date(),
    startTime: '23:59',
    validateLabel: 'Apply',
  });

  let dataPoint = L.circle([50.433125, 14.630015], {
    color: 'red',
    fillOpacity: 1,
    radius: 1
  }).addTo(map);

  dataPoint.bindPopup(`
      <div>
    <strong>65ca8063917973aac04200f7</strong>
    <hr class="my-1">
    <div class="is-flex is-justify-content-space-around">
    <span>
        <i class="fa-solid fa-arrows-left-right" title="Longitude"></i>
        14.63443
    </span>
    <span>
        <i class="fa-solid fa-arrows-up-down" title="Latitude"></i>
        50.43497
    </span>

</div>
<div class="is-flex is-justify-content-space-around mt-1">
    <span>
        <i class="fa-solid fa-arrows-up-to-line" title="Altitude"></i>
        136.5m
    </span>
</div>
   <div class="is-flex is-justify-content-space-around my-2">
    <span class="tag is-info">
      <i class="fa-solid fa-clock mr-1"></i>
        12. 2. 2024 12:00
    </span>
</div>
<div class="is-flex is-justify-content-space-around">
 <span class="tag is-warning">
      <i class="fa-solid fa-truck-fast mr-1"></i>
        <b class="pr-1">55</b> km/h
    </span>
    <span class="tag is-primary">
      <i class="fa-regular fa-compass mr-1"></i>
        West
    </span>
</div>
  </div>
    `)
})
;
</script>

<template>

  <form class="mb-5" @submit.prevent="submit">
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
        <button class="button is-danger" @click="clear">
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
