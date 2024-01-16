<script setup>
import Sidebar from "@/components/layout/Sidebar.vue";
import Navbar from "@/components/layout/Navbar.vue";

</script>

<template>
  <Navbar/>
  <Sidebar/>
  <main>
    <router-view></router-view>
  </main>
</template>

<style scoped>

</style>
