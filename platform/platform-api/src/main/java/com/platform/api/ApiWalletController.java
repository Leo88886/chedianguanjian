package com.platform.api;


import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiWalletMapper;
import com.platform.dao.ApiWalletWaterMapper;
import com.platform.entity.OrderVo;
import com.platform.entity.UserVo;
import com.platform.entity.WalletVo;
import com.platform.entity.WalletWaterVo;
import com.platform.service.ApiOrderService;
import com.platform.service.ApiWalletService;
import com.platform.util.ApiBaseAction;
import com.platform.util.CommonUtil;
import com.platform.util.wechat.WechatUtil;
import com.platform.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

@Api(tags = "钱包以及流水相关")
@RestController
@RequestMapping("/api/wallet")
public class ApiWalletController extends ApiBaseAction {

    @Autowired
    private ApiWalletMapper apiWalletMapper;
    @Autowired
    private ApiWalletService apiWalletService;
    @Autowired
    private ApiWalletWaterMapper apiWalletWaterMapper;
    @Autowired
    private ApiOrderService apiOrderService;

    @IgnoreAuth
    @PostMapping("balance")
    @ApiOperation(value = "余额查询")
    public Object queryBalance() {
        JSONObject jsonParam = this.getJsonRequest();
        String openId = jsonParam.getString("openId");
        WalletVo walletVo = null;
        try {
            walletVo = apiWalletMapper.queryUserWallet(openId);
        } catch (Exception e) {
            return toResponsFail("查询余额失败");
        }
        if (walletVo == null) {
            WalletVo wo = new WalletVo();
            wo.setBalance(new BigDecimal(0));
            return wo;
        }

        return walletVo;

    }

