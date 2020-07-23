<template>
  <v-app>
    <v-app-bar absolute color="teal lighten-3" dark shrink-on-scroll dense  scroll-target="#scrolling-techniques" >
      <v-app-bar-nav-icon></v-app-bar-nav-icon>

      <v-toolbar-title>{{lottery[lotteryType].name}}</v-toolbar-title>

      <v-spacer></v-spacer>

      
      <v-btn icon @click="showNumbers=true">
         <v-badge overlap left color="error"  :content="chooseNums.length" offset-x="8px" offset-y="9px">
          <v-icon>mdi-heart-multiple</v-icon>
          </v-badge>
      </v-btn>

      <v-btn icon @click="toggleType">
        <v-icon>{{lottery[lotteryType].icon}}</v-icon>
      </v-btn>
      <v-btn icon @click.stop="dialog=true">
        <v-icon>mdi-cog</v-icon>
      </v-btn>
       <v-btn icon @click="bottomNav = 'account'">
        <v-icon>mdi-account</v-icon>
      </v-btn>
     
      <v-btn icon >
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>
    </v-app-bar>

    <v-sheet id="scrolling-techniques" class="overflow-y-auto" max-height="600">
      <v-container style="margin-top:96px;">
        <component v-bind:is="currentTabComponent" :lotteryMod ="lotteryMod" :lotteryType="lotteryType" :currentNum="currentNum" v-on:chooseNum="setChooseNum"></component>
      </v-container>
    </v-sheet>
 
    <v-bottom-navigation v-model="bottomNav" shift change="changePage" fixed>
    <v-btn value="home">
      <span>Home</span>
      <v-icon>mdi-home</v-icon>
    </v-btn>
    <v-btn value="history">
      <span>History</span>
      <v-icon>mdi-database-search</v-icon>
    </v-btn>
    <v-btn value="nearby">
      <span>Nearby</span>
      <v-icon>mdi-map-marker</v-icon>
    </v-btn>
    <v-btn value="analysis">
      <span>Analysis</span>
      <v-icon>mdi-chart-line</v-icon>
    </v-btn>

    </v-bottom-navigation>

   <v-dialog v-model="dialog" max-width="290" >
      <v-card class="mx-auto" max-width="400">
        <v-card-title class="headline">改变选项</v-card-title>
        <v-card-subtitle class="pb-0">可以修改生成数量和模式</v-card-subtitle>
          <v-list-item three-line>
             <v-list-item-content>
               <v-list-item-title>生成数量</v-list-item-title>
              <v-slider v-model="index" :max="4" :tick-labels="nums"  class="mx-4" ticks ></v-slider>
             </v-list-item-content>
          </v-list-item>
          <v-list-item two-line  v-show="nums[index]=='...'">
            <v-text-field label="自定义数量" v-model="customNum"></v-text-field>
          </v-list-item>

          <v-list-item three-line>
             <v-list-item-content>
                <v-list-item-title>模式</v-list-item-title>
                 <v-radio-group v-model="mod">
                  <v-radio label="混合" value="mix"></v-radio>
                  <v-radio label="最少出现(默认)" value="min" ></v-radio>
                  <v-radio label="最多出现" value="max" ></v-radio>
                </v-radio-group>
             </v-list-item-content>
          </v-list-item>
      
        <v-card-actions style="text-align:right;">
          <v-btn text  @click="confirm">确定</v-btn>
        </v-card-actions>
    </v-card>
   </v-dialog>


   <v-dialog v-model="showNumbers" max-width="100%" >
      <v-card class="mx-auto" max-width="100%">
        <v-card-title class="headline">已选号码</v-card-title>
        <v-btn icon absolute top right @click="showNumbers = false"><v-icon>mdi-close</v-icon></v-btn>
        <v-card-text >
             <v-list>
              <v-list-item v-for="(item,idx) in chooseNums" :key="idx">
                <v-list-item-content>
                  <v-row style="margin:0">
                    <v-col style="padding:0;max-width:14%;line-height:44px;"  v-for="n in 6" :key="n">
                    <v-btn  fab dark x-small color="red">
                      <v-icon dark>{{item[n-1]}}</v-icon>
                    </v-btn>
                    </v-col>
                    <v-col style="padding:0;max-width:14%;line-height:44px;" > 
                    <v-btn fab dark x-small color="blue">
                      <v-icon dark>{{item[6]}}</v-icon>
                    </v-btn>
                    </v-col>
                  </v-row>
                </v-list-item-content>
              </v-list-item>
              </v-list>
        </v-card-text>
      </v-card>
   </v-dialog>
  </v-app>
</template>

<script>
import HomePage from './components/HomePage.vue'
import History from './components/History.vue'
import Map from './components/Map.vue'
import Analysis from './components/Analysis.vue'
import Account from './components/Account.vue'

export default {
  name: 'app',
  components: {
    HomePage,
    History,
    Map,
    Analysis,
    Account,
  },
  data(){
    return{
      nums: [5,10,15,20,'...'],
      customNum:0,
      index: 1,
      bottomNav: '',
      dialog:false,
      showNumbers: false,
      mod : 'min',
      lottery:{
          'ssq':{
          'name':'双色球',
          'icon':'mdi-alpha-s-circle'
          },
          'dlt':{
            'name':'大乐透',
            'icon':'mdi-alpha-d-circle-outline'
          }
      },
      lotteryMod: 'min',
      lotteryType: 'ssq',
      currentNum:10,
      chooseNums:[],
    }
  },
  computed:{
    currentTabComponent(){
      if('home' === this.bottomNav){
        return 'HomePage'
      }else if('history' === this.bottomNav){
        return 'History'
      }else if('nearby' === this.bottomNav){
        return 'Map'
      }else if('analysis' === this.bottomNav){
        return 'Analysis'
      }else if('account' === this.bottomNav){
        return 'Account'
      }
      return 'HomePage'
    },
  },
  methods:{
    confirm(){
       this.dialog = false
       this.currentNum = this.index<=3?this.nums[this.index]:this.customNum
       this.lotteryMod=this.mod
     },
     toggleType(){
       if(this.lotteryType === 'ssq' ){
         this.lotteryType = 'dlt'
       }else{
         this.lotteryType = 'ssq'
       }
     },
     setChooseNum(list){
       console.log(list)
       this.chooseNums = list
     },
  }
}
</script>
<style lang="css">
  .v-item-group.v-bottom-navigation .v-btn{
  min-width: 60px!important;
  padding: 8px;
}
</style>
