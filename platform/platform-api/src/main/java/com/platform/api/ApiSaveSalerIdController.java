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
     * 登录
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
        Map<String,Object> map2 = new HashMap<String,Object>();
        map.put("openId",openId);
        map2.put("salerId",salerId);
        List<ApiCusRelationVo> cusList = cusRelationService.queryAll(map);     //通過用户openId查询是否存在
        List<ApiSaleVo> saleList = saleService.queryAll(map);     //通過识别码查询销售openId
        if(null == cusList || cusList.size() <= 0){
            if(null != saleList && saleList.size() > 0){
                fromOpenId = saleList.get(0).getOpenId();
                ApiCusRelationVo cusRelationVo = new ApiCusRelationVo();
                cusRelationVo.setFromOpenId(fromOpenId);
                cusRelationVo.setToOpenId(openId);
                cusRelationVo.setSalerId(Integer.valueOf(salerId));
                cusRelationVo.setId(null);
                cusRelationService.save(cusRelationVo);
                return "1";         //
            }else{
                return "2";        //
            }
        }else {
            return "2";            //
        }
    }
}
