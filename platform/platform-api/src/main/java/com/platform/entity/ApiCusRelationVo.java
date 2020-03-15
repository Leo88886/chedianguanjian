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
import java.util.Date;

/**
 * 实体
 *
 * @author heguoqiang
 * @date 2019-12-22 22:21:10
 */
public class ApiCusRelationVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    @TableId
    private Integer id;
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
    private Date createTime;
    /**
     * 销售码
     */
    private Integer salerId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFromOpenId() {
        return fromOpenId;
    }

    public void setFromOpenId(String fromOpenId) {
        this.fromOpenId = fromOpenId;
    }

    public String getToOpenId() {
        return toOpenId;
    }

    public void setToOpenId(String toOpenId) {
        this.toOpenId = toOpenId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getSalerId() {
        return salerId;
    }

    public void setSalerId(Integer salerId) {
        this.salerId = salerId;
    }















}
