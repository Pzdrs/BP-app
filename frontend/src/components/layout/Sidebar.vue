<script setup>
import {computed} from "vue";
import router from "@/router";
import {useAuthStore} from "@/stores/auth.store";

const authStore = useAuthStore();

const links = computed(() => {
  return router.getRoutes()
      .filter(route => route.meta.nav)
      .filter(authStore.hasViewPermission)
});
</script>

<template>
  <aside class="menu">
    <div class="mb-0 is-flex is-align-items-center is-hidden-mobile">
      <div class="ml-2">
        <figure class="image is-32x32">
          <img alt="Profile picture" class="is-rounded" src="https://github.com/mdo.png">
        </figure>
      </div>
      <div class="ml-2">
        <p class="has-text-weight-bold">{{ authStore.getFullName }}</p>
        <p class="text-muted">{{ authStore.details.email }}</p>
      </div>
    </div>

    <div class="mb-0 is-flex is-flex-direction-column is-align-items-center is-justify-content-center is-hidden-tablet">
      <div class="ml-2 mb-1">
        <figure class="image is-32x32">
          <img alt="Profile picture" class="is-rounded" src="https://github.com/mdo.png">
        </figure>
      </div>
      <div class="ml-2">
        <p class="has-text-weight-bold">{{ authStore.getFullName }}</p>
        <p class="text-muted">{{ authStore.details.email }}</p>
      </div>
    </div>
    <hr class="my-2">
    <ul class="menu-list">
      <li v-for="link in links" :key="link.name.toLowerCase()">
        <router-link :to="link.path">
          <i :class="link.meta.icon" class="mr-2"></i>
          {{ link.name }}
        </router-link>
      </li>
    </ul>
  </aside>
</template>

<style scoped>

</style>
