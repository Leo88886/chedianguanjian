/*
 * 类名称:SaleDao.java
 * 包名称:com.platform.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-12-23 22:05:23        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 微同科技
 */
package com.platform.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.entity.ApiSaleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2019-12-23 22:05:23
 */
@Mapper
public interface ApiSaleDao extends BaseDao<ApiSaleVo> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ApiSaleVo> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<ApiSaleVo> selectSalePage(IPage page, @Param("params") Map<String, Object> params);

    ApiSaleVo getSalerId(@Param("params") Map<String, Object> params);


    ApiSaleVo getSalerIdByOpenId(@Param("openId") String openId);

    ApiSaleVo getSalerIdBySalerId(@Param("salerId") String salerId);
}
