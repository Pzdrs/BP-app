<script setup>
import {onMounted, ref} from "vue";
import {closeModalByQuery, openModal, setupModals} from "@/utils/modal";
import {useDataSourceStore} from "@/stores/datasource.store";
import {getDisplayName, getRandomHexColor} from "@/utils/dataSource";
import {localizeDateTime} from "@/utils/dates";
import Modal from "@/components/modal/Modal.vue";
import DataSourceModal from "@/components/modal/DataSourceModal.vue";
import {useToast} from "vue-toast-notification";
import {getSelectedOptions} from "@/utils/forms";
import NoDataSourcesAdoptedYetMessage from "@/components/message/NoDataSourcesAdoptedYetMessage.vue";

const dataSourceStore = useDataSourceStore();
const $toast = useToast({position: 'top-right'});

const currentDataSource = ref({});

dataSourceStore.loadDataSources().then(() => {
  currentDataSource.value = dataSourceStore.dataSources[0];
});

dataSourceStore.loadGroups();

function hasGroup(group) {
  if (!currentDataSource.value.groups) {
    return false;
  }
  return currentDataSource.value.groups.includes(group);
}

function confirmDeleteDataSource(dataSource) {
  currentDataSource.value = dataSource;
  openModal('#confirm-delete');
}

function adoptDataSource(event) {
  dataSourceStore.adoptDataSource(currentDataSource.value.id, Object.fromEntries(new FormData(event.target)))
      .then(_ => {
        $toast.success('Data source adopted');
      })
      .catch(_ => {
        $toast.error('Failed to adopt data source');
      });
  closeModalByQuery('#adopt');
}

function openAdoptModal(dataSource) {
  currentDataSource.value = dataSource;
  openModal('#adopt')
  document.querySelector('input[name="color"]').value = getRandomHexColor();
}

function deleteDataSource() {
  dataSourceStore.deleteDataSource(currentDataSource.value.id)
      .then(_ => {
        $toast.success('Data source deleted');
        closeModalByQuery('#confirm-delete')
      })
      .catch(_ => {
        $toast.error('Failed to delete data source');
      });
}

function openUpdateModal(dataSource) {
  currentDataSource.value = dataSource;
  openModal('#update');

}

function updateDataSource(event) {
  const data = Object.fromEntries(new FormData(event.target));
  data.groups = getSelectedOptions(event.target.elements['groups']);

  dataSourceStore.updateDataSource(currentDataSource.value.id, data)
      .then(_ => {
        $toast.success('Data source updated');
      })
      .catch(_ => {
        $toast.error('Failed to update data source');
      });
  closeModalByQuery('#update');
}

function createNewGroup() {
  const newGroup = document.querySelector('input#new-group').value;
  if (newGroup.length > 0) document.querySelector('select[name="groups"]').append(new Option(newGroup, newGroup));
}

function randomizeColor(target) {
  target.target.parentElement.parentElement.querySelector('input[name="color"]').value = getRandomHexColor();
}

onMounted(() => {
  setupModals();
});
</script>

