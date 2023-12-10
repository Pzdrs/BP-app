<script setup>

import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

import {onMounted} from "vue";
import Sidebar from "@/components/Sidebar.vue";
import Navbar from "@/components/Navbar.vue";

onMounted(() => {
  // Get all "navbar-burger" elements
  const $navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);

  // Add a click event on each of them
  $navbarBurgers.forEach(el => {
    el.addEventListener('click', () => {
      // Get the target from the "data-target" attribute
      const target = el.dataset.target;
      const $target = document.getElementById(target);

      // Toggle the "is-active" class on both the "navbar-burger" and the "navbar-menu"
      el.classList.toggle('is-active');
      $target.classList.toggle('is-active');

    });
  });
})
</script>

<template>
  <Navbar/>
  <Sidebar/>
  <main>
    <header>
      <div class="mb-3">
        <p class="is-size-3">
          title
        </p>
        <p class="is-size-6 has-text-grey">
          subtitle
        </p>
      </div>
      <div class="is-flex is-justify-content-space-between">
        breadcrumb
        <div></div>
        <div>
          status
        </div>
      </div>
    </header>
    <hr>
    <router-view></router-view>
  </main>
</template>

<style scoped>

</style>
