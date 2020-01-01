/*
 * 类名称:StoreEntity.java
 * 包名称:com.platform.entity
 *
 * 修改履历:
 *     日期                       修正者        主要内容
 *     2020-01-01 17:19:20        lipengjun     初版做成
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
 * @date 2020-01-01 17:19:20
 */
@Data
@TableName("nideshop_store")
public class ApiStore implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    @TableId
    private Integer id;
    /**
     * 门店名称
     */
    private String storeName;
    /**
     * 门店位置
     */
    private String storeLocation;
    /**
     * 联系方式
     */
    private String phone;

    public ApiStore() {

    }
    public ApiStore(Integer id, String storeName, String storeLocation, String phone) {
        this.id = id;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.phone = phone;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(String storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