<template>
  <p class="is-size-3">Data source management</p>
  <hr class="mt-3">

  <article v-for="dataSource in dataSourceStore.getAdoptionAwaitingDataSources" :key="dataSource.id"
           class="message is-link">
    <div class="message-header">
      <p>New device discovered - <span class="has-text-weight-normal">{{ dataSource.mac }}</span></p>
    </div>
    <div class="message-body">
      <p>
        A new device has been discovered to be sending data to the configured MQTT server.
      </p>
      <div class="is-flex is-justify-content-end">
        <button class="button" @click="openAdoptModal(dataSource)">Adopt device</button>
      </div>
    </div>
  </article>

  <NoDataSourcesAdoptedYetMessage v-if="dataSourceStore.dataSources.length === 0"/>

  <div class="list">
    <div v-for="dataSource in dataSourceStore.getAdoptedDataSources" :key="dataSource.id" class="list-item">
      <div class="list-item-image">
        <div class="data-source-color-indicator" :style="`background-color: ${dataSource.color}`"></div>
      </div>

      <div class="list-item-content">
        <div class="list-item-title">{{ getDisplayName(dataSource) }}</div>
        <div class="list-item-description is-flex is-justify-content-space-between is-align-items-center">
          <div>
            <p>{{ dataSource.mac }}</p>
            <p>Discovered: <span>{{ localizeDateTime(dataSource.created) }}</span></p>
            <p>Last modified: <span>{{ localizeDateTime(dataSource.updated) }}</span></p>
          </div>
          <div class="tags are-medium">
            <span v-for="group in dataSource.groups" class="tag">{{ group }}</span>
          </div>
        </div>
      </div>

      <div class="list-item-controls">
        <div class="buttons is-right">
          <button class="button" @click.prevent="openUpdateModal(dataSource)">
            <span class="icon is-small">
              <i class="fas fa-edit"></i>
            </span>
            <span>Edit</span>
          </button>

          <button class="button is-danger" @click.prevent="confirmDeleteDataSource(dataSource)">
            <span class="icon is-small">
              <i class="fa-solid fa-minus"></i>
            </span>
            <span>Delete</span>
          </button>
        </div>
      </div>
    </div>
  </div>

  <template v-if="dataSourceStore.dataSources.length > 0">
    <Modal id="adopt">
      <template #title>
        Adopt data source
      </template>

      <template #content>
        <form @submit.prevent="adoptDataSource">
          <div class="field">
            <label class="label">Name</label>
            <div class="control">
              <input name="name" class="input" type="text" placeholder="Unnamed data source" required>
            </div>
          </div>

          <div class="field">
            <label class="label">Color</label>
            <div class="control">
              <input name="color" class="input" type="color" placeholder="Color" required>
            </div>
          </div>
        </form>
      </template>

      <template #footer>
        <button
            onclick="this.parentElement.previousElementSibling.querySelector('form').dispatchEvent(new Event('submit'))"
            class="button is-info">
          Adopt
        </button>
      </template>
    </Modal>

    <DataSourceModal id="confirm-delete" :data-source="currentDataSource">
      <template #content>
        <p>Are you sure you want to delete this data source?</p>
      </template>
      <template #footer>
        <button @click="deleteDataSource" class="button is-danger">Delete</button>
      </template>
    </DataSourceModal>

    <DataSourceModal id="update" :data-source="currentDataSource">
      <template #content>
        <form @submit.prevent="updateDataSource">
          <div class="field">
            <label class="label">Name</label>
            <div class="control">
              <input name="name" class="input" type="text" :value="currentDataSource.name" required>
            </div>
          </div>

          <label class="label">Color</label>
          <div class="field has-addons">
            <div class="control is-expanded">
              <input name="color" class="input" type="color" :value="currentDataSource.color" required>
            </div>
            <div class="control">
              <button class="button" title="Random color" @click.prevent="randomizeColor">
                Randomize
              </button>
            </div>
          </div>

          <div class="field">
            <label class="label">Groups</label>
            <div class="control">
              <div class="select is-multiple">
                <select name="groups" multiple>
                  <option v-for="group in dataSourceStore.groups" :key="group" :value="group"
                          :selected="hasGroup(group)">
                    {{ group }}
                  </option>
                </select>
              </div>
            </div>
            <div class="control mt-3">
              <div class="field has-addons">
                <p class="control">
                  <input id="new-group" class="input" type="text" placeholder="New data source group">
                </p>
                <p class="control">
                  <button class="button" @click.prevent="createNewGroup">
                    Add
                  </button>
                </p>
              </div>
            </div>
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
    </DataSourceModal>
  </template>
</template>

<style scoped>
.data-source-color-indicator {
  width: 48px;
  height: 48px;
  border-radius: 100%;
  border: 1px solid #7c7c7c;
}
</style>
