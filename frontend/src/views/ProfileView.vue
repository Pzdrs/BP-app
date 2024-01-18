<script setup>
import {useAuthStore} from "@/stores/auth";
import {useUserStore} from "@/stores/user";
import {useToast} from "vue-toast-notification";

const authStore = useAuthStore();
const userStore = useUserStore();

const $toast = useToast({position: 'top-right'});

userStore.loadRoles();

function updateUser(event) {
  userStore.updateUser(authStore.details.id, Object.fromEntries(new FormData(event.target)))
      .then(_ => $toast.success('Profile updated'))
      .catch(_ => $toast.error('Failed to update profile'));
  event.target.querySelector('input[name="password"]').value = '';
}
</script>

<template>
  <p class="is-size-3">My profile</p>
  <hr class="mt-3">

  <form @submit.prevent="updateUser">
    <div class="field">
      <label class="label">First name</label>
      <div class="control">
        <input name="firstName" class="input" type="text" placeholder="First name" :value="authStore.details.firstName">
      </div>
    </div>

    <div class="field">
      <label class="label">Last name</label>
      <div class="control">
        <input name="lastName" class="input" type="text" placeholder="Last name" :value="authStore.details.lastName">
      </div>
    </div>

    <div class="field">
      <label class="label">Role</label>
      <div class="select">
        <select name="role">
          <option
              v-for="role in userStore.roles" :key="role"
              :value="role"
              :selected="authStore.details.role === role"
          >
            {{ role }}
          </option>
        </select>
      </div>
    </div>

    <div class="field">
      <label class="label">Password</label>
      <p class="control is-expanded has-icons-left">
        <input name="password" class="input" type="password" placeholder="Password">
        <span class="icon is-small is-left">
          <i class="fas fa-lock"></i>
        </span>
      </p>
    </div>

    <div class="field">
      <div class="control">
        <button class="button is-primary" type="submit">Update</button>
      </div>
    </div>
  </form>
</template>

<style scoped>

</style>
