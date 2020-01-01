/*
 * 类名称:StoreDao.java
 * 包名称:com.platform.dao
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-01-01 17:19:20        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 微同科技
 */
package com.platform.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.platform.entity.ApiStore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Dao
 *
 * @author lipengjun
 * @date 2020-01-01 17:19:20
 */
@Mapper
public interface ApiStoreDao extends BaseMapper<ApiStore> {

    /**
     * 查询所有列表
     *
     * @param params 查询参数
     * @return List
     */
    List<ApiStore> queryAll(@Param("params") Map<String, Object> params);

    /**
     * 自定义分页查询
     *
     * @param page   分页参数
     * @param params 查询参数
     * @return List
     */
    List<ApiStore> selectStorePage(IPage page, @Param("params") Map<String, Object> params);
}
