module.exports = {
  "devServer": {
    https: true,
    "port": 8080,
    proxy: {
      '/lottery': {
        target: 'https://127.0.0.1:8666/lottery', //对应自己的接口
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/lottery': ''
        }
      }
    }
  },
  "outputDir": "../resources/web",
  "transpileDependencies": [
    "vuetify"
  ],

}