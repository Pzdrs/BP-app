<script setup>
import {useNotificationStore} from "@/stores/notification.store";

const props = defineProps({
  notification: Object
});

const notificationStore = useNotificationStore();

const SEVERITY_COLORS = {
  'INFO': 'has-background-success-light',
  'WARNING': 'has-background-warning-light',
  'ERROR': 'has-background-danger-light'
}

function getColor() {
  return SEVERITY_COLORS[props.notification.severity]
}

const dismiss = () => {
  notificationStore.dismissNotification(props.notification.id);
};
</script>

<template>
  <a @click="dismiss" class="list-item" :class="getColor()">
    <div class="list-item-content">
      <div class="list-item-title">{{ notification.title }}</div>
      <div class="list-item-description" style="white-space: normal">{{ notification.message }}</div>
    </div>

    <div v-if="notification.metadata && notification.metadata.actions" class="list-item-controls">
      <RouterLink
          :to="notification.metadata.actions.resolveUrl"
          v-if="notification.metadata.actions.resolveUrl"
          title="Resolve">
        <button class="button">
          <span class="icon is-medium border-1 has-text-success">
            <i class="fa-solid fa-hammer"></i>
          </span>
        </button>
      </RouterLink>
    </div>
  </a>
</template>

<style scoped>

</style>
