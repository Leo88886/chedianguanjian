package com.platform.service;

import com.platform.dao.ApiCusRelationDao;

import com.platform.entity.ApiCusRelationVo;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ApiCusRelationService {
    @Autowired
    private ApiCusRelationDao cusRelationDao;

    public void save(ApiCusRelationVo cusRelationVo) {
        cusRelationDao.save(cusRelationVo);
    }

    public List<ApiCusRelationVo> queryAll (@Param("params") Map<String, Object> params){
        List<ApiCusRelationVo> list = cusRelationDao.queryAll(params);
        return list;
    }

    public List<ApiCusRelationVo> getCusByToOpenid (@Param("params") Map<String, Object> params){
        List<ApiCusRelationVo> list = cusRelationDao.getCusByToOpenid(params);
        return list;
    }
}
