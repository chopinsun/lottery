<template>
  <div class="Analysis">
    <v-card class="redBall">
      <v-toolbar flat dense>
        <v-toolbar-title>redballs</v-toolbar-title>
      </v-toolbar>
       <ve-line :data="redBalls" :theme="theme_red" :legend-visible="false"></ve-line>
    </v-card>

     <v-card  class="blueBall">
       <v-toolbar flat dense>
        <v-toolbar-title>blueballs</v-toolbar-title>
      </v-toolbar>
       <ve-line :data="blueBalls" :theme="theme_blue" :legend-visible="false"></ve-line>
    </v-card >
  </div>
</template>

<script>
export default {
  data() {
    return{
      redBalls: {
        columns: ['ball','cnt'],
        rows:[]
      },
      blueBalls:{
        columns: ['ball','cnt'],
        rows:[]
      },
      theme_red:{},
      theme_blue:{}
    }
  },
  mounted(){
    this.theme_red = {
      "color": [
          "#c12e34",
          "#e6b600",
          "#0098d9",
          "#2b821d",
          "#005eaa",
          "#339ca8",
          "#cda819",
          "#32a487"
      ]
    }

    this.theme_blue = {
      "color": [
          "#0098d9",
          "#c12e34",
          "#e6b600",
          "#2b821d",
          "#005eaa",
          "#339ca8",
          "#cda819",
          "#32a487"
      ]
    }
    this.search()
  },
  methods:{
     search(){
       this.$axios
        .get('/lottery/count')
        .then(response => {
          console.log(response)
          this.redBalls.rows = response.data.redBall
          this.blueBalls.rows = response.data.blueBall
        })
        .catch(e=> { // 请求失败处理
          window.console.log(e);
        });
     },

  }
}

</script>

<style scoped>
  .homepage ul li span{
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
