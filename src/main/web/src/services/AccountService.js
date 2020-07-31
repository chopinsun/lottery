import { post } from '@lib/http'

const login = (params) => {
  if (!params || !params.username || !params.password) {
    return
  }
  return post('/account/login/' + params.username + '/' + params.password)
}

const logout = (params) => {
  return post('/account/logout/' + params.username)
}
const thridAuth = (params) => {
  return post('/account/thirdAuth/' + params.type, params)
}

const resetPwd = (params) => {
  return post('/account/resetPwd', params)
}

const register = (params) => {
  return post('/account/register', params)
}
export default {
  login,
  logout,
  thridAuth,
  resetPwd,
  register,
}
