<script setup>
import UserModal from "@/components/modal/UserModal.vue";
import {closeModalByQuery} from "@/utils/modal";
import {useUserStore} from "@/stores/user.store";
import {useToast} from "vue-toast-notification";

const userStore = useUserStore();
const $toast = useToast({position: 'top-right'});

const props = defineProps({
  id: {
    type: String,
    required: true
  },
  user: {
    type: Object,
    required: true
  }
});

function updateUser(event) {
  userStore.updateUser(props.user.id, Object.fromEntries(new FormData(event.target)))
      .then(_ => {
        $toast.success('User updated');
        closeModalByQuery('#update');
      })
      .catch(_ => {
        $toast.error('Failed to update user');
      });
  closeModalByQuery('#update');
}
</script>

<template>
  <UserModal :id="id" :user="user">
    <template #content>
      <form @submit.prevent="updateUser">
        <div class="field">
          <label class="label">First name</label>
          <div class="control">
            <input name="firstName" class="input" type="text" placeholder="First name" :value="user.firstName">
          </div>
        </div>

        <div class="field">
          <label class="label">Last name</label>
          <div class="control">
            <input name="lastName" class="input" type="text" placeholder="Last name" :value="user.lastName">
          </div>
        </div>

        <div class="field">
          <label class="label">Role</label>
          <div class="select">
            <select name="role">
              <option v-for="role in userStore.roles" :key="role" :value="role" :selected="user.role===role">
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
      </form>
    </template>
    <template #footer>
      <button
          onclick="this.parentElement.previousElementSibling.querySelector('form').dispatchEvent(new Event('submit'))"
          class="button is-info">
        Update
      </button>
    </template>
  </UserModal>
</template>

<style scoped>

</style>
