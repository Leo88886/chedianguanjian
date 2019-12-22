package com.platform.service;

import com.platform.dao.ApiCusRelationDao;

import com.platform.entity.ApiCusRelationVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class ApiCusRelationService {
    @Autowired
    private ApiCusRelationDao cusRelationDao;

    public void save(ApiCusRelationVo cusRelationVo) {
        cusRelationDao.save(cusRelationVo);
    }


}
