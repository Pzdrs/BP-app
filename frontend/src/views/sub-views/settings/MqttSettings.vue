<script setup>
import {onMounted} from "vue";
import {useConfigurationStore} from "@/stores/configuration.store";
import {useToast} from "vue-toast-notification";


const configurationStore = useConfigurationStore();
const $toast = useToast({position: 'top-right'});


const submitMqtt = (event) => {
  configurationStore
      .updateMqttConfig(Object.fromEntries(new FormData(event.target).entries()))
      .then(_ => {
        $toast.success('MQTT settings updated');
      })
      .catch(err => {
        const errors = err.response.data;
        const message = Object.keys(errors).map(key => `- ${key} ${errors[key]}`).join('\n');
        $toast.error('Failed to update MQTT settings\n\n' + message);
      });
}

onMounted(() => {
  configurationStore.loadMqttConfig().then(_ => {
    const form = document.querySelector('form#mqtt');
    Object.keys(configurationStore.mqtt).forEach(key => {
      const input = form.querySelector(`input[name="${key}"]`);
      if (input) {
        input.value = configurationStore.mqtt[key];
      }
    });
  });
});
</script>

<template>
  <p class="is-size-5 mb-3">MQTT</p>
  <form @submit.prevent="submitMqtt" id="mqtt">
    <div class="field is-horizontal">
      <div class="field-body">
        <div class="field">
          <p class="control is-expanded has-icons-left">
            <input name="host" class="input" type="text" placeholder="Server address" required>
            <span class="icon is-small is-left">
            <i class="fa-solid fa-server"></i>
          </span>
          </p>
        </div>
        <div class="field">
          <p class="control">
            <input name="port" class="input" type="number" placeholder="Port" required>
          </p>
        </div>
      </div>
    </div>

    <div class="field is-horizontal">
      <div class="field-body">
        <div class="field">
          <p class="control is-expanded has-icons-left">
            <input name="username" class="input" type="text" placeholder="Username">
            <span class="icon is-small is-left">
            <i class="fa-solid fa-user"></i>
          </span>
          </p>
        </div>
        <div class="field">
          <p class="control is-expanded has-icons-left">
            <input name="password" class="input" type="password" placeholder="Password">
            <span class="icon is-small is-left">
            <i class="fa-solid fa-lock"></i>
          </span>
          </p>
        </div>
      </div>
    </div>

    <div class="field is-horizontal">
      <div class="field-body">
        <div class="field">
          <div class="control">
            <button class="button is-primary">
              Submit
            </button>
          </div>
        </div>
      </div>
    </div>
  </form>

</template>

<style scoped>

</style>
