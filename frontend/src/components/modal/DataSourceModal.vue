<script setup>
import Modal from "@/components/modal/Modal.vue";
import {getDisplayName} from "@/utils/dataSource";

defineProps({
  id: {
    type: String,
    required: true
  },
  dataSource: {
    type: Object,
    required: true
  }
});

</script>

<template>
  <Modal :id="id">
    <template #title>
      {{ getDisplayName(dataSource) }}
      <span class="has-text-grey is-size-6">{{ dataSource.mac }}</span>
    </template>
    <template #content>
      <slot name="content"></slot>
    </template>
    <template #footer>
      <slot name="footer"></slot>
    </template>
  </Modal>
</template>

<style scoped>

</style>
