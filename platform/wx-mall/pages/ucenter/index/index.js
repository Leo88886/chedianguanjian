var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var user = require('../../../services/user.js');
var app = getApp();
Page({
  data: {
    balance: 0,
    userInfo: {},
    hasMobile: '',
    user: '',
    referrer: '',
    chargeModalShow: false, //充值（提现）弹框
    chargePrice: 0, //充值或提现金额
    action: 0, //0 充值弹框 1 提现弹框
    chargeSuccessModalShow: false, //充值（提现）成功弹框
    chargeFailModalShow: false, //充值（提现）失败弹框
    balanceOrderId: '', //充值订单id
    buyBalance: 0, //最近一次充值金额
    buyResult: false,
    salerId: '暂无',
    mySalerId: '暂无',
    couponNum: '0',
    color: 'black'
  },
  onLoad: function (options) {

  },
  onReady: function () {

  },
  onShareAppMessage: function () {
    return {
      title: '车车店管家',
      path: '/pages/index/index?formOpenId=' + wx.getStorageSync('openId'), //当前登陆用户openId,
      success: function (res) { }
    }
  },
  onShow: function () {
    let userInfo = wx.getStorageSync('userInfo');
    let token = wx.getStorageSync('token');

    // 页面显示
    if (userInfo && token) {
      app.globalData.userInfo = userInfo;
      app.globalData.token = token;
    }
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
    var that = this;
    that.setData({
      userInfo: app.globalData.userInfo,
    });

    util.request(api.QueryCouponNum, {}).then(function (res) {
      if (res != '0') {
        that.setData({
          couponNum: res,
          color: 'red'
        });
      }
    });

    //查推荐人
    wx.request({
      url: api.QueryReferrer,
      data: {
        openId: openId
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        console.log(res.data)
        that.setData({
          referrer: res.data.referrer
        });
      }
    });

    //查询余额
    wx.request({
      url: api.QueryBanlance,
      data: {
        openId: openId
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        that.setData({
          balance: res.data.balance
        });
      }
    });

    wx.request({
      url: api.GetRelation,
      data: {
        openId: wx.getStorageSync('openId'), //当前登陆用户openId
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        that.setData({
          salerId: res.data.salerId,
          mySalerId: res.data.mySalerId
        })
      }
    });

  },
  onHide: function () {
    // 页面隐藏

  },
  onUnload: function () {
    // 页面关闭
  },
  bindGetUserInfo(e) {
    let userInfo = wx.getStorageSync('userInfo');
    let token = wx.getStorageSync('token');
    if (userInfo && token) {
      return;
    }
    if (e.detail.userInfo) {
      //用户按了允许授权按钮
      user.loginByWeixin(e.detail).then(res => {
        this.setData({
          userInfo: res.data.userInfo
        });
        app.globalData.userInfo = res.data.userInfo;
        app.globalData.token = res.data.token;
      }).catch((err) => {
        console.log(err)
      });
    } else {
      //用户按了拒绝按钮
      wx.showModal({
        title: '警告通知',
        content: '您点击了拒绝授权,将无法正常显示个人信息,点击确定重新获取授权。',
        success: function (res) {
          if (res.confirm) {
            wx.openSetting({
              success: (res) => {
                if (res.authSetting["scope.userInfo"]) { ////如果用户重新同意了授权登录
                  user.loginByWeixin(e.detail).then(res => {
                    this.setData({
                      userInfo: res.data.userInfo
                    });
                    app.globalData.userInfo = res.data.userInfo;
                    app.globalData.token = res.data.token;
                  }).catch((err) => {
                    console.log(err)
                  });
                }
              }
            })
          }
        }
      });
    }
  },
  exitLogin: function () {
    wx.showModal({
      title: '',
      confirmColor: '#b4282d',
      content: '退出登录？',
      success: function (res) {
        if (res.confirm) {
          wx.removeStorageSync('token');
          wx.removeStorageSync('userInfo');
          wx.switchTab({
            url: '/pages/index/index'
          });
        }
      }
    })
  },

  //点击遮罩
  hideChargeModal: function () {
    this.setData({
      chargeModalShow: false,
      chargeSuccessModalShow: false,
      chargeFailModalShow: false,
    });
    wx.showTabBar({
      animation: false,
    })
  },
  preventTouchMove: function () {

  },

  //提现临时提示框
  caocaocao: function (e) {
    wx.showModal({
      title: '提示',
      content: '该功能35日后开放,您可以使用余额进行购买，或者耐心等待提现功能，抱歉~~',
      success: function (res) {

      }
    });
  },

  //充值（提现）弹出框
  showChargeModal: function (e) {
    this.setData({
      action: e.currentTarget.dataset.action,
      chargeModalShow: !this.data.chargeModalShow
    })
  },

  //充值（提现）输入框事件
  bindChargeInput: function (e) {
    this.setData({
      chargePrice: e.detail.value,
    })
  },

  //充值\提现
  charge: function () {
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
    var reg = /^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
    if (parseFloat(this.data.chargePrice) && this.data.chargePrice > 0 &&
      openId != null && openId != '' && reg.test(this.data.chargePrice)) {
      // action 0表示当前操作是充值操作，1表示当前操作是提现操作
      if (this.data.action == 0) { //充值操作
        //请调用充值接口，返回成功时候调用这个方法显示成功界面
        //this.charge_success();
        const balance = this.data.chargePrice
        const that = this;
        wx.request({
          url: api.BuyBanlance,
          data: {
            openId: openId,
            balance: balance
          },
          method: 'POST',
          header: {
            'content-type': 'application/json'
          },
          success: function (res) {
            var payParam = res.data;
            var orderId = payParam.data.orderId;
            var balance = payParam.data.balance;
            wx.requestPayment({
              'timeStamp': payParam.data.timeStamp,
              'nonceStr': payParam.data.nonceStr,
              'package': payParam.data.package,
              'signType': payParam.data.signType,
              'paySign': payParam.data.paySign,
              'success': function (res) {
                that.data.balanceOrderId = orderId
                that.data.buyBalance = balance

                //维护流水
                wx.request({
                  url: api.BuyBanlanceResult,
                  data: {
                    openId: openId,
                    balance: balance,
                    orderId: orderId
                  },
                  method: 'POST',
                  header: {
                    'content-type': 'application/json'
                  },
                  success: function (res) {
                    that.charge_success();
                  },
                  fail: function (res) {
                    that.charge_fail();
                  }
                });
              },
            });

          }
        });
      } else { //提现操作
        //请调用提现接口，返回成功时候调用这个方法显示成功界面
        //this.charge_success();

        //返回失败时候调用这个方法显示失败界面
        //this.charge_fail();

      }

    } else {
      wx.showToast({
        icon: 'none',
        title: '请输入正确的金额格式',
      })
    }
  },

  // 充值（提现）成功
  charge_success: function (msg) {
    this.setData({
      chargeModalShow: false,
      chargeSuccessModalShow: true,
      chargeFailModalShow: false,
    })
    wx.hideTabBar({
      animation: false,
    })
    this.onShow()
  },

  // 充值（提现）失败
  charge_fail: function (msg) {
    this.setData({
      chargeModalShow: false,
      chargeSuccessModalShow: false,
      chargeFailModalShow: true,
    })
    wx.hideTabBar({
      animation: false,
    })
  }
})