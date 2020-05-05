var app = getApp();
var util = require('../../utils/util.js');
var api = require('../../config/api.js');

Page({
  data: {
    orderId: 0,
    actualPrice: 0.00
  },
  onLoad: function(options) {
    // 页面初始化 options为页面跳转所带来的参数
    this.setData({
      orderId: options.orderId,
      actualPrice: options.actualPrice
    })
  },
  onReady: function() {

  },
  onShow: function() {
    // 页面显示

  },
  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },
  //向服务请求支付参数
  requestPayParam() {
    let that = this;
    util.request(api.PayPrepayId, {
      orderId: that.data.orderId,
      payType: 1
    }).then(function(res) {
      if (res.errno === 0) {
        let payParam = res.data;
        console.log(payParam)
        wx.requestPayment({
          'timeStamp': payParam.timeStamp,
          'nonceStr': payParam.nonceStr,
          'package': payParam.package,
          'signType': payParam.signType,
          'paySign': payParam.paySign,
          'success': function(res) {
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=true&orderId=' + that.data.orderId,
            })
          },
          'fail': function(res) {
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=false',
            })
          }
        })
      }
    });
  },
  startPay() {
  
    let that = this;
    wx.showModal({
      title: '提示',
      content: '是否使用钱包余额支付?',
      success: function (res) {
        console.log(res);
        if (res.confirm) {
          util.request(api.PayPrepayId, {
            orderId: that.data.orderId || 15,
            flag: 1
          }).then(function (res) {
            console.log(res.errno);
            if (res.errno === 0) {
              wx.redirectTo({
                url: '/pages/payResult/payResult?flag=1&status=1',
              })
            } else {
              wx.redirectTo({
                url: '/pages/payResult/payResult?flag=1&status=0&orderId=' + that.data.orderId
              });
            }
          });
        } else if (res.cancel) {
          that.requestPayParam();
        }
      }
    });
  },

})