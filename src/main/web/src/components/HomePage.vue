<template>
  <div class="homepage">
    <v-list>
    <v-list-item v-for="(item,idx) in items" :key="idx">
      <v-list-item-content>
        <v-row style="margin:0">
          <v-col style="padding:0;max-width:12.6%;line-height:44px;"  v-for="n in 6" :key="n">
          <v-btn  fab dark x-small color="red">
            <v-icon dark>{{item[n-1]}}</v-icon>
          </v-btn>
          </v-col>
          <v-col style="padding:0;max-width:12.6%;line-height:44px;" > 
          <v-btn fab dark x-small color="blue">
            <v-icon dark>{{item[6]}}</v-icon>
          </v-btn>
          </v-col>
          <v-col style="padding:0;max-width:12.6%;line-height:44px;" >
              <v-btn icon @click="chooseNum(item)">
                <v-icon>{{selected(item)}}</v-icon>
              </v-btn>
          </v-col>
        </v-row>
      </v-list-item-content>
    </v-list-item>
    </v-list>
     <!-- <v-btn absolute  dark  fab right color="pink" class="bottom80">
        <v-icon>mdi-plus</v-icon>
      </v-btn> -->
      <v-btn fixed  dark  fab right color="pink" class="bottom80" v-on:click="search">
        <v-icon>mdi-sync</v-icon>
      </v-btn>
    <div class="footer">
    </div>
     <v-overlay :value="overlay">
      <v-progress-circular indeterminate size="64"></v-progress-circular>
    </v-overlay>
  </div>
</template>

<script>
export default {
  props: ['config','currentNum'],
  data() {
    return{
      items: [],
      custom:false,
      overlay:false,
      selectedList:new Array(),
    }
  },
  mounted(){
    this.search()
  },
  watch:{
    currentNum(n,o){
      console.log(n.o)
      this.search()
    }
  },
  computed:{
    
  },
  methods:{
     search(){
       this.overlay = true
       this.$axios
        .get('/lottery/'+this.config.ctype+'/generate/'+this.config.mod+'/'+this.currentNum)
        .then(response => {
          this.items = response.data
          this.overlay =false
        })
        .catch(e=> { // 请求失败处理
          window.console.log(e)
          this.overlay =false
        });
     },
     selected(item){
       if(!this.selectedList || this.selectedList.length==0){
         return 'mdi-heart-outline'
       }
      let exists = this.selectedList.indexOf(item) != -1
      if(exists){
        return 'mdi-heart'
      }else{
        return 'mdi-heart-outline'
      }
    },
    chooseNum(item){
      if(this.selectedList.indexOf(item) != -1){
        this.selectedList = this.selectedList.filter(i=>i!=item)
      }else{
        this.selectedList.push(item)
      }
      this.$emit("chooseNum",this.selectedList)
    }
  }
}





</script>

<style scoped>
.container{
  min-width: 320px;
  padding: 8px;
}
.v-list-item{
  padding: 0 8px;
}

  .homepage .checkbox{
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
    box-shadow: 0px 3px 5px -1px rgba(0, 0, 0, 0.2), 0px 6px 10px 0px rgba(0, 0, 0, 0.14), 0px 1px 18px 0px rgba(0, 0, 0, 0.12);
    will-change: box-shadow;
    color: #FFFFFF;
    background-color: #009688;
    border-color: #009688;
    border-radius: 10%;
    margin: 0 10px;
  }
  .homepage .checkbox:hover{
    background-color: #3DAFA4;
    border-color: #3DAFA4;

  }

   .homepage .checkbox.selected{
    background-color: #6EC3BB;
    border-color: #6EC3BB;
   }
   .bottom80{
     bottom: 80px;
   }
   .bottom100{
     bottom: 160px;
   }

   /* .v-btn{
     margin: 10px 0 ;
   } */
  .v-input--radio-group__input{
    display: inline;
  }
  .v-radio{
    display: inline;
    padding: 0 10px;
  } 

  .footer{
    height: 56px;
  }
  .choosed{
    background-color: #009688!important;
    color: #fff!important;
    
  }

  .v-btn--fab.v-btn--contained{
    box-shadow: none!important;
  }

</style>
