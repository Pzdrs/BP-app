<script setup>
import {useUserStore} from "@/stores/user";
import {onMounted, ref} from "vue";
import {getFullName} from "@/utils/user";
import {closeModalByQuery, openModal, setupModals} from "@/utils/modal";
import UserModal from "@/components/modal/UserModal.vue";
import UserRegistrationModal from "@/components/modal/UserRegistrationModal.vue";
import {useAuthStore} from "@/stores/auth";
import UserRoleTag from "@/components/UserRoleTag.vue";
import UserUpdateModal from "@/components/modal/UserUpdateModal.vue";
import {useToast} from "vue-toast-notification";

const userStore = useUserStore();
const authStore = useAuthStore();
const $toast = useToast({position: 'top-right'});

const currentUser = ref({});

userStore.loadUsers(authStore.details.id).then(() => {
  currentUser.value = userStore.users[0];
});

function openDeleteModal(user) {
  currentUser.value = user;
  openModal('#confirm-delete');
}

function deleteUser() {
  userStore.deleteUser(currentUser.value.id)
      .then(_ => {
        $toast.success('User deleted');
        closeModalByQuery('#confirm-delete')
      })
      .catch(_ => {
        $toast.error('Failed to delete user');
      });
}

function openUpdateModal(user) {
  currentUser.value = user;
  openModal('#update');
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
    <div v-for="user in userStore.users" :key="user.id" class="list-item"
         :style="user.id === authStore.details.id ? 'border-bottom: 1px solid #7a7a7a' : ''">
      <div class="list-item-content">
        <div class="list-item-title">
          {{ getFullName(user) }}
          <UserRoleTag :user="user"/>
        </div>
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

          <button v-if="user.id !== authStore.details.id" class="button is-danger"
                  @click.prevent="openDeleteModal(user)">
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

  <UserUpdateModal id="update" :user="currentUser"/>

  <UserRegistrationModal/>
</template>

<style scoped>

</style>
