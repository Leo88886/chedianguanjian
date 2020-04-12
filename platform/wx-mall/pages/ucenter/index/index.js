var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
var user = require('../../../services/user.js');
var app = getApp();
Page({
  data: {
    balance: 0 ,
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
    buyBalance: 0 ,//最近一次充值金额
    buyResult: false
  },
  onLoad: function (options) {
    
  },
  onReady: function () {

  },
  onShareAppMessage: function () {
    return {
      title: '车车店管家',
      path: '/pages/index/index?formOpenId=' + wx.getStorageSync('openId'), //当前登陆用户openId,
      success: function (res) {}
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

    this.setData({
      userInfo: app.globalData.userInfo,
    });

    //查推荐人
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
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
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
    var that = this;
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
    if (parseFloat(this.data.chargePrice) && this.data.chargePrice > 0
      && openId != null && openId!='') {
      // action 0表示当前操作是充值操作，1表示当前操作是提现操作
      if (this.data.action == 0) { //充值操作
        //请调用充值接口，返回成功时候调用这个方法显示成功界面
        //this.charge_success();
       const balance = this.data.chargePrice
      //  const that = this;
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

          },
        });

      //  return new Promise(function (resolve, reject) {
      //     util.request(api.BuyBanlance, {
      //       openId: openId,
      //       balance: balance
      //     }).then((res) => {
      //       if (res.errno === 0) {
      //         const payParam = res.data;
      //         const orderId = payParam.orderId;
      //         const balance = payParam.balance;
      //         wx.requestPayment({
      //           'timeStamp': payParam.timeStamp,
      //           'nonceStr': payParam.nonceStr,
      //           'package': payParam.package,
      //           'signType': payParam.signType,
      //           'paySign': payParam.paySign,
      //           'success': function (res) {
      //             that.data.balanceOrderId = orderId
      //             that.data.buyBalance = balance 
      //             that.data.buyResult = true
      //             resolve(res);
      //           },
      //           'fail': function (res) {
      //             // that.charge_fail();
      //             reject(res);
      //           },
      //           'complete': function (res) {
      //             reject(res);
      //           }
      //         });
      //       } else {
      //         reject(res);
      //       }
      //     });
      //   });

      } 
      
      else { //提现操作
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
    //获取当前充值金额以及orderId
    const openId = wx.getStorageSync('openId'); //当前登陆用户openId
    const balance = this.data.buyBalance
    const orderId = this.data.balanceOrderId

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
       
      }
    });

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