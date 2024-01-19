<script setup>
import {useNotificationStore} from "@/stores/notification.store";

const notificationStore = useNotificationStore();

notificationStore.listenForNotifications();
</script>

<template>
  <RouterView/>
</template>
