const util = require('../../../utils/util.js');
const api = require('../../../config/api.js');

//获取应用实例
const app = getApp()
Page({
  data: {
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
    navUrl: '',
    code: ''
  },

  onLoad: function(options) {
    let that = this;
    if (wx.getStorageSync("navUrl")) {
      that.setData({
        navUrl: wx.getStorageSync("navUrl")
      })
    } else {
      that.setData({
        navUrl: '/pages/index/index'
      })
    }

    wx.login({
      success: function(res) {
        if (res.code) {
          that.setData({
            code: res.code
          })
        }
      }
    });
  },

  bindGetUserInfo: function(e) {
    let that = this;
    //登录远程服务器
    console.log(that.data.code)
    if (that.data.code) {
      util.request(api.AuthLoginByWeixin, {
        code: that.data.code,
        userInfo: e.detail
      }, 'POST', 'application/json').then(res => {
        console.log(res.errno === 0)
        if (res.errno === 0) {
          //存储用户信息
          wx.setStorageSync('userInfo', res.data.userInfo);
          wx.setStorageSync('token', res.data.token);
          wx.setStorageSync('userId', res.data.userId);
          wx.setStorageSync('openId', res.data.openId);
          var formOpenId = wx.getStorageSync('formOpenId');
          if (formOpenId != "undefined" && formOpenId != null && formOpenId !="") {   //通过转发进入的页面
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
          var page = getCurrentPages().pop();
          console.log('page', page)
          if (page == undefined || page == null) return;
          page.onLoad(e);
          console.log(res.data.openId)
        } else {
          // util.showErrorToast(res.errmsg)
          wx.showModal({
            title: '提示',
            content: res.errmsg,
            showCancel: false
          });
        }
      });
    }
    if (that.data.navUrl && that.data.navUrl == '/pages/index/index') {
      wx.switchTab({
        url: that.data.navUrl,
      })
    } else if (that.data.navUrl) {
      wx.redirectTo({
        url: that.data.navUrl,
      })
    }
  },
  onReady: function() {
    // 页面渲染完成
  },
  onShow: function() {
    // 页面显示
  },
  onHide: function() {
    // 页面隐藏
  },
  onUnload: function() {
    // 页面关闭
  }
})