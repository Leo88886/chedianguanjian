const util = require('../../utils/util.js');
const api = require('../../config/api.js');
const user = require('../../services/user.js');
var fromOpenId = "";
//获取应用实例
const app = getApp()
Page({
  data: {
    floorGoods: [],
    banner: [],
    channel: []
  },

  onShareAppMessage: function () {
    return {
      title: '车车店管家',
      path: '/pages/index/index?formOpenId=' + wx.getStorageSync('openId'), //当前登陆用户openId,
      success: function (res) {}
    }
  },
  onPullDownRefresh() {
    // 增加下拉刷新数据的功能
    var self = this;
    this.getIndexData();
  },
  getIndexData: function () {
    let that = this;
    var data = new Object();

    util.request(api.IndexUrlCategory).then(function (res) {
      if (res.errno === 0) {
        data.floorGoods = res.data.categoryList
        that.setData(data);
      }
    });
    util.request(api.IndexUrlBanner).then(function (res) {
      if (res.errno === 0) {
        data.banner = res.data.banner
        that.setData(data);
      }
    });
    util.request(api.IndexUrlChannel).then(function (res) {
      if (res.errno === 0) {
        data.channel = res.data.channel
        that.setData(data);
      }
    });

  },
  onLoad: function (options) {
    var that = this;
    var formOpenId = decodeURIComponent(options.formOpenId);
    wx.setStorageSync('formOpenId', formOpenId);
    var openId = wx.getStorageSync('openId');
       //保存转发用户关系
    if(openId == "undefined" || openId == null || openId == ""){
      util.request(api.CartList).then(function (res) {});
    }else{
      wx.request({
        url: api.SaveForwardSalerId,
        data: {
          fromOpenId: formOpenId,
          openId: wx.getStorageSync('openId'),
        },
        method: 'POST',
        header: {
          'content-type': 'application/json'
        },
        success: function (res) {
          console.log(res.data);
        }
      });
    }
    that.getIndexData();
    //console.log(options);
    console.log("formOpenId:" + decodeURIComponent(options.formOpenId));
  },
  onReady: function () {
    // 页面渲染完成
  },
  onShow: function () {
    // 页面显示
  },
  onHide: function () {
    // 页面隐藏
  },
  onUnload: function () {
    // 页面关闭
  },
  //解析链接方法
  getQueryString: function (url, name) {
    var reg = new RegExp('(^|&|/?)' + name + '=([^&|/?]*)(&|/?|$)', 'i');
    var r = url.substr(1).match(reg);
    if (r != null) {
      // console.log("r = " + r)
      // console.log("r[2] = " + r[2])
      return r[2];
    }
    return null;
  },
})