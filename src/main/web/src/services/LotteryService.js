import { get } from '@lib/http'

const lottery = (params) => {
  return get(
    '/lottery/' + params.type + '/generate/' + params.mod + '/' + params.num
  )
}

export default {
  lottery,
}
