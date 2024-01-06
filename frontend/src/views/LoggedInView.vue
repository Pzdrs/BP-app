<script setup>
import Sidebar from "@/components/Sidebar.vue";
import Navbar from "@/components/Navbar.vue";

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
