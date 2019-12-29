package com.platform.service.impl;

import com.platform.dao.ApiSaleDao;
import com.platform.entity.ApiSaleVo;
import com.platform.service.ApiSaleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Service实现类
 *
 * @author heguoqiang
 * @email 939961241@qq.com
 * @date 2017-08-16 17:22:46
 */
@Service("apiSaleService")
public class ApiSaleServiceImpl implements ApiSaleService {

    private ApiSaleDao saleDao;
    @Override
    public int save(String weixinOpenid) {
        ApiSaleVo saleVo = new ApiSaleVo();
        saleVo.setOpenId(weixinOpenid);
        saleVo.setSalerId(null);
        saleDao.save(saleVo);
        return 0;
    }
}
