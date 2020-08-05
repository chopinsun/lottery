module.exports = {
  devServer: {
    disableHostCheck: true,
    hotOnly: true,
    https: true,
    port: 8080,
    proxy: {
      '/lottery': {
        target: 'https://127.0.0.1:8666/lottery', //对应自己的接口
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/lottery': '',
        },
      },
      '/account': {
        target: 'https://127.0.0.1:8666/account', //对应自己的接口
        changeOrigin: true,
        ws: true,
        pathRewrite: {
          '^/account': '',
        },
      },
    },
    public: '0.0.0.0:8080',
  },
  outputDir: '../resources/web',
  transpileDependencies: ['vuetify'],
  configureWebpack: {
    resolve: {
      alias: {
        '@assets': '@/assets',
        '@components': '@/components',
        '@service': '@/services',
        '@store': '@/store',
        '@router': '@/router',
        '@lib': '@/lib',
        '@plugins': '@/plugins',
        '@utils': '@/utils',
        '@views': '@/views',
      },
    },
  },
  productionSourceMap: false,
  runtimeCompiler: false,
}
