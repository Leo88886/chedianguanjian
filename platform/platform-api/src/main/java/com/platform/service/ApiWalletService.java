package com.platform.service;


import com.platform.dao.ApiWalletMapper;
import com.platform.entity.WalletVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ApiWalletService {

    @Autowired
    private ApiWalletMapper apiWalletMapper;

    /**
     * 增加余额
     * @param addNum
     * @param openId
     */
    @Transactional
    public void addBalance(BigDecimal addNum, String openId) {
        WalletVo walletVo = apiWalletMapper.queryUserWallet(openId);
        WalletVo wo = new WalletVo();
        //第一次增加余额
        if (walletVo == null) {
            wo.setOpenId(openId);
            wo.setBalance(addNum);
            // todo 改为insert
            apiWalletMapper.insertWalletBalance(wo);
        } else {
            wo.setOpenId(openId);
            BigDecimal balance = walletVo.getBalance();
            BigDecimal afterAddNum = balance.add(addNum);
            wo.setBalance(afterAddNum);
            apiWalletMapper.updateWalletBalance(wo);
        }
        // todo 流水维护

    }

    /**
     * 减少余额
     * @param reduceNum
     * @param openId
     * @return
     */
    @Transactional
    public Integer reduceBalance(BigDecimal reduceNum, String openId) {
        WalletVo walletVo = apiWalletMapper.queryUserWallet(openId);
        WalletVo wo = new WalletVo();
        //第一次减少余额
        if (walletVo == null) {
            return 0;
        } else {
            wo.setOpenId(openId);
            BigDecimal balance = walletVo.getBalance();
            int i = balance.compareTo(reduceNum);
            if (i == -1) {
                return 0;
            }

            BigDecimal result = balance.subtract(reduceNum);
            wo.setOpenId(openId);
            wo.setBalance(result);
            apiWalletMapper.updateWalletBalance(wo);
            return 1;
        }
    }

    /**
     * 查询余额
     * @param openId
     */
    public Integer queryBalance(String openId) {
        int balance = 0;
        WalletVo walletVo = apiWalletMapper.queryUserWallet(openId);
        if(null != walletVo){
            balance = walletVo.getBalance().intValue();
        }
        return balance;
    }

}
