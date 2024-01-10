<script setup>
import Modal from "@/components/Modal.vue";

defineProps({
  id: {
    type: String,
    required: true
  },
  user: {
    type: Object,
    required: true
  }
});

import {getFullName} from "@/utils/user";
</script>

<template>
  <Modal :id="id">
    <template #title>
      {{ getFullName(user) }}
      <span class="has-text-grey is-size-6">{{ user.email }}</span>
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
