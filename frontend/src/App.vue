<template>
  <v-layout>
    <Navbar/>
    <Sidebar/>

    <v-main style="height: 250px">
      <router-view/>
    </v-main>
  </v-layout>
</template>

<script setup>
import Sidebar from "./components/Sidebar.vue";
import Navbar from "./components/Navbar.vue";
</script>
