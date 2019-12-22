package com.platform.api;


/**
 * 作者: @author Harmon <br>
 * @gitee https://gitee.com/fuyang_lipengjun/platform
 * 时间: 2017-08-11 08:32<br>
 * 描述: ApiIndexController <br>
 */
@Api(tags = "保存salerId")
@RestController
@RequestMapping("/api/saveSalerId")
public class ApiSaveSalerIdController extends ApiBaseAction {
    @Autowired
    private ApiAddressService addressService;
    /**
     * 保存salerId
     */
    @ApiOperation(value = "保存salerId", response = Map.class)
    @PostMapping("save")
    public void save() {




    }
}
