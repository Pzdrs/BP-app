<script setup>
import {useAuthStore} from "@/stores/auth.store";
import {useRouter} from "vue-router";
import {useToast} from "vue-toast-notification";

const authStore = useAuthStore();
const router = useRouter();
const $toast = useToast({position: 'top-right'});

function login(event) {
  authStore
      .signIn(Object.fromEntries(new FormData(event.target).entries()))
      .then(_ => {
        router.push({name: 'Dashboard'});
        $toast.success('Logged in successfully');
      })
      .catch(err => {
        if (err.response.status === 401)
          $toast.error('Incorrect username or password');
        else {
          $toast.error('Failed to log in')
        }
      });
}
</script>

<template>
  <div class="hero is-fullheight" style="background-color: #efefef">
    <div class="hero-body is-justify-content-center is-align-items-center">
      <form @submit.prevent="login">
        <div class="columns is-flex is-flex-direction-column box">
          <div class="column">
            <label class="label" for="username">Username</label>
            <input name="username" class="input is-info" type="text" placeholder="Username" required>
          </div>
          <div class="column">
            <label class="label" for="password">Password</label>
            <input name="password" class="input is-info" type="password" placeholder="Password" required>
            <a href="#" class="is-size-7 has-text-grey">Forgot password?</a>
          </div>
          <div class="column">
            <button class="button is-link is-fullwidth" type="submit">
              <i class="fa-solid fa-right-to-bracket"></i>&nbsp;
              Sign in
            </button>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
</style>
