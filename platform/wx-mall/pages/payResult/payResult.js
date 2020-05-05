var util = require('../../utils/util.js');
var api = require('../../config/api.js');
const pay = require('../../services/pay.js');

var app = getApp();
Page({
  data: {
    status: false,
    orderId: 0
  },
  onLoad: function (options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.orderId || 24,
      status: options.status
    })
    this.updateSuccess()
  },
  onReady: function () {

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
  
  updateSuccess: function () {
    let that = this
    util.request(api.OrderQuery, {
       orderId: this.data.orderId}).then(function (res) {
         if (res.errno === 0){
           that.setData({
             status: true
           });
         }else{
           that.setData({
             status: false
           });
         }
    })
  },

  payOrder() {
    let orderId = this.data.orderId;
    pay.payOrder(parseInt(orderId)).then(res => {
      wx.redirectTo({
        url: '/pages/payResult/payResult?status=true&orderId=' + orderId
      });
    }).catch(res => {
      wx.redirectTo({
        url: '/pages/payResult/payResult?status=0&orderId=' + orderId
      });
    });
  }
})