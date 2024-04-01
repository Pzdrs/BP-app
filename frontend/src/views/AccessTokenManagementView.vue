<script setup>
import {onMounted, ref} from "vue";
import {closeModalByQuery, openModal, setupModals} from "@/utils/modal";
import {useToast} from "vue-toast-notification";
import {useTokenStore} from "@/stores/access_token.store";
import NoAccessTokensIssuedYet from "@/components/message/NoAccessTokensIssuedYet.vue";
import {getFullName} from "@/utils/user";
import {localizeDateTime, toDate} from "@/utils/dates";
import Modal from "@/components/modal/Modal.vue";
import AccessTokenUpdateModal from "@/components/modal/AccessTokenUpdateModal.vue";
import AccessTokenIssueModal from "@/components/modal/AccessTokenIssueModal.vue";

const tokenStore = useTokenStore();
const $toast = useToast({position: 'top-right'});

const currentToken = ref({});

tokenStore.loadTokens();

function openDeleteModal(token) {
  currentToken.value = token;
  openModal('#confirm-delete');
}

function deleteToken() {
  tokenStore.deleteToken(currentToken.value.id)
      .then(_ => {
        $toast.success('Access token revoked');
        closeModalByQuery('#confirm-delete')
      })
      .catch(_ => {
        $toast.error('Failed to revoke token');
      });
}

function openUpdateModal(token) {
  currentToken.value = token;
  openModal('#update');
}

function copyIntoClipboard(token) {
  navigator.clipboard.writeText(token.value);
  $toast.info('Token copied to clipboard');
}

function issueToken() {
  openModal('#create');
}

onMounted(() => setupModals());
</script>

<template>
  <div class="is-flex is-justify-content-space-between is-align-items-center">
    <p class="is-size-3">Access tokens</p>
    <button class="button is-success" @click.prevent="issueToken">
      <span class="icon is-small">
        <i class="fa-solid fa-plus"></i>
      </span>
      <span>Add token</span>
    </button>
  </div>
  <hr class="mt-3">
  <NoAccessTokensIssuedYet v-if="tokenStore.isNoTokens"/>
  <div class="list">
    <div v-for="token in tokenStore.tokens" :key="token.id" class="list-item">
      <div class="list-item-content">
        <div class="list-item-title">
          {{ token.value }} <span v-if="token.disabled" class="tag is-danger">Disabled</span>
        </div>
        <div class="list-item-description">
          <p class="mb-1">{{ token.description || 'No description' }}</p>
          <p>
            Issued by: <span class="has-text-black">
            {{ getFullName(token.user) }} ({{ localizeDateTime(token.created) }})
          </span>
          </p>
          <p>
            Expires:
            <span v-if="token.expiry === null" class="has-text-black">Never</span>
            <span v-else-if="toDate(token.expiry) > Date.now()" class="has-text-black">
              {{ localizeDateTime(token.expiry) }}
            </span>
            <span v-else class="has-text-danger has-text-weight-bold">Expired</span>
          </p>
        </div>
      </div>

      <div class="list-item-controls">
        <div class="buttons is-right">
          <button class="button" @click.prevent="copyIntoClipboard(token)">
            <span class="icon is-small">
              <i class="fa-regular fa-clipboard"></i>
            </span>
            <span>Copy</span>
          </button>

          <button class="button" @click.prevent="openUpdateModal(token)">
            <span class="icon is-small">
              <i class="fas fa-edit"></i>
            </span>
            <span>Edit</span>
          </button>

          <button class="button is-danger" @click.prevent="openDeleteModal(token)">
            <span class="icon is-small">
              <i class="fa-solid fa-user-minus"></i>
            </span>
            <span>Revoke</span>
          </button>
        </div>
      </div>
    </div>
  </div>

  <Modal id="confirm-delete">
    <template #title>
      {{ currentToken.description || currentToken.id }}
    </template>
    <template #content>
      <p>Are you sure you want to revoke this access token?</p>
    </template>
    <template #footer>
      <button @click="deleteToken" class="button is-danger">Revoke</button>
    </template>
  </Modal>

  <AccessTokenUpdateModal id="update" :token="currentToken"/>

  <AccessTokenIssueModal/>
</template>

<style scoped>

</style>
