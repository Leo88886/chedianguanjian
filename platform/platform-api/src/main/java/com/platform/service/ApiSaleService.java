package com.platform.service;


import com.platform.dao.ApiSaleDao;
import com.platform.entity.ApiSaleVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiSaleService {
    @Autowired
    private ApiSaleDao saleDao;

    public void save(ApiSaleVo saleVo) {
        saleDao.save(saleVo);
    }

    /**
     * 通過识别码查询销售openId
     * @param params
     * @return
     */
    public List<ApiSaleVo> queryAll (@Param("params") Map<String, Object> params){
        List<ApiSaleVo> list = saleDao.queryAll(params);
        return list;
    }
}
