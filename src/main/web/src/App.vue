<template>
  <v-app>
    <v-app-bar absolute color="teal lighten-3" dark shrink-on-scroll dense  scroll-target="#scrolling-techniques" >
      <v-app-bar-nav-icon></v-app-bar-nav-icon>

      <v-toolbar-title>{{lotteryConfig.lottery[lotteryType].name}}</v-toolbar-title>

      <v-spacer></v-spacer>

      
      <v-btn icon @click="showNumbers=true">
         <v-badge overlap left color="error"  :content="chooseNums.length" offset-x="8px" offset-y="9px" :value="chooseNums.length">
          <v-icon>mdi-heart-multiple</v-icon>
          </v-badge>
      </v-btn>

      <v-btn icon @click="toggleType">
        <v-icon>{{lotteryConfig.lottery[lotteryType].icon}}</v-icon>
      </v-btn>
      <v-btn icon @click.stop="showDialog" v-show="currentTabComponent==='HomePage' || currentTabComponent ==='Analysis' || currentTabComponent ==='Map'">
        <v-icon>mdi-cog</v-icon>
      </v-btn>
       <v-btn icon @click="bottomNav = 'account'">
        <v-icon>mdi-account</v-icon>
      </v-btn>
     
      <v-btn icon >
        <v-icon>mdi-dots-vertical</v-icon>
      </v-btn>
    </v-app-bar>
s
    <v-sheet   id="scrolling-techniques" class="overflow-y-auto" :style="{height:screenHeight+'px','margin-top':'-24px'}">
      <v-container style="margin-top:96px;" :style="{height:screenHeight+'px'}">
        <component v-bind:is="currentTabComponent" :lotteryMod ="lotteryMod" :lotteryType="lotteryType" :lotteryNum="lotteryNum"  :mapSearchRange="mapSearchRange" :chartmod="chartmod" v-on:chooseNum="setChooseNum"></component>
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

   <v-dialog v-model="lotteryConfig.dialog" max-width="290" >
      <v-card class="mx-auto" max-width="400">
        <v-card-title class="headline">选号设置</v-card-title>
        <v-card-subtitle class="pb-0">可以修改生成数量和模式</v-card-subtitle>
          <v-list-item three-line>
             <v-list-item-content>
               <v-list-item-title>生成数量</v-list-item-title>
              <v-slider v-model="lotteryConfig.index" :max="4" :tick-labels="lotteryConfig.nums"  class="mx-4" ticks ></v-slider>
             </v-list-item-content>
          </v-list-item>
          <v-list-item two-line  v-show="lotteryConfig.nums[lotteryConfig.index]=='...'">
            <v-text-field label="自定义数量" v-model="lotteryConfig.customNum"></v-text-field>
          </v-list-item>

          <v-list-item three-line>
             <v-list-item-content>
                <v-list-item-title>模式</v-list-item-title>
                 <v-radio-group v-model="lotteryConfig.mod">
                  <v-radio label="混合" value="mix"></v-radio>
                  <v-radio label="最少出现(默认)" value="min" ></v-radio>
                  <v-radio label="最多出现" value="max" ></v-radio>
                </v-radio-group>
             </v-list-item-content>
          </v-list-item>
      
        <v-card-actions style="text-align:right;">
          <v-btn text  @click="confirmLottery">确定</v-btn>
        </v-card-actions>
    </v-card>
   </v-dialog>



   <v-dialog v-model="chartConfig.dialog" max-width="290" >
      <v-card class="mx-auto" max-width="400">
        <v-card-title class="headline">分析设置</v-card-title>
        <v-card-text>
           <v-radio-group v-model="chartConfig.chartmod">
              <v-radio label="号码出现次数分析" value="cnt"></v-radio>
              <v-radio label="冷热号" value="ch" ></v-radio>
              <v-radio label="..." value="..." ></v-radio>
            </v-radio-group>
        </v-card-text>
        <v-card-actions style="text-align:right;">
          <v-btn text  @click="confirmChart">确定</v-btn>
        </v-card-actions>
    </v-card>
   </v-dialog>

      <v-dialog v-model="mapconfig.dialog" max-width="290" >
      <v-card class="mx-auto" max-width="400">
        <v-card-title class="headline">地图搜索设置</v-card-title>
        <v-card-text>
           <v-text-field  v-model="mapconfig.mapSearchRange" label="搜索范围" ></v-text-field>
        </v-card-text>
        <v-card-actions style="text-align:right;">
          <v-btn text  @click="confirmMap">确定</v-btn>
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
        <v-card-actions>
          <v-btn text @click="clearNumbers"> 清空</v-btn>
        </v-card-actions>
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
      screenHeight: document.documentElement.clientHeight,
      bottomNav: '',
      lotteryMod: 'min',
      lotteryType: 'ssq',
      lotteryNum:10,
      chooseNums:[],
      mapSearchRange: 0,
      chartmod:'',
      showNumbers: false,
      lotteryConfig:{
        dialog: false,
        nums: [5,10,15,20,'...'],
        customNum:0,
        index: 1,
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
      },
      chartConfig:{
        dialog: false,
        chartmod:'cnt',
      },
      mapconfig:{
        dialog: false,
        mapSearchRange:1500
      },
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
    resizeHeight(){
      console.log( window.screen.height)
      return {
        height: window.screen.height
      }
    }
  },
  methods:{
    confirmLottery(){
       this.lotteryConfig.dialog = false
       this.lotteryNum = this.lotteryConfig.index<=3?this.lotteryConfig.nums[this.lotteryConfig.index]:this.lotteryConfig.customNum
       this.lotteryMod=this.lotteryConfig.mod
     },
     confirmChart(){
       this.chartConfig.dialog = false
       this.chartmod = this.chartConfig.chartmod
     },
     confirmMap(){
       this.mapconfig.dialog = false
       this.mapSearchRange = this.mapconfig.mapSearchRange
     },
     toggleType(){
       if(this.lotteryType === 'ssq' ){
         this.lotteryType = 'dlt'
       }else{
         this.lotteryType = 'ssq'
       }
     },
     setChooseNum(list){
       this.chooseNums = list
     },
     showDialog(){
       if(this.currentTabComponent === 'HomePage'){
         this.lotteryConfig.dialog=true
       }else if(this.currentTabComponent === 'Analysis'){
         this.chartConfig.dialog = true
       }else if(this.currentTabComponent === 'Map'){
         this.mapconfig.dialog = true
       }
     },
     clearNumbers(){
       this.chooseNums = []
     }
  }
}
</script>
<style lang="css">
  .v-item-group.v-bottom-navigation .v-btn{
  min-width: 60px!important;
  padding: 8px;
}

    .Chart {
      background: #212733;
      border-radius: 15px;
      box-shadow: 0px 2px 15px rgba(25, 25, 25, 0.27);
      margin:  25px 0;
    }

    .Chart h2 {
      margin-top: 0;
      padding: 15px 0;
      color:  rgba(255, 0,0, 0.5);
      border-bottom: 1px solid #323d54;
    }
</style>
