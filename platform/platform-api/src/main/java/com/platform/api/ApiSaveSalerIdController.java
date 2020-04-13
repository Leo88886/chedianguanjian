package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.*;
import com.platform.service.*;
import com.platform.util.ApiBaseAction;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * API登录授权
 *
 * @author heguoqiang
 * @date 2019-12-23 15:31
 */
@Api(tags = "API保存salerId接口")
@RestController
@RequestMapping("/api/saveSalerId")
public class ApiSaveSalerIdController extends ApiBaseAction {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ApiSaleService saleService;

    @Autowired
    private ApiCusRelationService cusRelationService;

    @Autowired
    private ApiUserService userService;

    @Autowired
    private ApiUserCouponService userCouponService;

    @Autowired
    private ApiCouponService couponService;
    /**
     * 保存salerId
     */
    @IgnoreAuth
    @PostMapping("save")
    @ApiOperation(value = "保存salerId接口")
    public String save(String openId) {
        try {
            ApiSaleVo saleVo = new ApiSaleVo();
            saleVo.setSalerId(null);
            saleVo.setOpenId(openId);
            saleService.save(saleVo);
            return "success";     //保存成功
        }catch (Exception e){
            logger.error("新增用户salerId失败！", e);
            return "fail";     //保存失败
        }
    }

