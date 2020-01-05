var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
Page({
  data: {
    elevatorFlag: 0,
    storeName: '',
    shopkeeperName: '',
    phone: '',
    businessLicenseNo: '',
    storeLocation: '',
    region: ["省", "市", "区"],
    regionFlag: 1,
    floorValue: 0,
    remarksValue: '',
    addressStatus: 0,
    userID: 0
  },
  onLoad: function() {

  },

  getStoreName: function(e) {
    var that = this;
    that.setData({
      storeName: e.detail.value
    });
  },
  getShopkeeperName: function(e) {
    var that = this;
    that.setData({
      shopkeeperName: e.detail.value
    });
  },
  getPhone: function(e) {
    this.setData({
      phone: e.detail.value
    });
  },
  bindRegionChange: function(e) {
    var that = this;
    that.setData({
      region: e.detail.value,
      regionFlag: 0
    });
  },
  getStoreLocation: function(e) {
    var that = this;
    that.setData({
      storeLocation: e.detail.value
    });
  },
  getBusinessLicenseNo: function(e) {
    var that = this;
    that.setData({
      businessLicenseNo: e.detail.value
    });
  },
  saveStore: function() {
    var that = this;
    console.log(that.data.storeName)
    console.log(that.data.shopkeeperName)
    console.log(that.data.phone)
    console.log(that.data.businessLicenseNo)
    console.log(that.data.region+"-----" + that.data.storeLocation)
    var location = that.data.region + that.data.restoreLocationgion
    var openId = wx.getStorageSync('openId'); //当前登陆用户openId
    wx.request({
      url: api.SaveOrUpdateStore,
      data: {
        openId: openId,
        storeName: that.data.storeName,
        shopkeeperName: that.data.shopkeeperName,
        phone: that.data.phone,
        businessLicenseNo: that.data.businessLicenseNo,
        storeLocation: location
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        console.log(res.data)
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
            title: '保存失败',
          })
          setTimeout(function () {
            wx.hideLoading()
          }, 2000)
        }
      }
    });

  }
  
});