package com.platform.api;

import com.platform.annotation.IgnoreAuth;
import com.platform.service.ApiCusRelationService;
import com.platform.util.ApiBaseAction;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * API登录授权
 *
 * @author heguoqiang
 * @date 2019-12-23 15:31
 */
@Api(tags = "API保存salerId接口")
@RestController
@RequestMapping("/api/SaveSalerId")
public class ApiSaveSalerIdController extends ApiBaseAction {
    private Logger logger = Logger.getLogger(getClass());
    @Autowired
    private ApiCusRelationService apiCusRelationService;


    /**
     * 登录
     */
    @IgnoreAuth
    @PostMapping("save")
    @ApiOperation(value = "保存salerId接口")
    public String login(String salerId, String openId) {

        return "";
    }
}
