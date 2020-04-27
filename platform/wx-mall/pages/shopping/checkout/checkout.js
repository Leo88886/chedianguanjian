var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const pay = require('../../../services/pay.js');

var app = getApp();

Page({
  data: {
    checkedGoodsList: [],
    checkedAddress: {},
    checkedCoupon: [],
    couponList: [],
    goodsTotalPrice: 0.00, //商品总价
    freightPrice: 0.00, //快递费
    couponPrice: 0.00, //优惠券的价格
    orderTotalPrice: 0.00, //订单总价
    actualPrice: 0.00, //实际需要支付的总价
    addressId: 0,
    couponId: 0,
    isBuy: false,
    couponDesc: '',
    couponCode: '',
    buyType: ''
  },
  onLoad: function(options) {

    // 页面初始化 options为页面跳转所带来的参数
    if (options.isBuy != null) {
      this.data.isBuy = options.isBuy
    }
    this.data.buyType = this.data.isBuy ? 'buy' : 'cart'
    //每次重新加载界面，清空数据
    app.globalData.userCoupon = 'NO_USE_COUPON'
    app.globalData.courseCouponCode = {}
  },

  getCheckoutInfo: function() {
    let that = this;
    var url = api.CartCheckout
    let buyType = this.data.isBuy ? 'buy' : 'cart'
    util.request(url, {
      addressId: that.data.addressId,
      couponId: that.data.couponId,
      type: buyType
    }).then(function(res) {
      if (res.errno === 0) {
        that.setData({
          checkedGoodsList: res.data.checkedGoodsList,
          checkedAddress: res.data.checkedAddress,
          actualPrice: res.data.actualPrice,
          checkedCoupon: res.data.checkedCoupon ? res.data.checkedCoupon : "",
          couponList: res.data.couponList ? res.data.couponList : "",
          couponPrice: res.data.couponPrice,
          freightPrice: res.data.freightPrice,
          goodsTotalPrice: res.data.goodsTotalPrice,
          orderTotalPrice: res.data.orderTotalPrice
        });
        //设置默认收获地址
        if (that.data.checkedAddress.id) {
          let addressId = that.data.checkedAddress.id;
          if (addressId) {
            that.setData({
              addressId: addressId
            });
          }
        } else {
          wx.showModal({
            title: '',
            content: '请添加默认收货地址!',
            success: function(res) {
              if (res.confirm) {
                that.selectAddress();
              }
            }
          })
        }
      }
      wx.hideLoading();
    });
  },
  selectAddress() {
    wx.navigateTo({
      url: '/pages/shopping/address/address',
    })
  },
  addAddress() {
    wx.navigateTo({
      url: '/pages/shopping/addressAdd/addressAdd',
    })
  },
  onReady: function() {
    // 页面渲染完成

  },
  onShow: function() {
    this.getCouponData()
    // 页面显示
    wx.showLoading({
      title: '加载中...',
    })
    this.getCheckoutInfo();

    try {
      var addressId = wx.getStorageSync('addressId');
      if (addressId) {
        this.setData({
          'addressId': addressId
        });
      }
    } catch (e) {
      // Do something when catch error
    }
  },

  /**
   * 获取优惠券
   */
  getCouponData: function() {
    if (app.globalData.userCoupon == 'USE_COUPON') {
      this.setData({
        couponDesc: app.globalData.courseCouponCode.name,
        couponId: app.globalData.courseCouponCode.user_coupon_id,
      })
    } else if (app.globalData.userCoupon == 'NO_USE_COUPON') {
      this.setData({
        couponDesc: "不使用优惠券",
        couponId: '',
      })
    }
  },

  onHide: function() {
    // 页面隐藏

  },
  onUnload: function() {
    // 页面关闭

  },

  /**
   * 选择可用优惠券
   */
  tapCoupon: function() {
    let that = this

    wx.navigateTo({
      url: '../selCoupon/selCoupon?buyType=' + that.data.buyType,
    })
  },
  balancePay: function() {
    var that = this;
    util.request(api.OrderSubmit, {
      addressId: that.data.addressId,
      couponId: that.data.couponId,
      type: that.data.buyType
    }, 'POST', 'application/json').then(res => {
      if (res.errno === 0) {
        const orderId = res.data.orderInfo.id;
        util.request(api.PayPrepayId, {
          orderId: orderId,
          flag: 1
        }).then(function(res) {
          console.log(res.errno);
          if (res.errno === 0) {
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=1&orderId=' + orderId
            });
          } else {
            wx.redirectTo({
              url: '/pages/payResult/payResult?status=0&orderId=' + orderId
            });
          }
        });
      } else {
        util.showErrorToast('下单失败');
      }
    });
  },
  pay: function() {
    var that = this;
    util.request(api.OrderSubmit, {
      addressId: that.data.addressId,
      couponId: that.data.couponId,
      type: that.data.buyType
    }, 'POST', 'application/json').then(res => {
      if (res.errno === 0) {
        const orderId = res.data.orderInfo.id;
        pay.payOrder(parseInt(orderId)).then(res => {
          wx.redirectTo({
            url: '/pages/payResult/payResult?status=1&orderId=' + orderId
          });
        }).catch(res => {
          wx.redirectTo({
            url: '/pages/payResult/payResult?status=0&orderId=' + orderId
          });
        });
      } else {
        util.showErrorToast('下单失败');
      }
    });
  },
  submitOrder: function() {
    if (this.data.addressId <= 0) {
      util.showErrorToast('请选择收货地址');
      return false;
    }
    var list = this.data.checkedGoodsList;
    if (list != null && list.length>=0) {
      for (var i = 0; i < list.length;i++){
        if (list[i].is_purchase != null && list[i].is_purchase == '1' && this.data.checkedAddress.provinceName.indexOf('北京市') == -1
          && this.data.checkedAddress.provinceName.indexOf('河北省') == -1 && this.data.checkedAddress.provinceName.indexOf('天津市') == -1){
            wx.showModal({
              title: '部分商品不支持京津冀地区外的配送' ,
              content: '该订单中不支持的商品:' + list[i].goods_name,
              success: function (res) { }
            })
          return false;
          }
      }
    }
    if (this.data.checkedAddress.provinceName.indexOf('海南') > -1 || this.data.checkedAddress.provinceName.indexOf('新疆') > -1 || this.data.checkedAddress.provinceName.indexOf('西藏') > -1) {
      wx.showModal({
        title: '以下地区暂未开通服务',
        content: '四川（甘孜市、攀枝花市），海南，黑龙江（大兴安岭），新疆，西藏',
        success: function(res) {}
      })
      return false;
    }
    if (this.data.checkedAddress.provinceName.indexOf('四川') > -1 || this.data.checkedAddress.provinceName.indexOf('黑龙江') > -1) {
      if (this.data.checkedAddress.cityName.indexOf('甘孜') > -1 || this.data.checkedAddress.cityName.indexOf('攀枝花') > -1 || this.data.checkedAddress.cityName.indexOf('大兴安岭') > -1) {
        wx.showModal({
          title: '以下地区暂未开通服务',
          content: '四川（甘孜市、攀枝花市），海南，黑龙江（大兴安岭），新疆，西藏',
          success: function(res) {}
        })
        return false;
      }
    }

    var that = this;
    wx.showModal({
      title: '提示',
      content: '是否使用钱包余额支付?',
      success: function(res) {
        console.log(res);
        if (res.confirm) {
          that.balancePay();


        } else if (res.cancel) {
          that.pay();
        }
      }
    });

  }
})