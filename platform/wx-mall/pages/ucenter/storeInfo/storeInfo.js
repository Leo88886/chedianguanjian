var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
Page({
  data: {
    elevatorFlag: 0,
    storeName: '',
    shopkeeperName: '',
    phone: '',
    businessLicenseNo: '',
    storeLocationDetails: '',
    region: ["省", "市", "区"],
    regionFlag: 1,
    floorValue: 0,
    remarksValue: '',
    addressStatus: 0,
    userID: 0,
  },
  onLoad: function () {
    var that = this;
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
    wx.request({
      url: api.QueryStore,
      data: {
        openId: openId
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        if (null != res.data.data.storeLocation || "" != res.data.data.storeLocation) {
          that.setData({
            regionFlag: 0
          });
        }
        that.setData({
          region: JSON.parse(res.data.data.storeLocation),
          storeName: res.data.data.storeName,
          shopkeeperName: res.data.data.shopkeeperName,
          phone: res.data.data.phone,
          businessLicenseNo: res.data.data.businessLicenseNo,
          storeLocationDetails: res.data.data.storeLocationDetails
        });
      }
    });
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
    console.log(location)
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
        console.log(res.data)
        if (res.data.errno == '0') {
          wx.showToast({
            title: '保存成功',
            icon: 'success',
            duration: 3000
          })
          wx.navigateBack({
            delta: 1
          })
        } else {
          wx.showLoading
          wx.showLoading({
            title: '保存失败',
          })
          setTimeout(function () {
            wx.hideLoading()
          }, 3000)
        }
      }
    });

  }

});