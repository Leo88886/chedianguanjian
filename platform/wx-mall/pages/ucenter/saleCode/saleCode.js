var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const user = require('../../../services/user.js');
Page({
  data: {
    salerId: '',
    text: '',
    saleMon: '',
    saleAll: ''
  },
  onLoad: function(options) {
    var that = this;
    wx.request({
      url: api.GetRelation,
      data: {
        openId: wx.getStorageSync('openId'), //当前登陆用户openId
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function(res) {
        that.setData({
          salerId: res.data.salerId,
          saleMon: res.data.saleMon,
          saleAll: res.data.saleAll
        })
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
  SaveRelation: function(e) {
    var that = this;
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
    wx.request({
      url: api.SaveRelation,
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