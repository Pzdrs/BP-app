<script setup>
import {onMounted, ref} from "vue";
import {useDataPointStore} from "@/stores/datapoint.store";
import {useDataSourceStore} from "@/stores/datasource.store";
import bulmaCalendar from "bulma-calendar";
import router from "@/router";
import {end} from "@popperjs/core";
import NoDataSourcesAdoptedYetMessage from "@/components/message/NoDataSourcesAdoptedYetMessage.vue";

const loading = ref(false);

let map = null;


const dataPointStore = useDataPointStore();
const dataSourceStore = useDataSourceStore();

dataSourceStore.loadDataSources();
dataSourceStore.loadGroups();

async function submit(event) {
  loading.value = true;

  const fromDate = new Date(event.target.from.value).toISOString();
  const toDate = new Date(event.target.to.value).toISOString();
  const selectedDataSources = Array.from(document.querySelector('#data-sources').querySelectorAll('input[type="checkbox"]:checked')).map(checkbox => checkbox.getAttribute('data-source-id'));

  console.log(fromDate, toDate, selectedDataSources);

  dataPointStore.clear(selectedDataSources);

  for (let selectedDataSource of selectedDataSources) {
    const dataSource = dataSourceStore.getDataSourceById(selectedDataSource);
    await dataPointStore.fetchDataPoints(selectedDataSource).then(() => {
      dataPointStore.dataPoints.forEach(point => {
        let dataPoint = L.circle(point, {
          color: dataSource.color,
          fillOpacity: 1,
          radius: 1
        }).addTo(map);

        dataPoint.bindPopup(`
      <div>
        <div>Id: ${point.id}</div>
        <div>Latitude: ${point.lat}</div>
        <div>Longitude: ${point.lng}</div>
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

}


onMounted(() => {
  map = L.map('map').setView([50.433125, 14.630015], 16);
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
});
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
