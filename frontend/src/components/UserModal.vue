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
import UserRoleTag from "@/components/UserRoleTag.vue";
</script>

<template>
  <Modal :id="id">
    <template #title>
      {{ getFullName(user) }}
      <span class="has-text-grey is-size-6">{{ user.email }}</span>
      <UserRoleTag v-if="user.role" :user="user" class="ml-2"/>
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
