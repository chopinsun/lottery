<template>
  <div class="homepage">
    
    <v-container>
      <v-app-bar color="white" >
      <!-- <v-btn icon> -->
      <v-icon large>mdi-home</v-icon>
      <!-- </v-btn> -->
      <v-toolbar-title>Lottery</v-toolbar-title>

      <v-spacer></v-spacer>

      <v-btn icon large @click="overlay =!overlay">
        <v-icon>mdi-settings</v-icon>
      </v-btn>
      <!-- <v-btn icon>
        <v-icon>mdi-magnify</v-icon>
      </v-btn> -->
<!-- 
      <v-menu left bottom >
        <template v-slot:activator="{ on }">
          <v-btn icon v-on="on">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list>
          <v-list-item
            v-for="n in 5"
            :key="n"
            @click="() => {}"
          >
            <v-list-item-title>Option {{ n }}</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
-->      
    </v-app-bar>
      
      <v-divider></v-divider>
    <v-list>
    <v-list-item v-for="(item,idx) in items" :key="idx">
      <v-list-item-content>
        <v-list-item-title>
          <v-btn class="mx-1" fab dark x-small color="red" v-for="n in 6" :key="n">
            <v-icon dark>{{item[n-1]}}</v-icon>
          </v-btn>
          <v-btn class="mx-1" fab dark x-small color="blue">
            <v-icon dark>{{item[6]}}</v-icon>
          </v-btn>
        </v-list-item-title>
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
    </v-container>
 

    <v-overlay absolute  :value="overlay">
      <v-card class="mx-auto" max-width="500" outlined  v-show="overlay" style="z-index:8" light>
        <div style="padding: 0 10px;">
      <v-row>
        <div style="width:80px; text-align:center;line-height:80px;">
          <span class="form-title"> Number </span>
        </div>
        <v-col>
        <v-btn-toggle v-model="selected" shaped  mandatory >
          <v-btn v-for="(item,i) in nums" :key="i" :value="i" active-class="choosed" outlined>{{item}}</v-btn>
        </v-btn-toggle>
        </v-col>
      </v-row>
       <v-row v-if="selected === 4" >
        <v-col >
          <v-text-field label="Customize" outlined v-model="customNum"></v-text-field>
        </v-col> 
      </v-row>
      <v-row>
         <div style="width:80px; text-align:center;line-height:80px;">
          <span class="form-title"> Type </span>
        </div>
        <v-col xs="10" >
           <v-radio-group v-model="type">
              <v-radio label="mix" value="mix"></v-radio>
              <v-radio label="min" value="min" ></v-radio>
              <v-radio label="max" value="max" ></v-radio>
            </v-radio-group>
        </v-col>
      </v-row>
     
        </div>
      <v-card-actions style="text-align:center;">
        <v-btn min-width="100%" outlined color="success"  @click="confirm">Confirm</v-btn>
      </v-card-actions>
    </v-card>
    </v-overlay>
    
  </div>
</template>

<script>
export default {
  data() {
    return{
      nums: [5,10,15,20,'...'],
      type: 'mix',
      searching: false,
      items: [],
      custom:false,
      selected: 1,
      customNum:0,
      overlay:false,
    }
  },
  computed:{
    num(){
      if(this.nums[this.selected]!=='...'){
          return this.nums[this.selected]
        }else{
           return this.customNum
        }
     
    },
  },
  mounted(){
    this.search()
  },
  methods:{
     search(){
       this.$axios
        .get('/lottery/generate/'+this.type+'?n='+this.num)
        .then(response => {
          console.log(response.data)
          this.items = response.data
        })
        .catch(e=> { // 请求失败处理
          window.console.log(e);
        });
     },
     select(idx){
        this.selected = idx
     },
     confirm(){
       this.overlay = false
      //  this.search();
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
