package com.platform.api;


import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.annotation.LoginUser;
import com.platform.cache.J2CacheUtils;
import com.platform.dao.ApiWalletMapper;
import com.platform.dao.ApiWalletWaterMapper;
import com.platform.entity.UserVo;
import com.platform.entity.WalletVo;
import com.platform.entity.WalletWaterVo;
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
            apiWalletService.addBalance(balance, weixin_openid,1);

            //维护流水
            WalletWaterVo wwo = new WalletWaterVo();
            wwo.setOpenId(weixin_openid);
            wwo.setDealNum(balance);
            wwo.setTime(new Date());
            wwo.setType(1);
            apiWalletWaterMapper.saveWalletWater(wwo);

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


}
