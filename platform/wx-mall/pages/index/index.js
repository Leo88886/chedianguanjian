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
    channel: [],
    showModalStatus: false,
    storeName: '',
    shopkeeperName: '',
    phone: '',
    businessLicenseNo: '',
    storeLocationDetails: '',
    region: ["省", "市", "区"],
    regionFlag: 1,
    active: 0,
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
    //热销
    util.request(api.IndexUrlHotGoods).then(function (res) {
      console.log(res.errno);
      if (res.errno === 0) {
        data.hotGoods = res.data.hotGoodsList
        that.setData(data);
      }
    });
    //高返利商品
    util.request(api.IndexUrlHighReturn).then(function (res) {
      console.log(res.errno);
      if (res.errno === 0) {
        //res.data.highCouponReturnGoods[0].cash_back 返现金额
        console.log(res.data.highReturnGoods[0].cash_back);
        data.highReturnGoods = res.data.highReturnGoods
        that.setData(data);
      }
    });
    //高返卷商品
    util.request(api.IndexUrlHighCouponReturn).then(function (res) {
      console.log(res.errno);
      if (res.errno === 0) {
        //res.data.highCouponReturnGoods[0].coupon_back 返卷金额
        data.highCouponReturnGoods = res.data.highCouponReturnGoods
        that.setData(data);
      }
    });
    //商品列表
    util.request(api.IndexUrlCategory).then(function (res) {
      if (res.errno === 0) {
        //cash_back 返现金额
        //coupon_back 返卷金额
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
    //var formOpenId = decodeURIComponent(options.formOpenId);
    //wx.setStorageSync('formOpenId', formOpenId);
    var openId = wx.getStorageSync('openId');
    //判断用户是否授权登陆
    if (openId == "undefined" || openId == null || openId == "") {
      util.request(api.CartList).then(function (res) {});
    } else {
      //  if (formOpenId != "undefined" && formOpenId != null && formOpenId != ""){
      //     wx.request({
      //       url: api.SaveForwardSalerId,
      //       data: {
      //         fromOpenId: formOpenId,
      //         openId: wx.getStorageSync('openId'),
      //       },
      //       method: 'POST',
      //       header: {
      //         'content-type': 'application/json'
      //       },
      //       success: function (res) {
      //         console.log(res.data);
      //       }
      //     });
      //   }
      //查询用户门店信息，如果没有数据，则弹框
      wx.request({
        url: api.QueryStore,
        data: {
          openId: wx.getStorageSync('openId'),
        },
        method: 'POST',
        header: {
          'content-type': 'application/json'
        },
        success: function (res) {
          if (res.data.errno == "1") {
            //加判断
            that.setData({
              showModalStatus: true
            });
          }
        }
      });
    }
    that.getIndexData();
    //console.log(options);
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
  getStoreName: function (e) {
    var that = this;
    that.setData({
      storeName: e.detail.value
    });
  },
  getShopkeeperName: function (e) {
    var that = this;
    that.setData({
      shopkeeperName: e.detail.value
    });
  },
  getPhone: function (e) {
    this.setData({
      phone: e.detail.value
    });
  },
  bindRegionChange: function (e) {
    var that = this;
    that.setData({
      region: e.detail.value,
      regionFlag: 0
    });
  },
  getStoreLocationDetails: function (e) {
    var that = this;
    that.setData({
      storeLocationDetails: e.detail.value
    });
  },
  getBusinessLicenseNo: function (e) {
    var that = this;
    that.setData({
      businessLicenseNo: e.detail.value
    });
  },
  saveStore: function () {
    var that = this;
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
    wx.request({
      url: api.SaveOrUpdateStore,
      data: {
        openId: openId,
        storeName: that.data.storeName,
        shopkeeperName: that.data.shopkeeperName,
        phone: that.data.phone,
        businessLicenseNo: that.data.businessLicenseNo,
        storeLocation: that.data.region,
        storeLocationDetails: that.data.storeLocationDetails
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        if (res.data.errno == '0') {
          wx.showToast({
            title: '保存成功',
            icon: 'success',
            duration: 3000
          })
          that.setData({
            showModalStatus: false
          });
        } else {
          wx.showLoading
          wx.showLoading({
            title: '保存失败',
          })
          setTimeout(function () {
            wx.hideLoading()
          }, 3000)
          that.setData({
            showModalStatus: false
          });
        }
      }
    });
  },
  cancel: function () {
    var that = this;
    that.setData({
      showModalStatus: false
    });
  },

  toggle: function (e) {
    this.setData({
      //设置active的值为用户点击按钮的索引值
      active: e.currentTarget.dataset.index,
    })
  }
})