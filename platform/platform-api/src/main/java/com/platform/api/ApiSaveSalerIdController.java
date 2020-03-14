package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.entity.ApiCusRelationVo;
import com.platform.entity.ApiSaleVo;
import com.platform.service.ApiCusRelationService;
import com.platform.service.ApiSaleService;
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
        }
        map.put("salerId", salerId);
        map.put("saleMon", saleMon);
        map.put("saleAll", saleAll);
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
            try {
                cusRelationService.saveOrUpdate(cusRelationVo);
                return "1";         //  保存成功
            }catch (Exception e){
                logger.error("关系表更新失败！", e);
                return "fail";     //保存失败
            }
        }else{
            return "2";        // 不存在销售码
        }
    }
}
