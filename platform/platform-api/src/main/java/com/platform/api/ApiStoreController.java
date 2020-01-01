
package com.platform.api;

import com.platform.service.ApiStoreService;
import com.platform.controller.AbstractController;
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
@RestController
@RequestMapping("nideshop/store")
public class ApiStoreController extends AbstractController {
    @Autowired
    private ApiStoreService storeService;

}
