package com.platform.service;

import com.platform.dao.ApiCusRelationMapper;
import com.platform.entity.ApiCusRelationVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class ApiCusRelationService {
    @Autowired
    private ApiCusRelationMapper cusRelationDao;

    /**
     * 保存绑定关系
     * @param cusRelationVo
     */
    public void save(ApiCusRelationVo cusRelationVo) {
        cusRelationDao.save(cusRelationVo);
    }

    /**
     * 修改绑定关系
     * @param cusRelationVo
     */
    public void update(ApiCusRelationVo cusRelationVo) {
        cusRelationDao.update(cusRelationVo);
    }

    /**
     * 通过toOpenId查询绑定关系
     * @param toOpenId
     * @return
     */
    public List<ApiCusRelationVo> getCusByToOpenid (String toOpenId){
        List<ApiCusRelationVo> list = cusRelationDao.getCusByToOpenid(toOpenId);
        return list;
    }

    /**
     * 通过fromOpenId查询绑定关系
     * @param fromOpenId
     * @return
     */
    public List<ApiCusRelationVo> getCusByFromOpenId( String fromOpenId) {
        List<ApiCusRelationVo> list = cusRelationDao.getCusByFromOpenId(fromOpenId);
        return list;
    }

}
