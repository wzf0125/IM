import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import VueRouter from 'vue-router'
import 'element-ui/lib/theme-chalk/index.css';
import router from './router/index'
import "./mock/index.js"

Vue.use(VueRouter)
Vue.config.productionTip = false
Vue.use(ElementUI);

import { showError } from "./util/utils";

//登录拦截
router.beforeEach((to, from, next) => {
  if (to.meta.requireAuth) {
    //requireAuth若为true则该路由需要判断是否登录
    if (sessionStorage.getItem("Authorization")) {
      //判断当前的userName数据是否存在
      next();
    } else {
      showError("登录失效");
      next({
        //返回登录页面
        path: "/login",
        query: { redirect: to.fullPath },
      });
    }
  } else {
    next();
  }
});
new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
