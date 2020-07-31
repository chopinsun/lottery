import axios from 'axios'

axios.defaults.timeout = 10000 //响应时间
axios.defaults.headers.post['Content-Type'] =
  'application/x-www-form-urlencoded;charset=UTF-8' //配置请求头
axios.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest' //配置请求头
axios.defaults.headers.get['Content-Type'] = 'application/json;charset=UTF-8' //配置请求头
axios.defaults.headers.get['X-Requested-With'] = 'XMLHttpRequest' //配置请求头
axios.defaults.withCredentials = true

axios.defaults.baseURL = '' //配置接口地址
//POST传参序列化(添加请求拦截器)
axios.interceptors.request.use(
  (config) => {
    //在发送请求之前做某件事
    if (config.method === 'post') {
      config.data = JSON.stringify(config.data)
    }
    return config
  },
  (error) => {
    $alert.error('错误的传参')
    return Promise.reject(error)
  }
)
//返回状态判断(添加响应拦截器)
axios.interceptors.response.use(
  (res) => {
    //对响应数据做些事
    if (res && res.status != 200) {
      res.data.msg ? $alert.warn(res.data.msg) : false
      return Promise.reject(res)
    } else {
      return res.data
    }
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      console.log(`未登录`)
      const location = error.response.headers['location']
      if (location) {
        const protocol = window.location.protocol
        const hostname = window.location.hostname
        const port = window.location.port ? ':' + window.location.port : ''
        const pathname = window.location.pathname
        const hash = window.location.hash
        const returnUrlBackcall = `${protocol}//${hostname}${port}${pathname}${hash}`
        const rurl = `${location.split('?')[0]}?ReturnUrl=${returnUrlBackcall}`
        console.log(`跳转登录页面-->  ${rurl}`)
        window.location.href = rurl
      }
    } else {
      $alert.error('网络异常', error.response)
      return Promise.reject(error)
    }
  }
)

const get = (url, params) => {
  const config = {
    params: params,
  }
  return new Promise((resolve, reject) => {
    axios
      .get(url, config)
      .then(
        (response) => {
          if (response) {
            resolve(response.data)
          } else {
            resolve()
          }
        },
        (err) => {
          reject(err)
        }
      )
      .catch((error) => {
        reject(error)
      })
  })
}
//返回一个Promise(发送post请求)
const post = (url, params) => {
  return new Promise((resolve, reject) => {
    axios
      .post(url, params)
      .then(
        (response) => {
          resolve(response.data)
        },
        (err) => {
          reject(err)
        }
      )
      .catch((error) => {
        reject(error)
      })
  })
}

const ajax = (url, params) => {
  return new Promise((resolve, reject) => {
    axios
      .ajax(url, params)
      .then(
        (response) => {
          resolve(response.data)
        },
        (err) => {
          reject(err)
        }
      )
      .catch((error) => {
        reject(error)
      })
  })
}

const http = axios

export { http, ajax, get, post }
export default get
