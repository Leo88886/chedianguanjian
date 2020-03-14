
package com.platform.api;

import com.alibaba.fastjson.JSONObject;
import com.platform.annotation.IgnoreAuth;
import com.platform.dao.ApiUserMapper;
import com.platform.entity.ApiCusRelationVo;
import com.platform.entity.ApiStore;
import com.platform.entity.UserVo;
import com.platform.service.ApiCusRelationService;
import com.platform.service.ApiStoreService;
import com.platform.service.ApiUserService;
import com.platform.util.ApiBaseAction;
import com.platform.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author heguoqiang
 * @date 2020-01-01 17:19:20
 */
@Api(tags = "店铺信息")
@RestController
@RequestMapping("/api/store")
public class ApiStoreController extends ApiBaseAction {

    @Autowired
    private ApiStoreService storeService;
    @Autowired
    private ApiCusRelationService cusRelationService;
    @Autowired
    private ApiUserService userService;

    @IgnoreAuth
    @ApiOperation(value = "查询店铺信息")
    @PostMapping("query")
    public Object query() {
        JSONObject jsonParam = this.getJsonRequest();
        String openId = jsonParam.getString("openId");
            ApiStore storeDataByOpenId = null;
            try {
                storeDataByOpenId = storeService.getStoreDataByOpenId(openId);
            } catch (Exception e) {
                logger.error("查询店铺信息报错", e);
                toResponsFail("查询异常");
            }
            if(null == storeDataByOpenId){
                return toResponsFail("查询结果为空");
            }else{
                return toResponsSuccess(storeDataByOpenId);
            }
    }

    @IgnoreAuth
    @ApiOperation(value = "插入或更新店铺信息")
    @PostMapping("saveOrUpdate")
    public Object saveOrUpdate() {
        JSONObject jsonParam = this.getJsonRequest();
        String openId = jsonParam.getString("openId");
        String storeName = jsonParam.getString("storeName");
        String storeLocation = jsonParam.getString("storeLocation");
        String storeLocationDetails = jsonParam.getString("storeLocationDetails");
        String phone = jsonParam.getString("phone");
        String businessLicenseNo = jsonParam.getString("businessLicenseNo");
        String shopkeeperName = jsonParam.getString("shopkeeperName");
        if (StringUtils.isNotEmpty(openId)) {
            ApiStore store = new ApiStore();
            store.setOpenId(openId);
            store.setStoreName(storeName);
            store.setStoreLocation(storeLocation);
            store.setStoreLocationDetails(storeLocationDetails);
            store.setPhone(phone);
            store.setBusinessLicenseNo(businessLicenseNo);
            store.setShopkeeperName(shopkeeperName);
            try {
                storeService.saveOrUpdateStoreData(store);
            } catch (Exception e) {
                logger.error("新增、更新店铺信息报错", e);
                return toResponsFail("新增、更新");
            }
            return toResponsMsgSuccess("新增、更新成功");
        } else {
            return toResponsFail("新增更新异常,入参、入参主键为空");
        }
    }

    @IgnoreAuth
    @ApiOperation(value = "查询推荐人信息")
    @PostMapping("queryReferrer")
    public Object queryReferrer() {
        Map<String,Object> map = new HashMap<String,Object>();
        JSONObject jsonParam = this.getJsonRequest();
        String openId = jsonParam.getString("openId");
        List<ApiCusRelationVo> list = cusRelationService.getCusByToOpenid(openId);
        if(null != list && list.size() > 0){
            UserVo user = userService.getUserByOpenId(list.get(0).getFromOpenId());
            map.put("referrer",user.getNickname());             //推荐人
        }else{
            map.put("referrer","暂未绑销售");
        }
         return map;
    }
}
