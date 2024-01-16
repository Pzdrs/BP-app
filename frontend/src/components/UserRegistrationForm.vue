<script setup>
import {ref} from "vue";
import {useUsersStore} from "@/stores/users";
import {closeModalByQuery} from "@/utils/modal";
import {useToast} from "vue-toast-notification";

const usersStore = useUsersStore();

const passwordsMatch = ref(true);
const emailIsInvalid = ref(false);

const $toast = useToast({position: 'top-right'});

function submit(event) {
  if (!passwordsMatch.value) {
    event.target.querySelector("input#confirm-password").setCustomValidity("Passwords do not match");
  } else {
    event.target.querySelector("input#confirm-password").setCustomValidity("");
  }

  if (!event.target.checkValidity()) {
    event.target.reportValidity();
    return;
  }

  usersStore.createUser(Object.fromEntries(new FormData(event.target)))
      .then(_ => {
        $toast.success('User created')
        closeModalByQuery('#create');
        event.target.reset();
      })
      .catch(_ => {
        $toast.error("Failed to create user")
      });

}

function checkPasswordMatch() {
  const password = document.querySelector("input#register[name='password']").value;
  const confirmPassword = document.querySelector("input#confirm-password").value;
  passwordsMatch.value = password === confirmPassword;
}
</script>

<template>
  <form @submit.prevent="submit">
    <div class="field">
      <label class="label">First name</label>
      <div class="control">
        <input name="firstName" class="input" type="text" placeholder="First name">
      </div>
    </div>

    <div class="field">
      <label class="label">Last name</label>
      <div class="control">
        <input name="lastName" class="input" type="text" placeholder="Last name">
      </div>
    </div>

    <div class="field">
      <label class="label">Email</label>
      <div class="control has-icons-left has-icons-right">
        <input name="email" class="input" :class="emailIsInvalid ? 'is-danger' : ''" type="email" placeholder="Email"
               required>
        <span class="icon is-small is-left">
          <i class="fas fa-envelope"></i>
        </span>
        <span v-if="emailIsInvalid" class="icon is-small is-right">
          <i class="fas fa-exclamation-triangle"></i>
        </span>
      </div>
      <p v-if="emailIsInvalid" class="help is-danger">This email is invalid</p>
    </div>

    <div class="field">
      <label class="label">Role</label>
      <div class="select">
        <select name="role">
          <option v-for="role in usersStore.roles" :key="role" :value="role">{{ role }}</option>
        </select>
      </div>
    </div>

    <label class="label">Password</label>
    <div class="field is-horizontal">
      <div class="field-body">
        <div class="field">
          <p class="control is-expanded has-icons-left">
            <input id="register" @input="checkPasswordMatch" name="password" class="input" type="password"
                   placeholder="Password"
                   required>
            <span class="icon is-small is-left">
              <i class="fas fa-lock"></i>
            </span>
          </p>
        </div>
        <div class="field">
          <p class="control is-expanded has-icons-left has-icons-right">
            <input @input="checkPasswordMatch" id="confirm-password" class="input"
                   :class="passwordsMatch ? 'is-success' : 'is-danger'"
                   type="password" placeholder="Confirm password" required>
            <span class="icon is-small is-left">
              <i class="fas fa-lock"></i>
            </span>
            <span class="icon is-small is-right">
              <i v-if="passwordsMatch" class="fas fa-check"></i>
              <i v-else class="fas fa-exclamation-triangle"></i>
            </span>
          </p>
        </div>
      </div>
    </div>
  </form>
</template>

<style scoped>

</style>