    @IgnoreAuth
    @PostMapping("buybalance")
    @ApiOperation(value = "余额充值")
    public Object buyBalance() {

//        if (loginUser==null) {
//            return toResponsObject(400, "用户信息为空", "");
//        }
        JSONObject jsonParam = this.getJsonRequest();
        String openId = jsonParam.getString("openId");
        String balanceStr = jsonParam.getString("balance");
        BigDecimal balance = new BigDecimal(balanceStr);
        String nonceStr = CharUtil.getRandomString(32);

        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
        Map<Object, Object> resultObj = new TreeMap();

        try {
            Map<Object, Object> parame = new TreeMap<Object, Object>();
            parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
            // 商家账号。
            parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
            // 随机字符串
            parame.put("nonce_str", randomStr);
            // 商户订单编号
            String orderId = CommonUtil.generateOrderNumber();
            parame.put("out_trade_no", orderId);
            // 商品描述
            parame.put("body", "chongzhi");
            //支付金额
            parame.put("total_fee", balance.multiply(new BigDecimal(100)).intValue());
            // 回调地址
            parame.put("notify_url", ResourceUtil.getConfigByName("wx.notifyUrl"));
            // 交易类型APP
            parame.put("trade_type", ResourceUtil.getConfigByName("wx.tradeType"));
            parame.put("spbill_create_ip", getClientIp());
            parame.put("openid", openId);
            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
            // 数字签证
            parame.put("sign", sign);

            String xml = MapUtils.convertMap2Xml(parame);
            logger.info("xml:" + xml);
            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.uniformorder"), xml));
            // 响应报文
            String return_code = MapUtils.getString("return_code", resultUn);
            String return_msg = MapUtils.getString("return_msg", resultUn);
            //
            if (return_code.equalsIgnoreCase("FAIL")) {
                return toResponsFail("支付失败," + return_msg);
            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
                // 返回数据
                String result_code = MapUtils.getString("result_code", resultUn);
                String err_code_des = MapUtils.getString("err_code_des", resultUn);
                if (result_code.equalsIgnoreCase("FAIL")) {
                    return toResponsFail("支付失败," + err_code_des);
                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
                    String prepay_id = MapUtils.getString("prepay_id", resultUn);
                    // 先生成paySign 参考https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=5
                    resultObj.put("appId", ResourceUtil.getConfigByName("wx.appId"));
                    resultObj.put("timeStamp", DateUtils.timeToStr(System.currentTimeMillis() / 1000, DateUtils.DATE_TIME_PATTERN));
                    resultObj.put("nonceStr", nonceStr);
                    resultObj.put("package", "prepay_id=" + prepay_id);
                    resultObj.put("signType", "MD5");
                    String paySign = WechatUtil.arraySign(resultObj, ResourceUtil.getConfigByName("wx.paySignKey"));
                    resultObj.put("paySign", paySign);
                    resultObj.put("balance", balance);
                    resultObj.put("orderId", orderId);
                    return toResponsObject(0, "微信统一订单下单成功", resultObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("下单失败,error=" + e.getMessage());
        }
        return toResponsFail("下单失败");

    }

    @IgnoreAuth
    @PostMapping("buybanlanceresult")
    @ApiOperation(value = "余额充值结果")
    public Object buyBanlanceResult() {
        JSONObject jsonParam = this.getJsonRequest();
        String openId = jsonParam.getString("openId");
        String balanceStr = jsonParam.getString("balance");
        String orderId = jsonParam.getString("orderId");
        BigDecimal balance = new BigDecimal(balanceStr);
        if (orderId == null) {
            return toResponsFail("未传入支付编号");
        }

        if (balance == null) {
            return toResponsFail("未传入充值金额");
        }

        if (StringUtils.isNullOrEmpty(openId)) {
            return toResponsFail("用户不存在或者未登录");
        }

        Map<Object, Object> parame = new TreeMap<Object, Object>();
        parame.put("appid", ResourceUtil.getConfigByName("wx.appId"));
        // 商家账号。
        parame.put("mch_id", ResourceUtil.getConfigByName("wx.mchId"));
        String randomStr = CharUtil.getRandomNum(18).toUpperCase();
        // 随机字符串
        parame.put("nonce_str", randomStr);
        // 商户订单编号
        parame.put("out_trade_no", orderId);

        String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
        // 数字签证
        parame.put("sign", sign);

        String xml = MapUtils.convertMap2Xml(parame);
        logger.info("xml:" + xml);
        Map<String, Object> resultUn = null;
        try {
            resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.orderquery"), xml));
        } catch (Exception e) {
            e.printStackTrace();
            return toResponsFail("查询失败,error=" + e.getMessage());
        }
        // 响应报文
        String return_code = MapUtils.getString("return_code", resultUn);
        String return_msg = MapUtils.getString("return_msg", resultUn);

        if (!"SUCCESS".equals(return_code)) {
            return toResponsFail("查询失败,error=" + return_msg);
        }

        String trade_state = MapUtils.getString("trade_state", resultUn);
        if ("SUCCESS".equals(trade_state)) {

            String weixin_openid = openId;
            // 增加余额
            //维护流水
            apiWalletService.addBalance(balance, weixin_openid,1);
//            WalletWaterVo wwo = new WalletWaterVo();
//            wwo.setOpenId(weixin_openid);
//            wwo.setDealNum(balance);
//            wwo.setTime(new Date());
//            wwo.setType(1);
//            apiWalletWaterMapper.saveWalletWater(wwo);

            return toResponsMsgSuccess("支付成功");
        } else if ("USERPAYING".equals(trade_state)) {
            // 重新查询 正在支付中
            Integer num = (Integer) J2CacheUtils.get(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId + "");
            // 重新查询 正在支付中
            if (num == null) {
                J2CacheUtils.put(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId + "", 1);
                this.buyBanlanceResult();
            } else if (num <= 3) {
                J2CacheUtils.remove(J2CacheUtils.SHOP_CACHE_NAME, "queryRepeatNum" + orderId);
                this.buyBanlanceResult();
            } else {
                return toResponsFail("查询失败,error=" + trade_state);
            }

        } else {
            // 失败
            return toResponsFail("查询失败,error=" + trade_state);
        }

        return toResponsFail("查询失败，未知错误");

    }

//    @IgnoreAuth
    @PostMapping("pickBalance")
    @ApiOperation(value = "余额提现")
    public Object pickBalance(@LoginUser UserVo loginUser,String openId,String picNum){

        if(loginUser==null){
            return toResponsFail("提现失败,请登录");
        }

        BigDecimal pkNumberDe = new BigDecimal(picNum);

        if(StringUtils.isNullOrEmpty(openId)){
            return toResponsFail("提现失败,用户存在问题，请联系管理员");
        }

        if(pkNumberDe.compareTo(new BigDecimal(50)) == -1){
            return toResponsFail("提现失败," + "提现金额需要大于50元");
        }
        //执行余额、流水处理
        Integer res = apiWalletService.reduceBalance(pkNumberDe, openId, 4);

        if(res==0){
            return toResponsFail("提现失败,请联系管理员" );
        }
        OrderVo ovo = new OrderVo();
        ovo.setOrder_sn(CommonUtil.generateOrderNumber());
        ovo.setUser_id(loginUser.getUserId());
        //留言
        ovo.setPostscript("提现");
        ovo.setActual_price(pkNumberDe);
        // 待付款
        ovo.setOrder_status(0);
        ovo.setShipping_status(0);
        ovo.setPay_status(2);
        ovo.setShipping_id(0);
        ovo.setShipping_fee(new BigDecimal(0));
        ovo.setIntegral(0);
        ovo.setIntegral_money(new BigDecimal(0));
        ovo.setOrder_type("5");
        apiOrderService.save(ovo);

        return toResponsObject(0, "提现成功，5个工作日内到账","提现成功，5个工作日内到账");

//        //https://pay.weixin.qq.com/wiki/doc/api/wxa/wxa_api.php?chapter=7_7&index=3
//        Map<Object, Object> resultObj = new TreeMap();
//
//        try {
//            Map<Object, Object> parame = new TreeMap<Object, Object>();
//            parame.put("mch_appid", ResourceUtil.getConfigByName("wx.appId"));
//            // 商家账号。
//            parame.put("mchid", ResourceUtil.getConfigByName("wx.mchId"));
//            String randomStr = CharUtil.getRandomNum(18).toUpperCase();
//            // 随机字符串
//            parame.put("nonce_str", randomStr);
//            // 商户订单编号
//            String orderId = CommonUtil.generateOrderNumber();
//            parame.put("partner_trade_no", orderId);
//
//            parame.put("openid", openId);
//            parame.put("check_name", "NO_CHECK");
//
//            //支付金额
//            parame.put("amount", pkNumberDe.multiply(new BigDecimal(100)).intValue());
//
//            parame.put("desc", "车车店管家提现");
//            String sign = WechatUtil.arraySign(parame, ResourceUtil.getConfigByName("wx.paySignKey"));
//            // 数字签证
//            parame.put("sign", sign);
//
//            String xml = MapUtils.convertMap2Xml(parame);
//            logger.info("xml:" + xml);
//            Map<String, Object> resultUn = XmlUtil.xmlStrToMap(WechatUtil.requestOnce(ResourceUtil.getConfigByName("wx.pickurl"), xml));
//            // 响应报文
//            String return_code = MapUtils.getString("return_code", resultUn);
//            String return_msg = MapUtils.getString("return_msg", resultUn);
//            //
//            if (return_code.equalsIgnoreCase("FAIL")) {
//                logger.error("@@@@@@@提现失败"+return_msg);
//                return toResponsFail("提现失败," + return_msg);
//            } else if (return_code.equalsIgnoreCase("SUCCESS")) {
//                // 返回数据
//                String result_code = MapUtils.getString("result_code", resultUn);
//                String err_code_des = MapUtils.getString("err_code_des", resultUn);
//                logger.error("@@@@@成功111"+result_code+"@@@@@@"+err_code_des);
//                if (result_code.equalsIgnoreCase("FAIL")) {
//                    return toResponsFail("提现失败," + err_code_des);
//                } else if (result_code.equalsIgnoreCase("SUCCESS")) {
//                    logger.error("@@@@@成功222"+return_msg);
//                    return toResponsObject(0, "提现成功", resultObj);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return toResponsFail("提现失败,error=" + e.getMessage());
//        }
//        return toResponsFail("提现失败");

    }

}
