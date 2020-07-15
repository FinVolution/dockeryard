/**
 * Created by lizhiming on 2017/8/17.
 */
import Vue from 'vue'
import Vuex from 'vuex'
import registry from './modules/registry'
import pauth from './modules/pauth'

Vue.use(Vuex);

const debug = process.env.NODE_ENV !== 'production';

export default new Vuex.Store({
    modules: {
        registry,
        pauth
    },
    strict: debug
})

