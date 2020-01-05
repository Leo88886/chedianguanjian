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

    public List<ApiCusRelationVo> getCusByToOpenid (String toOpenId,Integer salerId){
        List<ApiCusRelationVo> list = cusRelationDao.getCusByToOpenid(toOpenId,salerId);
        return list;
    }

    public List<ApiCusRelationVo> getRelation( String toOpenId) {
        List<ApiCusRelationVo> list = cusRelationDao.getRelation(toOpenId);
        return list;
    }

    public List<ApiCusRelationVo> getCusRelation(String toOpenId) {
        List<ApiCusRelationVo> list = cusRelationDao.getCusRelation(toOpenId);
        return list;
    }

    public List<ApiCusRelationVo> getCusByToOpenid2(String toOpenId, Integer salerId) {
        List<ApiCusRelationVo> list = cusRelationDao.getCusByToOpenid2(toOpenId,salerId);
        return list;
    }
}
