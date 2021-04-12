import Vue from "vue";
import VueRouter from "vue-router";
import App from "./App.vue";
import Login from "./components/Login";
import BikeList from "./components/BikeList";
import AddBike from "./components/AddBike";
import vuetify from "./plugins/vuetify";
import Vuex from "vuex";

Vue.use(Vuex);
const store = new Vuex.Store({
  state: {
    currentUser: {
      username: "",
      token: "",
    },
    isAuthenticated: false,
  },
  mutations: {
    setUser(state, user) {
      Object.assign(state.currentUser, user);
    },
    setIsAuthenticated(state, isAuthenticated) {
      state.isAuthenticated = isAuthenticated;
    },
  },
  actions: {
    setUser(context, user) {
      context.commit("setUser", user);
    },
  },
});

Vue.use(VueRouter);
const routes = [
  { path: "/", name: "Login", component: Login },
  { path: "/bikes", name: "Bikes", component: BikeList },
  { path: "/addBike", name: "AddBike", component: AddBike },
];
const router = new VueRouter({
  routes,
});
router.beforeEach((to, from, next) => {
  if (
    to.name !== "Login" &&
    store.state.currentUser.username == "" &&
    store.state.currentUser.token == ""
  )
    next({ name: "Login" });
  else next();
});

Vue.config.productionTip = false;

new Vue({
  router,
  vuetify,
  store: store,
  render: (h) => h(App),
}).$mount("#app");
