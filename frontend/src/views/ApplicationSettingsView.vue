<script setup>


import {ref} from "vue";
import MqttSettings from "@/views/sub-views/settings/MqttSettings.vue";
import DataManagement from "@/views/sub-views/settings/DataManagement.vue";

const currentTab = ref('mqtt');

const tabs = [
  {
    id: 'mqtt',
    name: 'MQTT',
    icon: 'fa-solid fa-envelope'
  },
  {
    id: 'data',
    name: 'Data',
    icon: 'fa-solid fa-database'
  }
];

</script>

<template>
  <div>
    <div class="tabs">
      <ul>
        <li v-for="tab in tabs" :key="tab.id" :class="{ 'is-active': tab.id === currentTab }">
          <a @click="currentTab = tab.id">
            <span class="icon is-small">
              <i :class="tab.icon"></i>
            </span>
            <span>{{ tab.name }}</span>
          </a>
        </li>
      </ul>
    </div>

    <MqttSettings v-if="currentTab === 'mqtt'"/>
    <DataManagement v-if="currentTab === 'data'"/>
  </div>
</template>

<style scoped>

</style>
