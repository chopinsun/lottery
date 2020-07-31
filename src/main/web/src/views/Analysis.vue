<template>
  <div class="Analysis">
    <v-toolbar flat dense>
      <div style="height:25px;font-size:14px;">号码次数分布</div>
    </v-toolbar>
    <ve-scatter
      :data="chartData"
      :settings="chartSettings"
      :legend-visible="true"
      :judge-width="true"
      theme-name="lottery"
      :mark-line="markLine"
      :mark-point="markPoint"
      :colors="colors"
      style="min-height:460px;"
    ></ve-scatter>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex'
import service from '@service/AnalysisService'
import { nav } from '@store/types'
export default {
  data() {
    this.markLine = {
      data: [
        {
          name: '平均线',
          type: 'average',
        },
      ],
    }
    this.markPoint = {
      data: [
        {
          name: '最大值',
          type: 'max',
        },
      ],
    }
    this.dataZoom = [
      {
        type: 'slider',
        start: 0,
        end: 100,
      },
    ]
    this.colors = [
      '#d95850',
      '#0098d9',
      '#e6b600',
      '#2b821d',
      '#005eaa',
      '#339ca8',
      '#cda819',
      '#32a487',
    ]

    this.chartSettings = {
      labelMap: {
        redBall: '红球',
        blueBall: '蓝球',
      },
    }
    return {
      chartData: {
        columns: ['num', 'redBall', 'blueBall'],
        rows: [],
      },
    }
  },

  mounted() {
    this.search()
  },
  activated() {
    this.showTopNav()
    this.showBotNav()
  },
  computed: {
    ...mapState({
      lotteryType: (state) => state.lottery.type,
    }),
  },
  watch: {
    lotteryType() {
      this.search()
    },
  },
  methods: {
    ...mapMutations({
      showTopNav: nav.SHOW_TOP,
      showBotNav: nav.SHOW_BOT,
    }),
    async search() {
      const result = await service.count({
        lotteryType: this.lotteryType,
      })
      this.chartData.rows = result
    },
  },
}
</script>

<style scoped>
.Analysis {
  height: 100%;
}
.homepage ul li span {
  width: 25px;
  height: 25px;
  border-radius: 25px;
}
.homepage ul li span.redball {
  background: red;
}
.homepage ul li span.blueball {
  background: blue;
}
</style>
