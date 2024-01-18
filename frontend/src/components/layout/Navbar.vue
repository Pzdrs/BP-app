<script setup>


import {useAuthStore} from "@/stores/auth.store";
import router from "@/router";
import {onMounted} from "vue";
import {useNotificationStore} from "@/stores/notification.store";
import NotificationItem from "@/components/NotificationItem.vue";
import {useToast} from "vue-toast-notification";

const authStore = useAuthStore();
const notificationStore = useNotificationStore();
const $toast = useToast({position: 'top-right'});

function logout() {
  authStore.signOut().then(_ => {
    router.push({name: 'Login'});
    $toast.success('Logged out')
  });
}

onMounted(() => {
  const navbarBurgers = Array.prototype.slice.call(document.querySelectorAll('.navbar-burger'), 0);
  const notificationDropdown = document.getElementById('notification-dropdown');

  navbarBurgers.forEach(el => {
    el.addEventListener('click', () => {
      const target = el.dataset.target;
      const $target = document.getElementById(target);

      el.classList.toggle('is-active');
      $target.classList.toggle('is-active');
    });
  });

  notificationDropdown.addEventListener('click', () => {
    notificationDropdown.classList.toggle('is-active');
  });
  notificationDropdown.addEventListener('focusout', () => {
    notificationDropdown.classList.remove('is-active');
  });
})
</script>

<template>
  <nav class="navbar is-fixed-top" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <a class="navbar-item" href="/public">
        ESGPS
      </a>

      <a role="button" class="navbar-burger" aria-label="menu" aria-expanded="false"
         data-target="navbar">
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>

    <div id="navbar" class="navbar-menu">
      <div class="navbar-end">

        <div class="navbar-item">
          <div class="is-flex is-justify-content-end">
            <div id="notification-dropdown" class="dropdown is-right">
              <div class="dropdown-trigger">
                <button class="button" aria-controls="notifications-dropdown">
                  <span class="icon is-small">
                    <span v-if="notificationStore.hasNotifications" class="badge is-danger">{{notificationStore.notifications.length}}</span>
                    <i class="fas fa-bell"></i>
                  </span>
                </button>
              </div>

              <div class="dropdown-menu" id="notifications-dropdown" role="menu">
                <div class="dropdown-content py-0">
                  <div class="list has-overflow-ellipsis" style="width: 340px">
                    <NotificationItem :notification="notification" v-for="notification in notificationStore.notifications" :key="notification.id"/>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="navbar-item">
          <a href="#" @click.prevent="logout">
            <i class="fas fa-right-to-bracket"></i>
          </a>
        </div>
      </div>
    </div>
  </nav>
</template>

<style scoped>

</style>
