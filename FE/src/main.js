import { createApp } from 'vue'
import App from './App.vue'

import ElementPlus, { ElIcon } from 'element-plus'  //引入element-plus库
import 'element-plus/dist/index.css'  //引入element-plus样式

import VForm3 from 'vform3-builds'  //引入VForm 3库
import 'vform3-builds/dist/designer.style.css'  //引入VForm3样式

import axios from 'axios'

import * as ELIcons from '@element-plus/icons-vue'

import store from './store'

const app = createApp(App)
app.use(ElementPlus)  //全局注册element-plus
app.use(VForm3)  //全局注册VForm 3(同时注册了v-form-designer和v-form-render组件)
// 全局导入plus图标
for (const iconName in ElIcon) {
    app.component(iconName, ELIcons[iconName])
}
app.use(store)

app.config.globalProperties.$axios = axios

app.mount('#app')
