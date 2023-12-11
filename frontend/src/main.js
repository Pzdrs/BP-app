import {createApp} from 'vue'
import App from './App.vue'
import router from "@/router";

import './assets/css/style.css'

import FontAwesomeIcon from './font-awesome';

import 'bulma/css/bulma.min.css'
import 'bulma-modal-fx/dist/css/modal-fx.min.css'
import 'bulma-modal-fx/dist/js/modal-fx.min'


const app = createApp(App);

app.use(router);

app.component('font-awesome-icon', FontAwesomeIcon);

app.mount('#app');
