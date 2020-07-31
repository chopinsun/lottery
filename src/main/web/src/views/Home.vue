<template>
  <div class="homepage">
    <v-list>
      <v-list-item v-for="(item,idx) in items" :key="idx">
        <v-list-item-content v-show="type==='ssq'">
          <v-row style="margin:0">
            <v-col style="padding:0;max-width:12.6%;line-height:44px;" v-for="n in 6" :key="n">
              <v-btn fab dark x-small color="red">
                <v-icon dark>{{item.nums[n-1]}}</v-icon>
              </v-btn>
            </v-col>
            <v-col style="padding:0;max-width:12.6%;line-height:44px;">
              <v-btn fab dark x-small color="blue">
                <v-icon dark>{{item.nums[6]}}</v-icon>
              </v-btn>
            </v-col>
            <v-col style="padding:0;max-width:12.6%;line-height:44px;">
              <v-btn icon @click="chooseNum(item)">
                <v-icon>{{item.status}}</v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-list-item-content>

        <v-list-item-content v-show="type==='dlt'">
          <v-row style="margin:0">
            <v-col style="padding:0;max-width:12.6%;line-height:44px;" v-for="n in 5" :key="n">
              <v-btn fab dark x-small color="red">
                <v-icon dark>{{item.nums[n-1]}}</v-icon>
              </v-btn>
            </v-col>
            <v-col style="padding:0;max-width:12.6%;line-height:44px;">
              <v-btn fab dark x-small color="blue">
                <v-icon dark>{{item.nums[5]}}</v-icon>
              </v-btn>
            </v-col>
            <v-col style="padding:0;max-width:12.6%;line-height:44px;">
              <v-btn fab dark x-small color="blue">
                <v-icon dark>{{item.nums[6]}}</v-icon>
              </v-btn>
            </v-col>
            <v-col style="padding:0;max-width:12.6%;line-height:44px;">
              <v-btn icon @click="chooseNum(item)">
                <v-icon>{{item.status}}</v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-list-item-content>
      </v-list-item>
    </v-list>
    <!-- <v-btn absolute  dark  fab right color="pink" class="bottom80">
        <v-icon>mdi-plus</v-icon>
    </v-btn>-->
    <v-btn fixed dark fab right color="pink" class="bottom80" v-on:click="search">
      <v-icon>mdi-sync</v-icon>
    </v-btn>
    <div class="footer"></div>
    <v-overlay :value="overlay">
      <v-progress-circular indeterminate size="64"></v-progress-circular>
    </v-overlay>
  </div>
</template>

<script>
import { mapState, mapMutations, mapGetters } from 'vuex'
import { lottery, favorite, nav } from '@store/types'
import service from '@service/LotteryService'
export default {
  data() {
    return {
      items: [],
      overlay: false,
      selectedList: new Array(),
    }
  },
  mounted() {
    if (!this.items.length) {
      this.search()
    }
  },
  activated() {
    this.showTopNav()
    this.showBotNav()
  },
  watch: {
    config() {
      this.search()
    },
    'favoriteList.length': {
      handler() {
        this.mergeList()
      },
      immediate: true,
    },
  },
  computed: {
    ...mapState({
      mod: (state) => state.lottery.mod,
      type: (state) => state.lottery.type,
      num: (state) => state.lottery.num,
      favoriteList: (state) => state.favorite.list,
    }),
    config() {
      return this.type + this.mod + this.num
    },
  },
  methods: {
    async search() {
      this.items = []
      this.overlay = true
      const result = await service.lottery({
        mod: this.mod,
        type: this.type,
        num: this.num,
      })
      if (result) {
        this.items = []
        result.forEach((i) =>
          this.items.push({ nums: i, status: 'mdi-heart-outline' })
        )
        this.mergeList()
      }
      this.overlay = false
    },
    chooseNum(item) {
      if (item.status === 'mdi-heart-outline') {
        item.status = 'mdi-heart'
      } else if (item.status === 'mdi-heart') {
        item.status = 'mdi-heart-outline'
      }
      if (this.selectedList.indexOf(item.nums) != -1) {
        this.selectedList = this.selectedList.filter((i) => i != item.nums)
      } else {
        this.selectedList.push(item.nums)
      }
      this.setFavorite(this.selectedList)
    },
    ...mapMutations({
      pushFavorite: favorite.PUSH,
      setFavorite: favorite.SET,
      removeFavorite: favorite.RMOVE,
      clearFavorite: favorite.CLEAR,
      showTopNav: nav.SHOW_TOP,
      showBotNav: nav.SHOW_BOT,
    }),
    mergeList() {
      this.selectedList = [...this.favoriteList]
      this.items.forEach((i) => {
        const n = this.selectedList.filter((j) => j == i.nums)
        if (n.length) {
          i.status = 'mdi-heart'
        } else {
          i.status = 'mdi-heart-outline'
        }
      })
    },
  },
}
</script>

<style scoped>
.v-list-item {
  padding: 0 8px;
}
.homepage {
  height: 100%;
}

.homepage .checkbox {
  align-items: center;
  display: inline-flex;
  flex: 0 0 auto;
  font-weight: 500;
  letter-spacing: 0.0892857143em;
  justify-content: center;
  position: relative;
  text-decoration: none;
  text-align: center;
  text-indent: 0.0892857143em;
  text-transform: uppercase;
  transition-duration: 0.28s;
  transition-property: box-shadow, transform, opacity;
  transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
  vertical-align: middle;
  white-space: nowrap;
  border-style: none;
  width: 40px;
  height: 40px;
  line-height: 40px;
  margin: 2px;
  box-sizing: border-box;
  box-shadow: 0px 3px 5px -1px rgba(0, 0, 0, 0.2),
    0px 6px 10px 0px rgba(0, 0, 0, 0.14), 0px 1px 18px 0px rgba(0, 0, 0, 0.12);
  will-change: box-shadow;
  color: #ffffff;
  background-color: #009688;
  border-color: #009688;
  border-radius: 10%;
  margin: 0 10px;
}
.homepage .checkbox:hover {
  background-color: #3dafa4;
  border-color: #3dafa4;
}

.homepage .checkbox.selected {
  background-color: #6ec3bb;
  border-color: #6ec3bb;
}
.bottom80 {
  bottom: 80px;
}
.bottom100 {
  bottom: 160px;
}

/* .v-btn{
     margin: 10px 0 ;
   } */
.v-input--radio-group__input {
  display: inline;
}
.v-radio {
  display: inline;
  padding: 0 10px;
}

.footer {
  height: 56px;
}
.choosed {
  background-color: #009688 !important;
  color: #fff !important;
}

.v-btn--fab.v-btn--contained {
  box-shadow: none !important;
}
</style>
