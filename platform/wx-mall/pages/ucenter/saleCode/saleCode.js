var util = require('../../../utils/util.js');
var api = require('../../../config/api.js');
const user = require('../../../services/user.js');
Page({
  data:{
    salerId:''
  },
  onLoad:function(options){
    // 页面初始化 options为页面跳转所带来的参数
    // 页面显示
  },  
  //获取用户输入的用户名
  salerId: function (e) {
    var that = this;
    this.setData({
      salerId: e.detail.value
    })
  },
  saveSalerId: function(e) {
    var that = this;
    console.log(api.SaveSalerId);
    wx.request({
      url: api.SaveSalerId,
      data: {
        salerId: that.data.salerId
      },
      method: 'POST',
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
       
      }
    });

  },
})