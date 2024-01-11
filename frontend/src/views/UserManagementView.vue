<script setup>
import {useUsersStore} from "@/stores/users";
import {onMounted, ref} from "vue";
import {getFullName} from "@/utils/user";
import {closeModalByQuery, openModal, setupModals} from "@/utils/modal";
import UserModal from "@/components/UserModal.vue";
import UserRegistrationModal from "@/components/UserRegistrationModal.vue";
import {toast} from "bulma-toast";
import {useUserStore} from "@/stores/user";

const usersStore = useUsersStore();
const userStore = useUserStore();

const currentUser = ref({});

usersStore.loadUsers(userStore.details.id).then(() => {
  currentUser.value = usersStore.users[0];
});

function openDeleteModal(user) {
  currentUser.value = user;
  openModal('#confirm-delete');
}

function deleteUser() {
  usersStore.deleteUser(currentUser.value.id)
      .then(_ => {
        toast({
          message: 'User deleted',
          type: 'is-success'
        })
        closeModalByQuery('#confirm-delete')
      })
      .catch(_ => {
        toast({
          message: "Couldn't delete user",
          type: 'is-danger'
        })
      });
}

function openUpdateModal(user) {
  currentUser.value = user;
  openModal('#update');
}

function updateUser(event) {
  usersStore.updateUser(currentUser.value.id, Object.fromEntries(new FormData(event.target)))
      .then(_ => {
        toast({
          message: 'User updated',
          type: 'is-success'
        });
        closeModalByQuery('#update');
      })
      .catch(_ => {
        toast({
          message: "Couldn't update user",
          type: 'is-danger'
        });
      });
  closeModalByQuery('#update');
}

function createUser() {
  openModal('#create');
}

onMounted(() => setupModals());
</script>

<template>
  <div class="is-flex is-justify-content-space-between is-align-items-center">
    <p class="is-size-3">User management</p>
    <button class="button is-success" @click.prevent="createUser">
      <span class="icon is-small">
        <i class="fa-solid fa-user-plus"></i>
      </span>
      <span>Add user</span>
    </button>
  </div>
  <hr class="mt-3">
  <div class="list">
    <div v-for="user in usersStore.users" :key="user.id" class="list-item" :style="user.id === userStore.details.id ? 'border-bottom: 1px solid #7a7a7a' : ''">
      <div class="list-item-content">
        <div class="list-item-title">{{ getFullName(user) }}</div>
        <div class="list-item-description">{{ user.email }}</div>
      </div>

      <div class="list-item-controls">
        <div class="buttons is-right">
          <button class="button" @click.prevent="openUpdateModal(user)">
            <span class="icon is-small">
              <i class="fas fa-edit"></i>
            </span>
            <span>Edit</span>
          </button>

          <button v-if="user.id !== userStore.details.id" class="button is-danger" @click.prevent="openDeleteModal(user)">
            <span class="icon is-small">
              <i class="fa-solid fa-user-minus"></i>
            </span>
            <span>Delete</span>
          </button>
        </div>
      </div>
    </div>
  </div>

  <UserModal id="confirm-delete" :user="currentUser">
    <template #content>
      <p>Are you sure you want to delete this user?</p>
    </template>
    <template #footer>
      <button @click="deleteUser" class="button is-danger">Delete</button>
    </template>
  </UserModal>

  <UserModal id="update" :user="currentUser">
    <template #content>
      <form @submit.prevent="updateUser">
        <div class="field">
          <label class="label">First name</label>
          <div class="control">
            <input name="firstName" class="input" type="text" placeholder="First name" :value="currentUser.firstName">
          </div>
        </div>

        <div class="field">
          <label class="label">Last name</label>
          <div class="control">
            <input name="lastName" class="input" type="text" placeholder="Last name" :value="currentUser.lastName">
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

  <UserRegistrationModal/>
</template>

<style scoped>

</style>
