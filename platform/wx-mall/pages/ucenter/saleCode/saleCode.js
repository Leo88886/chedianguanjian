var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const user = require('../../../services/user.js');
Page({
  data: {
    salerId: '',
    flag: false,
    flag2: false,
    text: ''
  },
  onLoad: function(options) {
    var that = this;
    wx.request({
      url: api.IsSaveSalerId,
      data: {
        openId: wx.getStorageSync('openId'), //当前登陆用户openId
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function(res) {
        if (null != res.data && res.data != '') { //已绑定
          console.log(res.data);
          that.setData({
            flag2: true, // 不显示保存按钮
            text: res.data
          })
        } else {
          that.setData({
            flag: true,
          })
        }
      }
    });
  },
  //获取用户输入的salerId
  salerId: function(e) {
    var that = this;
    this.setData({
      salerId: e.detail.value
    })
  },
  saveSalerId: function(e) {
    var that = this;
    console.log(api.SaveSalerId);
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
    wx.setStorageSync
    wx.request({
      url: api.SaveSalerId,
      data: {
        salerId: that.data.salerId,
        openId: openId
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function(res) {
        if (res.data == '1') {
          wx.showToast({
            title: '保存成功',
            icon: 'success',
            duration: 2000
          })
          wx.navigateBack({
            delta: 1
          })
        } else {
          wx.showLoading
          wx.showLoading({
            title: '暂无该识别码',
          })
          setTimeout(function() {
            wx.hideLoading()
          }, 2000)
        }
      }
    });

  },
})