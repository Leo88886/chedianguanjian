package com.platform.dao;

import com.platform.entity.OrderGoodsEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 * 
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-13 10:41:09
 */
public interface OrderGoodsDao extends BaseDao<OrderGoodsEntity> {

    List<OrderGoodsEntity> queryOrderDateals(Map<String, Object> map);
}
