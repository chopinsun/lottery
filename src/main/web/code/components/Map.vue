<template>
  <div class="m-map" :style="resizeHeight">
    <div class="search" v-if="placeSearch" v-show="false">
      <input type="text" placeholder="请输入关键字" v-model="searchKey">
      <button type="button" @click="handleSearch">搜索</button>
      <div id="js-result" v-show="searchKey" class="result"></div>
    </div> 
    <div id="js-container" class="map">正在加载数据 ...</div>
  </div>
</template>

<script>
import remoteLoad from '@/utils/remoteLoad.js'
import { MapKey, MapCityName } from '@/plugins/map'
export default {
  data () {
    return {
      searchKey: '福彩',
      placeSearch: null,
      dragStatus: false,
      AMapUI: null,
      AMap: null,
      markers:[],
      mapConfig:{
        zoom: 14,
        center: [116.387636, 40.000248],
         viewMode:'3D',
         resizeEnable: true,
      },
      map: null
    }
  },
  computed: {
    resizeHeight(){
      let winHeight = 650;
      if (window.innerHeight)
      winHeight = window.innerHeight
      else if ((document.body) && (document.body.clientHeight))
      winHeight = document.body.clientHeight
      winHeight -= 56
      return "height:"+winHeight+"px";
    }
  },
  watch: {

  },
  methods: {
    // 搜索
    handleSearch () {
      if (this.searchKey) {
        this.placeSearch.searchNearBy(this.searchKey,this.mapConfig.center,1500,(status,result)=>{})
      }
    },
    // 实例化地图
    initMap () {
      // 加载PositionPicker，loadUI的路径参数为模块名中 'ui/' 之后的部分
      let AMapUI = this.AMapUI = window.AMapUI
      let AMap = this.AMap = window.AMap
      AMapUI.loadUI(['misc/PoiPicker'], PositionPicker => {
        let map = this.map = new AMap.Map('js-container', this.mapConfig)
        //添加点
        for(marker in this.markers){
          map.add(new AMap.Marker(marker))
        }
        // 加载地图搜索插件
        AMap.service('AMap.PlaceSearch', () => {
        this.placeSearch = new AMap.PlaceSearch({
            pageSize: 100,
            pageIndex: 1,
            citylimit: true,
            city: '010',
            map: map,
            panel: 'js-result'
          })
        })
        // 启用工具条
        AMap.plugin(['AMap.ToolBar'], function () {
          map.addControl(new AMap.ToolBar({
            position: 'RB'
          }))
        })
        // 创建地图拖拽
        let positionPicker = new PositionPicker({
          mode: 'dragMap', // 设定为拖拽地图模式，可选'dragMap'、'dragMarker'，默认为'dragMap'
          map: map // 依赖地图对象
        })
        // 拖拽完成发送自定义 drag 事件
         positionPicker.on('poiPicked', positionResult => {
          // 过滤掉初始化地图后的第一次默认拖放
          if (!this.dragStatus) {
            this.dragStatus = true
          } else {
            this.$emit('drag', positionResult)
          }
        })
        //定位当前位置
        this.gps()
        // 启动拖放
         
      })
     
    },
    addMarker(){
        for(let i in this.markers){
          console.log(this.markers)
          this.map.add(this.markers[i])
        }
    },
    gps(){
      let $this=this
      AMap.plugin('AMap.Geolocation', function() {
      let geolocation = new AMap.Geolocation({
        // 是否使用高精度定位，默认：true
        enableHighAccuracy: true,
        // 设置定位超时时间，默认：无穷大
        timeout: 10000,
        // 定位按钮的停靠位置的偏移量，默认：Pixel(10, 20)
        buttonOffset: new AMap.Pixel(10, 20),
        //  定位成功后调整地图视野范围使定位位置及精度范围视野内可见，默认：false
        zoomToAccuracy: true,     
        //  定位按钮的排放位置,  RB表示右下
        buttonPosition: 'LB'
      })
      $this.map.addControl(geolocation);
      geolocation.getCurrentPosition((status,result)=>{
         if(status=='complete'){
                onComplete(result)
            }else{
                onError(result)
            }
      })
      AMap.event.addListener(geolocation, 'complete', onComplete)
      AMap.event.addListener(geolocation, 'error', onError)

      function onComplete (data) {
        // data是具体的定位信息
        $this.mapConfig.center = [data.position.lng,data.position.lat]
        $this.handleSearch()
      }

      function onError (data) {
        // 定位出错
      }
    })
    }
  },
  async created () {
    // 已载入高德地图API，则直接初始化地图
    if (window.AMap && window.AMapUI) {
      this.initMap()
    // 未载入高德地图API，则先载入API再初始化
    } else {
      await remoteLoad(`https://webapi.amap.com/maps?v=1.4.15&key=${MapKey}`)
      await remoteLoad('https://webapi.amap.com/ui/1.0/main.js')
      this.initMap()
    }
  }
}
</script>

<style lang="css">
.m-map{ height: 650px; position: relative; }
.m-map .map{ width: 100%; height: 100%; }
.m-map .search{ position: absolute; top: 10px; left: 10px; width: 285px; z-index: 1; }
.m-map .search input{ width: 180px; border: 1px solid #ccc; line-height: 20px; padding: 5px; outline: none; }
.m-map .search button{ line-height: 26px; background: #fff; border: 1px solid #ccc; width: 50px; text-align: center; }
.m-map .result{ max-height: 300px; overflow: auto; margin-top: 10px; }
</style>