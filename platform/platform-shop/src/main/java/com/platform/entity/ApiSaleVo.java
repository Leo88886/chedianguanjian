/*
 * 类名称:SaleEntity.java
 * 包名称:com.platform.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2019-12-23 22:05:23        lipengjun     初版做成
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
 * @author heguoqiang
 * @date 2019-12-23 22:05:23
 */
@Data
@TableName("nideshop_sale")
public class ApiSaleVo implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 销售码
     */
    @TableId
    private Integer salerId;
    /**
     * openId
     */
    private String openId;

    public ApiSaleVo() {

    }

    public ApiSaleVo(Integer salerId, String openId) {
        this.salerId = salerId;
        this.openId = openId;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getSalerId() {
        return salerId;
    }

    public void setSalerId(Integer salerId) {
        this.salerId = salerId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
