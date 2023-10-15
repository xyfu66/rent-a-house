const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  devServer: {
    port: 8180,
    https: false,
    open: false,
    proxy: {
      "/api": {
        target: 'http://localhost:8081',
        // 设置是否改变原始主机头为目标 URL 的主机头
        changeOrigin: true,
        // 设置是否支持 websocket
        ws: true,
        pathRewrite: {
          '^/api/': '/'
        }
      }
    }
  }
})
