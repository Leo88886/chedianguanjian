package com.platform.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 钱包实体
 */
public class WalletVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String openId; //用户openid
    private BigDecimal balance; //用户钱包余额

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
