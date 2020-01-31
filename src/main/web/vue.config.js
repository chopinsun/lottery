module.exports = {
  "devServer": {
    https: true,
    "port": 443,
    proxy: {
      '/lottery': {
        target: 'http://127.0.0.1:8080/lottery', //对应自己的接口
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/lottery': ''
        }
      }
    }
  },
  "outputDir": "../../resources/web",
  "transpileDependencies": [
    "vuetify"
  ],

}