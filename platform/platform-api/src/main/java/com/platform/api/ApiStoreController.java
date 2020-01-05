
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
import org.springframework.web.bind.annotation.RequestParam;
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
        String phone = jsonParam.getString("phone");
        String businessLicenseNo = jsonParam.getString("businessLicenseNo");
        String shopkeeperName = jsonParam.getString("shopkeeperName");
        if (StringUtils.isNotEmpty(openId)) {
            ApiStore store = new ApiStore();
            store.setOpenId(openId);
            store.setStoreName(storeName);
            store.setStoreName(storeLocation);
            store.setStoreName(phone);
            store.setStoreName(businessLicenseNo);
            store.setStoreName(shopkeeperName);
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
        List<ApiCusRelationVo> list = cusRelationService.getCusByToOpenid(openId,0);        //salerId !=0     销售
        List<ApiCusRelationVo> list2 = cusRelationService.getCusByToOpenid2(openId,0);      //salerId = 0;    普通用户
        //销售
        if(null != list && list.size() > 0){
            UserVo user = userService.getUserByOpenId(list.get(0).getFromOpenId());
            map.put("referrer",user.getNickname());             //推荐人  销售
        }else{
            map.put("referrer","暂未绑销售");
        }
        //普通用户信息
        if(null != list2 && list2.size() > 0 ){
            String fromOpenId = list2.get(0).getFromOpenId();
            ApiStore store = storeService.getStoreDataByOpenId(openId);       //查询推荐人的门店信息
            if(null != store){
                map.put("userName",store.getStoreName());           //门店信息
            }else {
                UserVo user = userService.getUserByOpenId(list2.get(0).getFromOpenId());
                map.put("userName", user.getNickname());           //普通用户信息
            }
        }else{
            map.put("userName", "暂无推荐人");
        }
        return map;
    }
}
