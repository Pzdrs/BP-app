<script setup>
import axios from "@/axios";
import {onMounted} from "vue";
import {toast} from "bulma-toast";
import {useConfigurationStore} from "@/stores/configuration";

const configurationStore = useConfigurationStore();


function submitMqtt(event) {
  configurationStore
      .updateMqttConfig(Object.fromEntries(new FormData(event.target).entries()))
      .then(_ => {
        toast({
          message: 'MQTT settings updated',
          type: 'is-success',
          dismissible: true,
        });
      })
      .catch(err => {
        const errors = err.response.data;
        const message = Object.keys(errors).map(key => `- ${key} ${errors[key]}`).join('\n');
        toast({
          message: 'Failed to update MQTT settings\n\n' + message,
          type: 'is-danger',
          dismissible: true,
          duration: 5000
        });
      });
}

onMounted(async () => {
  await configurationStore.loadMqttConfig();

  const form = document.querySelector('form#mqtt');
  Object.keys(configurationStore.mqtt).forEach(key => {
    const input = form.querySelector(`input[name="${key}"]`);
    if (input) {
      input.value = configurationStore.mqtt[key];
    }
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
            <input name="host" class="input" type="text" placeholder="Server address">
            <span class="icon is-small is-left">
            <font-awesome-icon icon="fa-solid fa-server"/>
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
            <font-awesome-icon icon="fa-solid fa-user"/>
          </span>
          </p>
        </div>
        <div class="field">
          <p class="control is-expanded has-icons-left">
            <input name="password" class="input" type="password" placeholder="Password">
            <span class="icon is-small is-left">
            <font-awesome-icon icon="fa-solid fa-lock"/>
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
