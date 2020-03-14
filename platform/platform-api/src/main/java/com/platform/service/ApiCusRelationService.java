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

    public void saveOrUpdate(ApiCusRelationVo cusRelationVo) {
        cusRelationDao.saveOrUpdate(cusRelationVo);
    }

    public List<ApiCusRelationVo> queryAll (@Param("params") Map<String, Object> params){
        List<ApiCusRelationVo> list = cusRelationDao.queryAll(params);
        return list;
    }

    public List<ApiCusRelationVo> getCusByToOpenid (String toOpenId){
        List<ApiCusRelationVo> list = cusRelationDao.getCusByToOpenid(toOpenId);
        return list;
    }

    public List<ApiCusRelationVo> getCusByFromOpenId( String fromOpenId) {
        List<ApiCusRelationVo> list = cusRelationDao.getCusByFromOpenId(fromOpenId);
        return list;
    }
}
