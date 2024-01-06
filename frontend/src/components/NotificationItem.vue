<script setup>
const props = defineProps({
  notification: Object
});

const SEVERITY_COLORS = {
  'INFO': 'has-background-success-light',
  'WARNING': 'has-background-warning-light',
  'ERROR': 'has-background-danger-light'
}

function getColor() {
  return SEVERITY_COLORS[props.notification.severity]
}
</script>

<template>
  <a class="list-item" :class="getColor()">
    <div class="list-item-content">
      <div class="list-item-title">{{ notification.title }}</div>
      <div class="list-item-description">{{ notification.message }}</div>
    </div>

    <div v-if="notification.metadata && notification.metadata.actions" class="list-item-controls">
      <router-link :to="notification.metadata.actions.resolve_url" v-if="notification.metadata.actions.resolve_url"
         title="Resolve">
        <span class="icon is-small has-text-success">
          <i class="fa-solid fa-hammer"></i>
        </span>
      </router-link>
    </div>
  </a>
</template>

<style scoped>

</style>
