<script setup>
const tabs = [
  {
    name: "general",
    label: "General",
    component: () => import("./sub-views/settings/GeneralSettings.vue"),
  },
];

</script>

<template>
  <v-tabs background-color="gray">
    <v-tab v-for="tab in tabs" :value="tab.name">{{ tab.label }}</v-tab>
  </v-tabs>
</template>

<style scoped>

</style>
