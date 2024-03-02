<script setup>
import {closeModalByQuery} from "@/utils/modal";
import {useToast} from "vue-toast-notification";
import {getSelectedOptions} from "@/utils/forms";
import {useTokenStore} from "@/stores/access_token.store";
import Modal from "@/components/modal/Modal.vue";

const tokenStore = useTokenStore();

const $toast = useToast({position: 'top-right'});

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  token: {
    type: Object,
    required: true
  }
});

function updateToken(event) {
  const data = Object.fromEntries(new FormData(event.target))
  data.disabled = event.target.querySelector('input[name="disabled"]').checked;

  tokenStore.updateToken(props.token.id, data)
      .then(_ => {
        $toast.success('Token updated');
        closeModalByQuery('#update');
      })
      .catch(_ => {
        $toast.error('Failed to update token');
      });
  closeModalByQuery('#update');
}

</script>

<template>
  <Modal :id="id">
    <template #title>
      Update token
    </template>
    <template #content>
      <form @submit.prevent="updateToken">
        <div class="field">
          <label class="label">Description</label>
          <div class="control">
            <input name="description" class="input" type="text" placeholder="Description" :value="token.description">
          </div>
        </div>

        <div class="field">
          <label class="label">Expiry</label>
          <div class="control">
            <input name="expiry" class="input" type="datetime-local" placeholder="Expiry" :value="token.expiry">
          </div>
        </div>

        <div class="field">
          <div class="control">
            <label class="checkbox">
              <input name="disabled" type="checkbox" :checked="token.disabled">
              Disabled
            </label>
          </div>
        </div>
      </form>
    </template>
    <template #footer>
      <button
          onclick="this.parentElement.previousElementSibling.querySelector('form').dispatchEvent(new Event('submit'))"
          class="button is-info">
        Update
      </button>
    </template>
  </Modal>
</template>

<style scoped>

</style>
