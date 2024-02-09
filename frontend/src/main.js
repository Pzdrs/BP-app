import {createApp} from 'vue'
import App from './App.vue'
import router from "@/router";

import './assets/css/style.css'
import 'bulma/css/bulma.min.css'
import 'bulma-modal-fx/dist/css/modal-fx.min.css'
import 'bulma-modal-fx/dist/js/modal-fx.min'
import '@creativebulma/bulma-badge/dist/bulma-badge.min.css'
import 'bulma-list/css/bulma-list.css'

import 'bulma-calendar/dist/css/bulma-calendar.min.css'
import 'bulma-calendar/dist/js/bulma-calendar.min'

import {createPinia} from "pinia";
import ToastPlugin from "vue-toast-notification";
import 'vue-toast-notification/dist/theme-sugar.css'


const app = createApp(App);

app.use(createPinia());
app.use(router);
app.use(ToastPlugin, {
    position: 'top',
});

app.mount('#app');
