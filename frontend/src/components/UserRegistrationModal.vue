<script setup>

import Modal from "@/components/Modal.vue";
import UserRegistrationForm from "@/components/UserRegistrationForm.vue";

</script>

<template>
  <Modal id="create">
    <template #title>
      Add user
    </template>
    <template #content>
      <UserRegistrationForm/>
    </template>
    <template #footer>
      <button
          onclick="this.parentElement.previousElementSibling.querySelector('form').dispatchEvent(new Event('submit'))"
          class="button is-success">
        Create
      </button>
    </template>
  </Modal>
</template>

<style scoped>

</style>
