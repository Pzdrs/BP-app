<script setup>
import {computed} from "vue";

const props = defineProps({
  user: Object
});

const COLORS = {
  'USER': 'is-primary',
  'ADMINISTRATOR': 'is-danger',
};

const roleName = computed(() => {
  return props.user.role.charAt(0).toUpperCase() + props.user.role.slice(1).toLowerCase()
})

const color = computed(() => {
  return COLORS[props.user.role]
})
</script>

<template>
  <span class="tag" :class="color">{{ roleName }}</span>
</template>

<style scoped>

</style>
