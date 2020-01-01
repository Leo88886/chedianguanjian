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
import com.platform.entity.SaleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author heguoqiang
 * @date 2019-12-23 22:05:23
 */
@Mapper
public interface SaleDao extends BaseDao<SaleVo> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<SaleVo> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<SaleVo> selectSalePage(IPage page, @Param("params") Map<String, Object> params);

    SaleVo getSalerIdByOpenId(@Param("openId") String openId);

    SaleVo getSalerIdBySalerId(@Param("salerId") String salerId);

    void deleteByOpenId(@Param("openId") String openId);
}
