<script setup>

import Modal from "@/components/modal/Modal.vue";

defineProps({
  id: {
    type: String,
    required: true
  },
});
</script>

<template>
  <Modal :id="id">
    <template #title>
      Please confirm this action
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