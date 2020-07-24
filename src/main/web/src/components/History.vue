<template>
  <div class="history">
     <v-card>
    <v-card-title>
      历史数据
      <v-spacer></v-spacer>
      <!-- <v-text-field v-model="keywards" append-icon="search" label="Search"  single-line hide-details ></v-text-field> -->
    </v-card-title>
     <v-data-table :headers="headers"  :items="items" item-key="id" calculate-widths disable-sort fixed-header :items-per-page="pageSize">
        <template v-slot:item.numbers="{ item }">
          <div v-html="item.numbers"></div>
        </template>
          <template  v-slot:item.action="{ item }">
           <v-btn class="ma-2" text icon color="red lighten-2">
              <v-icon  @click="show(item)">mdi-dots-horizontal-circle-outline</v-icon>
          </v-btn>
          </template>
     </v-data-table>
     </v-card>

      <v-dialog v-model="dialog" max-width="290">
        <v-card>
          <v-card-title class="headline">中奖详情</v-card-title>
          <v-card-text>
            <template v-for="dt in shownItem.details" >
              <div v-bind:key="dt.level">
                <span class="level">{{dt.levelName}}</span>
                <span class="leveln">{{dt.num}}</span>  *  <span class="levelm">{{dt.money.toLocaleString('en-US')}}</span>
              </div>
          </template>
            <div class="content">
              <div class="content-title">一等奖分布</div>
              <div class="content-text" v-html="contentToText">
              </div>
            </div>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn  color="green darken-1" text  @click="dialog = false" >
              关闭
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
    <v-overlay :value="overlay">
      <v-progress-circular indeterminate size="64"></v-progress-circular>
    </v-overlay>
  </div>
</template>

<script>
export default {
  props: ['lotteryMod','lotteryType','currentNum'],
  data() {
    return{
      headers: [
        {text: '期号',  value: 'code'},
        {text: '开奖日期',  value: 'lotteryDate'},
        { text: '中奖号码', value: 'numbers', },
        { text: '奖池奖金', value: 'poolmoney',},
        {text:'详情', value:'action'},
      ],
      keywards:'',
      items:[],
      pageSize: 100,
      snackbar: false,
      dialog:false,
      shownItem: {},
      overlay: false
    }
  },
  computed:{
    contentToText(){
      if(this.shownItem && this.shownItem.content){
        return this.shownItem.content.split(',').map(x=>'<div>'+x+'</div>').reduce((t1,t2)=>t1+t2)
      }
      return ''
    }
  },
  watch:{
    lotteryType(){
      this.search()
    }
  },
  mounted(){
    this.search()
  },
  methods:{
     search(){
       this.items = []
       this.overlay =true
       let $this = this
       this.$axios
        .get('/lottery/'+$this.lotteryType+'/history/'+$this.pageSize)
        .then(response => {
          response.data.forEach(e => {
            $this.overlay = false
            let i = e
            i.numbers = 
            `<span class="num rednum">`+e.r1+`</span>`+
            `<span class="num rednum">`+e.r2+`</span>`+
            `<span class="num rednum">`+e.r3+`</span>`+
            `<span class="num rednum">`+e.r4+`</span>`+
            `<span class="num rednum">`+e.r5+`</span>`
            if(e.r6){
               i.numbers+=`<span class="num rednum">`+e.r6+`</span>`
            }
            i.numbers+=`<span class="num bluenum">`+e.b1+`</span>`
            if(e.b2){
              i.numbers+=`<span class="num bluenum">`+e.b2+`</span>`
            }
            i.poolmoney = e.poolmoney!=null?e.poolmoney.toLocaleString('en-US'):''
            $this.items.push(i)
          });
        })
        .catch(e=> { // 请求失败处理
        $this.overlay = false
          console.log(e)
        });
     },
     show(item){
       this.shownItem = item
       this.dialog =true
     }

  }
}

</script>

<style lang="css">
.num{
  padding: 0 5px;
}
 .rednum{
  color: red!important;
}
 .bluenum{
  color: blue;
}
.yellownum{
  color: yellowgreen;
}
.v-snack__wrapper{
  box-shadow:none!important;
}
.level{
  padding: 0 10px;
  color: #000;
}
.leveln{
  padding: 0 5px;
  color: red;
}
.levelm{
  padding: 0 5px;
}
.content{
  padding: 10px 0;
}
.content-title{
    font-size: 16px;
    color: #000;
    padding: 10px 0;
  }
  .content-text div{
    display: inline-block;
    width: 45%;
  }
</style>
