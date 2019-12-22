/*
 * 类名称:CusRelationEntity.java
 * 包名称:com.platform.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-12-22 22:21:10        lipengjun     初版做成
 *
 * Copyright (c) 2019-2019 微同科技
 */
package com.platform.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 实体
 *
 * @author lipengjun
 * @date 2019-12-22 22:21:10
 */
@Data
@TableName("nideshop_cus_relation")
public class ApiCusRelationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private String id;
    /**
     * 推荐者openId
     */
    private String fromOpenId;
    /**
     * 被推荐者openId
     */
    private String toOpenId;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 销售码
     */
    private String salerId;
}
