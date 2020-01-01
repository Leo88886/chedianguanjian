package com.platform.service.impl;

import com.platform.dao.SaleDao;
import com.platform.entity.SaleVo;
import com.platform.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service实现类
 *
 * @author heguoqiang
 * @email 939961241@qq.com
 * @date 2017-08-16 17:22:46
 */
@Service("saleService")
public class SaleServiceImpl implements SaleService {
    @Autowired
    private SaleDao saleDao;
    @Override
    public int save(String openId) {
        SaleVo saleVo = new SaleVo();
        saleVo.setOpenId(openId);
        saleVo.setSalerId(null);
        return saleDao.save(saleVo);
    }

    @Override
    public void delete(String openId) {
        saleDao.deleteByOpenId(openId);
    }
}
