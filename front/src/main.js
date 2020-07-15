import Vue from 'vue';

import router from './router';
import ElementUI from 'element-ui';
// import 'element-ui/lib/theme-chalk/index.css';
import './assets/theme/index.css'
import './assets/css/main.css';
import store from './store';
import axios from 'axios';
import jwtTokenUtil from "./utils/jwtTokenUtil";

Vue.use(ElementUI);
Vue.prototype.$http = axios;

// http request 拦截器
axios.interceptors.request.use(
    config => {
        console.log(config.method)
        let jwtToken = jwtTokenUtil.readAccess();
        // 给http请求的header加上jwt-token
        config.headers['jwt-token'] = jwtToken;
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

new Vue({
    store,
    router,
}).$mount('#app');

