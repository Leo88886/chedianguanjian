
package com.platform.api;

import com.platform.annotation.LoginUser;
import com.platform.entity.ApiStore;
import com.platform.entity.UserVo;
import com.platform.service.ApiStoreService;
import com.platform.controller.AbstractController;
import com.platform.util.ApiBaseAction;
import com.platform.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("nideshop/store")
public class ApiStoreController extends ApiBaseAction {

    @Autowired
    private ApiStoreService storeService;

    @ApiOperation(value = "查询店铺信息", response = Map.class)
    @PostMapping("query")
    public Object query(@LoginUser UserVo loginUser) {
        if (loginUser != null && StringUtils.isNotEmpty(loginUser.getWeixin_openid())) {
            ApiStore storeDataByOpenId = null;
            try {
                storeDataByOpenId = storeService.getStoreDataByOpenId(loginUser.getWeixin_openid());
            } catch (Exception e) {
                logger.error("查询店铺信息报错", e);
                toResponsFail("查询异常");
            }
            return toResponsSuccess(storeDataByOpenId);
        } else {
            return toResponsFail("查询异常,入参为空");
        }
    }

    @ApiOperation(value = "插入或更新店铺信息", response = Map.class)
    @PostMapping("saveOrUpdate")
    public Object saveOrUpdate(ApiStore store) {
        if (store != null && StringUtils.isNotEmpty(store.getOpenId())) {
            try {
                storeService.saveOrUpdateStoreData(store);
            } catch (Exception e) {
                logger.error("新增、更新店铺信息报错", e);
                toResponsFail("新增、更新");
            }
            return toResponsMsgSuccess("新增、更新成功");
        } else {
            return toResponsFail("新增更新异常,入参、入参主键为空");
        }
    }

}
