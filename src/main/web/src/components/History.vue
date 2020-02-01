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
        <template v-slot:item.type1="{ item }">
          <div v-html="item.type1"></div>
        </template>
         <template v-slot:item.type2="{ item }">
          <div v-html="item.type2"></div>
        </template>
         <template v-slot:item.content="{ item }">
           <v-btn class="ma-2" text icon color="red lighten-2">
              <v-icon  @click="snackbar = true">mdi-dots-horizontal-circle-outline</v-icon>
            </v-btn>
            <v-snackbar  v-model="snackbar" top color="teal lighten-3"> {{item.content}} </v-snackbar>
        </template>
     </v-data-table>
     </v-card>
  </div>
</template>

<script>
export default {
  data() {
    return{
      headers: [
        {text: '期号',  value: 'code'},
        {text: '开奖日期',  value: 'lotteryDate'},
        { text: '中奖号码', value: 'numbers', },
        { text: '奖池奖金', value: 'poolmoney',},
        { text: '一等奖', value: 'type1' },
        { text: '二等奖', value: 'type2' },
        { text: '详情', value:'content'},
      ],
      keywards:'',
      items:[],
      pageSize: 100,
      snackbar: false,
    }
  },
  mounted(){
    this.search()
  },
  methods:{
     search(){
       let $this = this
       this.$axios
        .get('/lottery/history?n='+this.pageSize)
        .then(response => {
          console.log(response.data)
          response.data.forEach(e => {
            let i = e
            i.numbers = 
            `<span class="num rednum">`+e.r1+`</span>`+
            `<span class="num rednum">`+e.r2+`</span>`+
            `<span class="num rednum">`+e.r3+`</span>`+
            `<span class="num rednum">`+e.r4+`</span>`+
            `<span class="num rednum">`+e.r5+`</span>`+
            `<span class="num rednum">`+e.r6+`</span>`+
            `<span class="num bluenum">`+e.b1+`</span>`
            i.type1 = '<span class="yellownum">'+ e.type1Num + "</span> * "+ e.type1Money.toLocaleString('en-US')
            i.type2 = '<span class="yellownum">'+ e.type2Num + "</span> * "+ e.type2Money.toLocaleString('en-US')
            i.poolmoney = e.poolmoney.toLocaleString('en-US')
            $this.items.push(i)
          });
        })
        .catch(e=> { // 请求失败处理
          window.console.log(e);
        });
     },

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
</style>
