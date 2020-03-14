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
import com.platform.entity.ApiStore;
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
     * 通过toOpenId查询关系表
     * @param toOpenId
     * @return
     */
    List<ApiCusRelationVo> getCusByToOpenid(@Param("toOpenId") String toOpenId);

    /**
     * 通过fromOpenId查询关系表
     * @param fromOpenId
     * @return
     */
    List<ApiCusRelationVo> getCusByFromOpenId(@Param("fromOpenId") String fromOpenId);

    /**
     * 插入、更新
     * @param cusRelationVo
     * @return
     */
    void saveOrUpdate(@Param("relation") ApiCusRelationVo cusRelationVo);

}
