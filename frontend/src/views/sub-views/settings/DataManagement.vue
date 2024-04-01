<script setup>
import {useToast} from "vue-toast-notification";
import ConfirmationModal from "@/components/modal/ConfirmationModal.vue";
import {closeAllModals, openModal, setupModals} from "@/utils/modal";
import axios from "@/axios";
import {useDataPointStore} from "@/stores/datapoint.store";
import {useDataSourceStore} from "@/stores/datasource.store";
import {onMounted} from "vue";


const $toast = useToast({position: 'top-right'});

function eraseDataSources() {
  axios.delete('/datasource/erase').then(_=> {
    useDataSourceStore().clear();
    $toast.success('Data sources erased');
    closeAllModals();
  }).catch(_ => {
    $toast.error('Failed to erase data sources');
  });
}

function eraseLocationData() {
  axios.delete('/datapoint/erase').then(_=> {
    useDataPointStore().clear();
    $toast.success('All location data erased');
    closeAllModals();
  }).catch(_ => {
    $toast.error('Failed to erase location data');
  });
}

function confirmEraseDataSources() {
  openModal('#confirm-erase-data-sources');
}

function confirmEraseLocationData() {
  openModal('#confirm-erase-location-data');
}

onMounted(() => setupModals());

</script>

<template>
  <p class="is-size-5 mb-3">Data</p>
  <div class="buttons">
    <button class="button is-danger" @click="confirmEraseDataSources">
      Erase data sources
    </button>
    <button class="button is-danger" @click="confirmEraseLocationData">
      Erase location data
    </button>
  </div>

  <ConfirmationModal id="confirm-erase-data-sources">
    <template #content>
      <p>Are you sure you want to erase all data sources?</p>
    </template>
    <template #footer>
      <button class="button is-danger" @click="eraseDataSources">Erase</button>
    </template>
  </ConfirmationModal>
  <ConfirmationModal id="confirm-erase-location-data">
    <template #content>
      <p>Are you sure you want to erase all collected location data?</p>
    </template>
    <template #footer>
      <button class="button is-danger" @click="eraseLocationData">Erase</button>
    </template>
  </ConfirmationModal>
</template>

<style scoped>

</style>
