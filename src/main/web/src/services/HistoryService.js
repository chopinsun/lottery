import { get } from '@lib/http'

const history = (params) => {
  return get('/lottery/' + params.lotteryType + '/history/' + params.pageSize)
}

export default {
  history,
}
