/**
 * Created by lizhiming on 2017/8/17.
 */
import Vue from 'vue';
import Router from 'vue-router';
import Login from '../components/Login.vue';
import App from '../components/layout/App.vue';

import BlankPage from '../pages/Blank.vue'
import EnvList from '../pages/admin/EnvList.vue'
import OrgList from '../pages/admin/OrgList.vue'
import UserList from '../pages/admin/UserList.vue'
import ImageList from '../pages/image/ImageList.vue'
import RepoList from '../pages/image/RepoList.vue'
import OperationLog from '../pages/dashboard/OperationLog.vue'
import Statistics from '../pages/dashboard/Statistics.vue'


Vue.use(Router);

export default new Router({
    mode: "hash",
    routes: [{
        path: '',
        component: App,
        children: [{
            path: '',
            name: 'default',
            component: BlankPage
        },{
            path: '/admin/env',
            name: 'env',
            component: EnvList
        }, {
            path: '/admin/org',
            name: 'org',
            component: OrgList
        }, {
            path: '/admin/user',
            name: 'user',
            component: UserList
        }, {
            path: '/image/list',
            name: 'imagelist',
            component: ImageList
        }, {
            path: '/image/repo',
            name: 'repolist',
            component: RepoList
        }, {
            path: '/dashboard/log',
            name: 'dashboard-log',
            component: OperationLog
        }, {
            path: '/dashboard/statistics',
            name: 'dashboard-statistics',
            component: Statistics
        }]
    }, {
        path: '/login',
        name: 'Login',
        component: Login,
    }, {
        path: '/',
        redirect: '/login'
    }],
    linkActiveClass: 'active'
})
