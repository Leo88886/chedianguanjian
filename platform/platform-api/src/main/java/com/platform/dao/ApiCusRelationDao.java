/*
 * 类名称:CusRelationDao.java
 * 包名称:com.platform.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-12-22 22:21:10        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 微同科技
 */
package com.platform.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.entity.ApiCusRelationVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author heguoqiang
 * @date 2019-12-22 22:21:10
 */
@Mapper
public interface ApiCusRelationDao extends BaseDao<ApiCusRelationVo> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ApiCusRelationVo> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<ApiCusRelationVo> selectCusRelationPage(IPage page, @Param("params") Map<String, Object> params);


    /**
     *
     * @param toOpenId
     * @param salerId
     * @return
     */
    List<ApiCusRelationVo> getCusByToOpenid(@Param("toOpenId") String toOpenId, @Param("salerId") Integer salerId);

    List<ApiCusRelationVo> getRelation(@Param("toOpenId") String toOpenId);

    List<ApiCusRelationVo> getCusRelation(@Param("toOpenId") String toOpenId);

    List<ApiCusRelationVo> getCusByToOpenid2(@Param("toOpenId") String toOpenId, @Param("salerId") Integer salerId);
}
