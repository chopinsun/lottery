import { createModules } from '@lib/module'

export const alter = createModules('alter', {
  M: ['PUSH', 'REMOVE'],
  G: ['MESSAGE', 'LEVEL'],
})

export const nav = createModules('nav', {
  M: ['CHOOSE_PAGE', 'SHOW_TOP', 'HIDE_TOP', 'SHOW_BOT', 'HIDE_BOT'],
  G: ['CURRENT_PAGE', 'SHOWTOPNAV', 'SHOWBOTNAV'],
})

export const account = createModules('account', {
  M: ['SAVE_USERINFO', 'SAVE_CONFIG', 'SAVE_FAVORITE'],
  G: ['USERINFO', 'CONFIG', 'FAVORITE', 'AUTHOTITY'],
})
export const lottery = createModules('lottery', {
  M: ['SET_MOD', 'SET_TYPE', 'SET_NUM', 'SET_CONFIG'],
  G: ['CONFIG'],
})

export const map = createModules('map', {
  M: ['SET_RANGE'],
  G: ['RANGE'],
})
export const chart = createModules('chart', {
  M: ['SET_MOD'],
  G: ['MOD'],
})

export const history = createModules('history', {
  M: ['FROM', 'TO', 'LIMIT'],
  G: ['SET_FROM', 'SET_TO', 'SET_LIMIT'],
})
export const favorite = createModules('favorite', {
  M: ['PUSH', 'REMOVE', 'CLEAR', 'SET'],
  G: ['LIST'],
})
export default {
  nav,
  account,
  lottery,
  map,
  chart,
  history,
  favorite,
}
