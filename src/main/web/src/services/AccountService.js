import { get, post } from '@lib/http'

const login = (params) => {
  return post('/login/' + params.username + '/' + params.password)
}

const logout = (params) => {
  return post('/logout/' + params.username)
}
const thridAuth = (params) => {
  return post('/lottery/' + params.lotteryType + '/history/' + params.pageSize)
}
export default {
  login,
  logout,
  thridAuth,
}
