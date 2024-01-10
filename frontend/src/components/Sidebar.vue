<script setup>
import {computed} from "vue";
import router from "@/router";
import {useUserStore} from "@/stores/user";

const userStore = useUserStore();

const links = computed(() => {
  return router.getRoutes().filter(route => route.meta.nav)
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
        <p class="has-text-weight-bold">{{userStore.details.firstName}} {{userStore.details.lastName}}</p>
        <p class="text-muted">{{userStore.details.email}}</p>
      </div>
    </div>

    <div class="mb-0 is-flex is-flex-direction-column is-align-items-center is-justify-content-center is-hidden-tablet">
      <div class="ml-2 mb-1">
        <figure class="image is-32x32">
          <img alt="Profile picture" class="is-rounded" src="https://github.com/mdo.png">
        </figure>
      </div>
      <div class="ml-2">
        <p class="has-text-weight-bold">Petr Bohac</p>
        <p class="text-muted">petr.bohac@remeslovkostce.cz</p>
      </div>
    </div>
    <hr class="my-2">
    <ul v-for="link in links" :key="link.name.toLowerCase()" class="menu-list">
      <li>
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
