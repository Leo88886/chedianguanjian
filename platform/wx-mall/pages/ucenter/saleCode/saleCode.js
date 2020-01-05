var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const user = require('../../../services/user.js');
Page({
  data: {
    salerId: '',
    flag: false,
    flag2: false,
    flag3: false,
    text: '',
    saleMon:'',
    saleAll:''
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
        console.log(res.data)
        if (0 == res.data.saveSaleId) { //未绑定
          that.setData({
            flag: true
          })
        }
        if (null != res.data.user && res.data.user != '') { //已绑定
          that.setData({   // 不显示保存按钮
            flag2: true, 
            text: res.data.user
          })
        } 
        if (null != res.data.saler && res.data.saler != '') { // 销售页面
          that.setData({
            flag3: true, 
            saleMon: res.data.saleMon,
            saleAll: res.data.saleAll,
            text: res.data.saler
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