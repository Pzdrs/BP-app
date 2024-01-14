import {createApp} from 'vue'
import App from './App.vue'
import router from "@/router";

import './assets/css/style.css'
import 'bulma/css/bulma.min.css'
import 'bulma-modal-fx/dist/css/modal-fx.min.css'
import 'bulma-modal-fx/dist/js/modal-fx.min'
import '@creativebulma/bulma-badge/dist/bulma-badge.min.css'
import 'bulma-list/css/bulma-list.css'

import {createPinia} from "pinia";


const app = createApp(App);

app.use(createPinia());
app.use(router);

app.mount('#app');