    /**
     * 是否保存过salerId
     */
    @IgnoreAuth
    @PostMapping("isSave")
    @ApiOperation(value = "是否保存过salerId接口")
    public String isSave() {
        JSONObject jsonParam = this.getJsonRequest();
        String openId = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("openId"))) {
            openId = jsonParam.getString("openId");
        }
        ApiSaleVo saleVo = saleService.getSalerIdByOpenId(openId);       //判断用户是否保存过salerId
        if(null != saleVo){
             return "1";    //已生成salerId
        }else{
            String result = save(openId);
            return result;     //生成salerId
        }
    }

    /**
     * 获取用户绑定的salerId
     */
    @IgnoreAuth
    @PostMapping("getRelation")
    @ApiOperation(value = "获取用户salerId接口")
        public Map<String,Object> getRelation() {
        JSONObject jsonParam = this.getJsonRequest();
        String openId = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("openId"))) {
            openId = jsonParam.getString("openId");
        }
        String salerId = "";
        String saleMon = "0";
        String saleAll = "0";
        String mySalerId = "";
        Map<String,Object> map = new HashMap();
        List<ApiCusRelationVo> relationList = cusRelationService.getCusByToOpenid(openId);   //获取绑定的销售码,空则未绑定
        if(null != relationList && relationList.size() > 0){
            salerId = relationList.get(0).getSalerId().toString();    //一个用户只能绑定一个salerId,所以返回的list应该为1
        }

        ApiSaleVo saleVo = saleService.getSalerIdByOpenId(openId);         //通过OpenId查询销售码(自己)
        if(null != saleVo && !"".equals(saleVo)){
            Calendar cale = null;
            cale = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String firstday, lastday;
            // 获取本月的第一天
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 0);
            cale.set(Calendar.DAY_OF_MONTH, 1);
            firstday = format.format(cale.getTime());
            // 获取本月的最后一天
            cale = Calendar.getInstance();
            cale.add(Calendar.MONTH, 1);
            cale.set(Calendar.DAY_OF_MONTH, 0);
            lastday = format.format(cale.getTime());
            saleAll = userService.querySalesAllBySaler(saleVo.getSalerId());          //查询销售全部推广人数
            saleMon = userService.querySalesMonBySaler(firstday, lastday, saleVo.getSalerId());       //销售本月推广人数
            mySalerId = saleVo.getSalerId().toString();
        }
        map.put("salerId", salerId);
        map.put("saleMon", saleMon);
        map.put("saleAll", saleAll);
        map.put("mySalerId", mySalerId);
        return map;
    }

    /**
     * 保存用户填写的识别码
     */
    @IgnoreAuth
    @PostMapping("saveRelation")
    @ApiOperation(value = "保存用户填写的识别码")
    public String saveRelation() {
        JSONObject jsonParam = this.getJsonRequest();
        String openId = "";
        String salerId = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("openId"))) {
            openId = jsonParam.getString("openId");
        }
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("salerId"))) {
            salerId = jsonParam.getString("salerId");
        }
        ApiSaleVo saleVo = saleService.getSalerIdBySalerId(salerId);     //通過salerId查询fromOpenId
        if(!saleVo.getOpenId().equals(openId)){         //不能绑定自己
            if(null != saleVo){
                ApiCusRelationVo cusRelationVo = new ApiCusRelationVo();
                cusRelationVo.setFromOpenId( saleVo.getOpenId());
                cusRelationVo.setToOpenId(openId);
                cusRelationVo.setSalerId(Integer.valueOf(salerId));
                cusRelationVo.setCreateTime(new Date());
                List<ApiCusRelationVo> list = cusRelationService.getCusByFromOpenId( saleVo.getOpenId());
                try {
                    if(null != list && list.size() > 0 ){
                        cusRelationService.update(cusRelationVo);
                    }else{  //第一次绑定发送优惠卷
                        cusRelationVo.setId(null);
                        cusRelationService.save(cusRelationVo);

                        Date date = new Date();
                        Calendar cl = Calendar.getInstance();
                        cl.setTime(date);
                        cl.add(Calendar.MONTH, 3);//这里就是月份的相加
                        date = cl.getTime();//获取到相加后的时间
                        List<CouponVo> CouponList = new ArrayList<>();
                        List<String> uuidList = new ArrayList<>();
                        for(int i=0;i<5;i++){
                            CouponVo couponVo = new CouponVo();
                            String uuid = UUID.randomUUID().toString();
                            couponVo.setName("用户红包");
                            couponVo.setRemark(uuid);
                            couponVo.setSend_type(1);
                            if(i == 0){
                                couponVo.setType_money(new BigDecimal(10));
                                couponVo.setMin_goods_amount(new BigDecimal(200));
                                couponVo.setMax_amount(new BigDecimal(100000));
                            }
                            if(i == 1){
                                couponVo.setType_money(new BigDecimal(30));
                                couponVo.setMin_goods_amount(new BigDecimal(500));
                                couponVo.setMax_amount(new BigDecimal(100000));
                            }
                            if(i == 2){
                                couponVo.setType_money(new BigDecimal(55));
                                couponVo.setMin_goods_amount(new BigDecimal(800));
                                couponVo.setMax_amount(new BigDecimal(100000));
                            }
                            if(i == 3){
                                couponVo.setType_money(new BigDecimal(80));
                                couponVo.setMin_goods_amount(new BigDecimal(1000));
                                couponVo.setMax_amount(new BigDecimal(100000));
                            }
                            if(i == 4){
                                couponVo.setType_money(new BigDecimal(150));
                                couponVo.setMin_goods_amount(new BigDecimal(1500));
                                couponVo.setMax_amount(new BigDecimal(100000));
                            }
                            couponVo.setMin_amount(new BigDecimal(10));
                            couponVo.setSend_start_date(new Date());
                            couponVo.setSend_end_date(date);
                            couponVo.setUse_start_date(new Date());
                            couponVo.setUse_end_date(date);
                            couponVo.setMin_transmit_num(null);
                            uuidList.add(uuid);
                            CouponList.add(couponVo);
                        }

                        couponService.saveCouponList(CouponList);   //插入5条优惠卷
                        List<CouponVo> couponInfoList = couponService.findCouponList(uuidList);     //查询插入的5条数据
                        //建立用户和优惠卷的关系
                        List<UserCouponVo> userCouponList = new ArrayList<>();
                        UserVo user = userService.getUserByOpenId(openId);
                        if(null != couponInfoList && couponInfoList.size() > 0){
                            for(CouponVo couponVo : couponInfoList){
                                UserCouponVo userCouponVo = new UserCouponVo();
                                userCouponVo.setId(null);
                                userCouponVo.setCoupon_id(couponVo.getId());
                                userCouponVo.setCoupon_number("1");
                                userCouponVo.setUser_id(user.getUserId());
                                userCouponVo.setAdd_time(new Date());
                                userCouponList.add(userCouponVo);
                            }
                            userCouponService.saveUserCouponList(userCouponList);
                        }
                    }
                    return "1";         //  保存,更新成功
                }catch (Exception e){
                    logger.error("关系表更新失败！", e);
                    return "fail";     //保存失败
                }
            }else{
                return "2";        // 不存在销售码
            }
        }else{
            return "3";        // 不能绑定自己salerId
        }
    }
}
