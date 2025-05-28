import { createApp } from "vue";
import { createPinia } from "pinia";
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import App from "./App.vue";
import router from "./router";

/* global css */
import "./assets/styles/variables.css";
import "./assets/styles/form.css";

/* import the fontawesome core */
import { library } from "@fortawesome/fontawesome-svg-core";

/* import font awesome icon component */
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";

/* import specific icons */
import {
  faChevronLeft,
  faChevronRight,
  faXmark,
  faCar,
  faHeart,
  faPlus,
  faCheck,
  faBars,
  faLocationArrow,
  faPhone,
  faLocationDot,
  faPenToSquare,
  faTrash,
} from "@fortawesome/free-solid-svg-icons";

import { faHeart as farHeart } from "@fortawesome/free-regular-svg-icons";

/* add icons to the library */
library.add(
  faChevronLeft,
  faChevronRight,
  faXmark,
  faCar,
  faHeart,
  faPlus,
  faCheck,
  faBars,
  faLocationArrow,
  faPhone,
  faLocationDot,
  faPenToSquare,
  faTrash,
  farHeart
);

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.component("font-awesome-icon", FontAwesomeIcon);
app.use(router);
app.use(pinia);
app.mount("#app");
