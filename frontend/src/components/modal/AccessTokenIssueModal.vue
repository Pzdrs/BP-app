<script setup>

import Modal from "@/components/modal/Modal.vue";
import {closeModalByQuery} from "@/utils/modal";
import {useTokenStore} from "@/stores/access_token.store";
import {useToast} from "vue-toast-notification";

const tokenStore = useTokenStore();
const $toast = useToast({position: 'top-right'});

function submit(event) {
  if (!event.target.checkValidity()) {
    event.target.reportValidity();
    return;
  }

  tokenStore.issue(Object.fromEntries(new FormData(event.target)))
      .then(_ => {
        $toast.success('Access token issued')
        closeModalByQuery('#create');
        event.target.reset();
      })
      .catch(_ => {
        $toast.error("Failed to issue an access token")
      });

}
</script>

<template>
  <Modal id="create">
    <template #title>
      Issue an access token
    </template>
    <template #content>
      <form @submit.prevent="submit">
        <div class="field">
          <label class="label">Description</label>
          <div class="control">
            <input name="description" class="input" type="text" placeholder="Description">
          </div>
        </div>

        <div class="field">
          <label class="label">Expiry</label>
          <div class="control">
            <input name="expiry" class="input" type="datetime-local">
          </div>
        </div>
      </form>
    </template>
    <template #footer>
      <button
          onclick="this.parentElement.previousElementSibling.querySelector('form').dispatchEvent(new Event('submit'))"
          class="button is-success">
        Issue
      </button>
    </template>
  </Modal>
</template>

<style scoped>

</style>
