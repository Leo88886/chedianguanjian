const API_BASE_URL = 'http://192.168.0.103:8080/platform_framework/api/';
//const API_BASE_URL = 'https://chechedianguanjia.com/platform-framework/api/';

module.exports = {
  IndexUrlNewGoods: API_BASE_URL + 'index/newGoods', //新品首发
  IndexUrlHotGoods: API_BASE_URL + 'index/hotGoods', //热卖商品
  IndexUrlTopic: API_BASE_URL + 'index/topic', //专题精选
  IndexUrlBrand: API_BASE_URL + 'index/brand', //品牌制造商
  IndexUrlCategory: API_BASE_URL + 'index/category', //首页底部的分类及商品列表
  IndexUrlBanner: API_BASE_URL + 'index/banner', //首页banner
  IndexUrlChannel: API_BASE_URL + 'index/channel', //banner下的分类
  CatalogList: API_BASE_URL + 'catalog/index', //分类目录全部分类数据接口
  CatalogCurrent: API_BASE_URL + 'catalog/current', //分类目录当前分类数据接口

  AuthLoginByWeixin: API_BASE_URL + 'auth/login_by_weixin', //微信登录

  GoodsCount: API_BASE_URL + 'goods/count', //统计商品总数
  GoodsList: API_BASE_URL + 'goods/list', //获得商品列表
  GoodsCategory: API_BASE_URL + 'goods/category', //获得分类数据
  GoodsDetail: API_BASE_URL + 'goods/detail', //获得商品的详情
  GoodsHot: API_BASE_URL + 'goods/hot', //人气推荐
  GoodsRelated: API_BASE_URL + 'goods/related', //商品详情页的关联商品（大家都在看）

  BrandList: API_BASE_URL + 'brand/list', //品牌列表
  BrandDetail: API_BASE_URL + 'brand/detail', //品牌详情

  CartList: API_BASE_URL + 'cart/index', //获取购物车的数据
  CartAdd: API_BASE_URL + 'cart/add', // 添加商品到购物车
  BuyAdd: API_BASE_URL + 'buy/add', // 直接购买    
  CartUpdate: API_BASE_URL + 'cart/update', // 更新购物车的商品
  CartDelete: API_BASE_URL + 'cart/delete', // 删除购物车的商品
  CartChecked: API_BASE_URL + 'cart/checked', // 选择或取消选择商品
  CartGoodsCount: API_BASE_URL + 'cart/goodscount', // 获取购物车商品件数
  CartCheckout: API_BASE_URL + 'cart/checkout', // 下单前信息确认

  OrderSubmit: API_BASE_URL + 'order/submit', // 提交订单
  PayPrepayId: API_BASE_URL + 'pay/prepay', //获取微信统一下单prepay_id

  CollectList: API_BASE_URL + 'collect/list', //收藏列表
  CollectAddOrDelete: API_BASE_URL + 'collect/addordelete', //添加或取消收藏

  CommentList: API_BASE_URL + 'comment/list', //评论列表
  CommentCount: API_BASE_URL + 'comment/count', //评论总数
  CommentPost: API_BASE_URL + 'comment/post', //发表评论

  TopicList: API_BASE_URL + 'topic/list', //专题列表
  TopicDetail: API_BASE_URL + 'topic/detail', //专题详情
  TopicRelated: API_BASE_URL + 'topic/related', //相关专题

  SearchIndex: API_BASE_URL + 'search/index', //搜索页面数据
  SearchHelper: API_BASE_URL + 'search/helper', //搜索帮助
  SearchClearHistory: API_BASE_URL + 'search/clearhistory', //搜索帮助

  AddressList: API_BASE_URL + 'address/list', //收货地址列表
  AddressDetail: API_BASE_URL + 'address/detail', //收货地址详情
  AddressSave: API_BASE_URL + 'address/save', //保存收货地址
  AddressDelete: API_BASE_URL + 'address/delete', //删除收货地址

  RegionList: API_BASE_URL + 'region/list', //获取区域列表

  OrderList: API_BASE_URL + 'order/list', //订单列表
  OrderDetail: API_BASE_URL + 'order/detail', //订单详情
  OrderCancel: API_BASE_URL + 'order/cancelOrder', //取消订单
  OrderConfirm: API_BASE_URL + 'order/confirmOrder', //确认收货

  FootprintList: API_BASE_URL + 'footprint/list', //足迹列表
  FootprintDelete: API_BASE_URL + 'footprint/delete', //删除足迹

  FeedbackAdd: API_BASE_URL + 'feedback/save', //添加反馈
  SmsCode: API_BASE_URL + 'user/smscode', //发送短信
  BindMobile: API_BASE_URL + 'user/bindMobile', //绑定手机
  Login: API_BASE_URL + 'auth/login', //账号登录
  Register: API_BASE_URL + 'auth/register', //注册
  CouponList: API_BASE_URL + 'coupon/list', // 优惠券列表
  GoodsCouponList: API_BASE_URL + 'coupon/listByGoods', // 商品优惠券列表   
  OrderQuery: API_BASE_URL + 'pay/query', //微信查询订单状态

  HelpTypeList: API_BASE_URL + 'helpissue/typeList', //查看帮助类型列表
  HelpIssueList: API_BASE_URL + 'helpissue/issueList', //查看问题列表
  SaveSalerId: API_BASE_URL + 'saveSalerId/save', //保存salerId
  IsSaveSalerId: API_BASE_URL + 'saveSalerId/isSave', //查看是否绑定salerId
  SaveForwardSalerId: API_BASE_URL + 'saveSalerId/saveForwardSalerId', //查看是否绑定salerId
  SaveOrUpdateStore: API_BASE_URL + 'store/saveOrUpdate', // 保存更新门店信息
  QueryStore: API_BASE_URL + 'store/query', // 通过openId查询店铺
  QueryReferrer: API_BASE_URL + 'store/queryReferrer', // 通过openId查询推荐人
  GetRelation: API_BASE_URL + 'saveSalerId/getRelation', //   获取绑定salerId
  SaveRelation: API_BASE_URL + 'saveSalerId/saveRelation', // 保存绑定salerId
  IndexUrlHighReturn: API_BASE_URL + 'index/highReturnGoods', //高反利商品
  IndexUrlHighCouponReturn: API_BASE_URL + 'index/highCouponReturnGoods', //高返卷商品

  QueryBanlance: API_BASE_URL + 'wallet/balance', // 查询余额
  BuyBanlance: API_BASE_URL + 'wallet/buybalance', // 余额充值
  BuyBanlanceResult: API_BASE_URL + 'wallet/buybanlanceresult', // 余额充值结果查询、维护
  QueryCouponNum: API_BASE_URL + 'coupon/QueryCouponNum', // 查询优惠劵
};