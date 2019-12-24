package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.ApiCusRelationVo;
import com.platform.entity.ApiSaleVo;
import com.platform.service.ApiCusRelationService;
import com.platform.service.ApiSaleService;
import com.platform.util.ApiBaseAction;
import com.qiniu.util.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("salerId",salerId);
        List<ApiSaleVo> saleList = saleService.queryAll(map);     //通過识别码查询销售openId
        if(null != saleList && saleList.size() > 0){
            fromOpenId = saleList.get(0).getOpenId();
            ApiCusRelationVo cusRelationVo = new ApiCusRelationVo();
            cusRelationVo.setFromOpenId(fromOpenId);
            cusRelationVo.setToOpenId(openId);
            cusRelationVo.setSalerId(Integer.valueOf(salerId));
            cusRelationVo.setId(null);
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
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("toOpenId",toOpenId);
        map.put("salerId",0);
        List<ApiCusRelationVo> list = cusRelationService.getCusByToOpenid(map);       //通过toOpenId，且salerId!=0查询是否存在关系
        if(null != list && list.size() > 0){
            return "2";     // 已经绑定过识别码
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
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("toOpenId",toOpenId);
        map.put("fromOpenId",fromOpenId);

       List<ApiCusRelationVo> list =  cusRelationService.queryAll(map);         //通过fromOpenId toOpenId 查询是否存在关系
       if(null == list || list.size() < 0){
           Map<String,Object> map2 = new HashMap<String,Object>();
           map.put("fromOpenId",fromOpenId);
           ApiSaleVo saleVo = saleService.getSalerId(map2);         //通过fromOpenId获取salerId
           ApiCusRelationVo cusRelationVo = new ApiCusRelationVo();
           if(null != saleVo){
               cusRelationVo.setSalerId(saleVo.getSalerId());
           }else {
               cusRelationVo.setSalerId(0);         //普通用户转发
           }
           cusRelationVo.setFromOpenId(fromOpenId);
           cusRelationVo.setToOpenId(toOpenId);
           cusRelationVo.setId(null);
           cusRelationService.save(cusRelationVo);
           return "1";          //保存成功
       }else{
           return "2";          //已被绑定
       }

    }
}
