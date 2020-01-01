package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.ApiCusRelationVo;
import com.platform.entity.ApiSaleVo;
import com.platform.entity.UserCouponVo;
import com.platform.entity.UserVo;
import com.platform.service.ApiCusRelationService;
import com.platform.service.ApiSaleService;
import com.platform.service.ApiUserCouponService;
import com.platform.service.ApiUserService;
import com.platform.util.ApiBaseAction;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private ApiCusRelationService cusRelationService;
    @Autowired
    private ApiSaleService saleService;
    @Autowired
    private ApiUserCouponService userCouponService;
    @Autowired
    private ApiUserService userService;

    /**
     * 保存salerId
     */
    @IgnoreAuth
    @PostMapping("save")
    @ApiOperation(value = "保存salerId接口")
    public String save() {
        JSONObject jsonParam = this.getJsonRequest();
        String openId = "";
        String salerId = "";
        String fromOpenId = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("openId"))) {
            openId = jsonParam.getString("openId");
        }
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("salerId"))) {
            salerId = jsonParam.getString("salerId");
        }

        ApiSaleVo saleVo = saleService.getSalerIdBySalerId(salerId);     //通過salerId查询fromOpenId
        if(null != saleVo){
            fromOpenId = saleVo.getOpenId();
            ApiCusRelationVo cusRelationVo = new ApiCusRelationVo();
            cusRelationVo.setFromOpenId(fromOpenId);
            cusRelationVo.setToOpenId(openId);
            cusRelationVo.setSalerId(Integer.valueOf(salerId));
            cusRelationVo.setId(null);
            cusRelationVo.setCreateTime(new Date());
            cusRelationService.save(cusRelationVo);
            return "1";         //  保存成功
        }else{
            return "2";        // 不存在销售码
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
        String toOpenId = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("openId"))) {
            toOpenId = jsonParam.getString("openId");
        }
        List<ApiCusRelationVo> list = cusRelationService.getCusByToOpenid(toOpenId,0);       //通过toOpenId，且salerId!=0查询是否存在关系
        if(null != list && list.size() > 0){
            return list.get(0).getSalerId().toString();     // 已经绑定过识别码
        }else{
            return "";
        }
    }

    /**
     * 转发保存salerId
     */
    @IgnoreAuth
    @PostMapping("saveForwardSalerId")
    @ApiOperation(value = "转发保存salerId")
    public String saveForwardSalerId() {
        JSONObject jsonParam = this.getJsonRequest();
        String toOpenId = "";
        String fromOpenId = "";
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("openId"))) {
            toOpenId = jsonParam.getString("openId");
        }
        if (!StringUtils.isNullOrEmpty(jsonParam.getString("fromOpenId"))) {
            fromOpenId = jsonParam.getString("fromOpenId");
        }

        ApiSaleVo saleVo = saleService.getSalerIdByOpenId(fromOpenId);         //判断转发者是否为销售
        if (null != saleVo) {             //销售转发
            List<ApiCusRelationVo> list = cusRelationService.getCusByToOpenid(toOpenId,0);         //判断是否已绑定销售
            if (null == list || list.size() <= 0) {
                ApiCusRelationVo cusRelationVo = new ApiCusRelationVo();
                cusRelationVo.setSalerId(saleVo.getSalerId());
                cusRelationVo.setFromOpenId(fromOpenId);
                cusRelationVo.setToOpenId(toOpenId);
                cusRelationVo.setId(null);
                cusRelationService.save(cusRelationVo);
                return "1";          //保存成功
            } else {
                return "2";          //已绑定销售
            }
        } else {             //普通用户转发
            List<ApiCusRelationVo> list2 = cusRelationService.getCusRelation(fromOpenId);         //判断用户是否已转发过
            if (null == list2 || list2.size() <= 0) {
                ApiCusRelationVo cusRelationVo2 = new ApiCusRelationVo();
                cusRelationVo2.setSalerId(0);
                cusRelationVo2.setFromOpenId(fromOpenId);
                cusRelationVo2.setToOpenId(toOpenId);
                cusRelationVo2.setCreateTime(new Date());
                cusRelationVo2.setId(null);
                cusRelationService.save(cusRelationVo2);
                //第一次转发送优惠卷
                UserVo user = userService.getUserByOpenId(fromOpenId);
                UserCouponVo userCouponVo = new UserCouponVo();
                userCouponVo.setId(null);
                userCouponVo.setCoupon_id(2);
                userCouponVo.setCoupon_number("1");
                userCouponVo.setUser_id(user.getUserId());
                userCouponVo.setAdd_time(new Date());
                userCouponService.save(userCouponVo);
                return "3";                 // 第一次转发
            }else{
                return "4";                 //已转发过
            }

        }






    }
}
