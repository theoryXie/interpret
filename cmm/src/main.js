// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import VueCodemirror from 'vue-codemirror'
// require styles
import 'element-ui/lib/theme-chalk/index.css'
import 'codemirror/lib/codemirror.css'

var axios = require('axios')
axios.defaults.baseURL = 'http://localhost:8089/api'
// 全局注册，之后可在其他组件中通过 this.$axios 发送数据
Vue.prototype.$axios = axios
Vue.use(VueCodemirror)
Vue.use(ElementUI)
Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
