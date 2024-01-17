<script setup>
import Sidebar from "@/components/layout/Sidebar.vue";
import Navbar from "@/components/layout/Navbar.vue";
import {useAuthStore} from "@/stores/auth";
import {useNotificationStore} from "@/stores/notification";

const authStore = useAuthStore();
const notificationStore = useNotificationStore();

notificationStore.loadNotifications();
</script>

<template>
  <Navbar/>
  <Sidebar v-if="authStore.isAuthenticated"/>
  <main>
    <router-view></router-view>
  </main>
</template>

<style scoped>

</style>
