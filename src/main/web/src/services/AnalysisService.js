import { get } from '@lib/http'

const count = (params) => {
  return get('/lottery/' + params.lotteryType + '/count')
}

export default {
  count,
}
