<script setup>



import {ref} from "vue";
import GeneralSettings from "@/views/sub-views/settings/GeneralSettings.vue";

const currentTab = ref('general');

const tabs = [
  {
    id: 'general',
    name: 'General',
    icon: 'fas fa-music'
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

    <GeneralSettings v-if="currentTab === 'general'"/>
  </div>
</template>

<style scoped>

</style>
