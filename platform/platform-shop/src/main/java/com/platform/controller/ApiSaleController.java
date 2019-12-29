package com.platform.controller;

import com.platform.entity.AdEntity;
import com.platform.service.AdService;
import com.platform.service.ApiSaleService;
import com.platform.utils.PageUtils;
import com.platform.utils.Query;
import com.platform.utils.R;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller
 *
 * @author heguoqiang
 * @email 939961241@qq.com
 * @date 2019-12-29 09:37:35
 */
@RestController
@RequestMapping("apiSale")
public class ApiSaleController {
    @Autowired
    private ApiSaleService saleService;
    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("sale:save")
    public R save(@RequestBody String openId) {
        saleService.save(openId);
        return R.ok();
    }
}
