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
 * 店铺信息实体
 * @author gaojian
 * @date 2020-01-01 17:19:20
 */
@Data
@TableName("nideshop_store")
public class ApiStore implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * openId
     */
    @TableId
    private String  openId;
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

    /**
     * 营业执照编号
     */
    private String businessLicenseNo;

    /**
     * 店主名称
     */
    private String shopkeeperName;

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public String getBusinessLicenseNo() {
        return businessLicenseNo;
    }

    public void setBusinessLicenseNo(String businessLicenseNo) {
        this.businessLicenseNo = businessLicenseNo;
    }

    public String getShopkeeperName() {
        return shopkeeperName;
    }

    public void setShopkeeperName(String shopkeeperName) {
        this.shopkeeperName = shopkeeperName;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }
}
