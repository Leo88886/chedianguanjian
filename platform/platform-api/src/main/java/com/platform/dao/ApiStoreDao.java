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
 * @author gaojian
 * @date 2020-01-01 17:19:20
 */
@Mapper
public interface ApiStoreDao extends BaseMapper<ApiStore> {

    /**
     * 根据openid查询
     *
     * @return List
     */
    ApiStore queryByOpenId(@Param("openId")String  openId);

    /**
     * 插入、更近
     * @param store
     * @return
     */
    void saveOrUpdate(@Param("params") ApiStore store);
}
