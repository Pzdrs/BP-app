<script setup>
import UserModal from "@/components/modal/UserModal.vue";
import {closeModalByQuery} from "@/utils/modal";
import {useUserStore} from "@/stores/user.store";
import {useToast} from "vue-toast-notification";
import {useDataSourceStore} from "@/stores/datasource.store";
import {getNameOrId} from "@/utils/dataSource";
import {getSelectedOptions} from "@/utils/forms";

const userStore = useUserStore();
const dataSourceStore = useDataSourceStore();

dataSourceStore.loadDataSources();
dataSourceStore.loadGroups();

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
  const data = Object.fromEntries(new FormData(event.target))

  if (props.user.role !== 'ADMINISTRATOR') {
    data.dataSources = getSelectedOptions(event.target.elements['datasources']);
    data.dataSourceGroups = getSelectedOptions(event.target.elements['datasource-groups']);
  }

  userStore.updateUser(props.user.id, data)
      .then(_ => {
        $toast.success('User updated');
        closeModalByQuery('#update');
      })
      .catch(_ => {
        $toast.error('Failed to update user');
      });
  closeModalByQuery('#update');
}

function isAssignedDataSource(dataSourceId) {
  try {
    return props.user.assignedDataSources.includes(dataSourceId);
  } catch (_) {
    return false;
  }
}

function isAssignedDataSourceGroup(group) {
  try {
    return props.user.assignedDataSourceGroups.includes(group);
  } catch (_) {
    return false;
  }
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

        <section v-if="user.role !== 'ADMINISTRATOR'">
          <p class="is-size-5 has-text-weight-bold mt-4 mb-0">Assigned data sources</p>

          <div class="field is-horizontal">
            <div class="field-body">
              <div class="field">
                <label class="label">Individual</label>
                <div class="select is-multiple">
                  <select name="datasources" multiple>
                    <option v-for="dataSource in dataSourceStore.dataSources" :key="dataSource.id"
                            :value="dataSource.id" :selected="isAssignedDataSource(dataSource.id)">
                      {{ getNameOrId(dataSource) }}
                    </option>
                  </select>
                </div>
              </div>

              <div class="field">
                <label class="label">Groups</label>
                <div class="select is-multiple">
                  <select name="datasource-groups" multiple>
                    <option v-for="group in dataSourceStore.groups" :key="group" :value="group"
                            :selected="isAssignedDataSourceGroup(group)">
                      {{ group }}
                    </option>
                  </select>
                </div>
              </div>
            </div>
          </div>
        </section>
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
