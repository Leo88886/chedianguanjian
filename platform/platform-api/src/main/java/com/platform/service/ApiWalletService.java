package com.platform.service;


import com.platform.dao.ApiWalletMapper;
import com.platform.dao.ApiWalletWaterMapper;
import com.platform.entity.WalletVo;
import com.platform.entity.WalletWaterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class ApiWalletService {

    @Autowired
    private ApiWalletMapper apiWalletMapper;
    @Autowired
    private ApiWalletWaterMapper walletWaterMapper;

    /**
     * 增加余额
     * @param addNum
     * @param openId
     */
    @Transactional
    public void addBalance(BigDecimal addNum, String openId,Integer type) {
        WalletVo walletVo = apiWalletMapper.queryUserWallet(openId);
        WalletVo wo = new WalletVo();
        Date date = new Date();
        //第一次增加余额
        if (walletVo == null) {
            wo.setOpenId(openId);
            wo.setBalance(addNum);
            apiWalletMapper.insertWalletBalance(wo);
        } else { //对已有余额进行增加
            wo.setOpenId(openId);
            BigDecimal balance = walletVo.getBalance();
            BigDecimal afterAddNum = balance.add(addNum);
            wo.setBalance(afterAddNum);
            apiWalletMapper.updateWalletBalance(wo);
        }
        // 流水维护
        WalletWaterVo walletWaterVo = new WalletWaterVo();
        walletWaterVo.setOpenId(openId);
        walletWaterVo.setTime(new Date());
        walletWaterVo.setDealNum(addNum);
        walletWaterVo.setType(type);
        walletWaterMapper.saveWalletWater(walletWaterVo);
    }

    /**
     * 减少余额
     * @param reduceNum
     * @param openId
     * @return
     */
    @Transactional
    public Integer reduceBalance(BigDecimal reduceNum, String openId,Integer type) {
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
            WalletWaterVo walletWaterVo = new WalletWaterVo();
            walletWaterVo.setOpenId(openId);
            walletWaterVo.setTime(new Date());
            walletWaterVo.setDealNum(result);
            walletWaterVo.setType(type);
            walletWaterMapper.saveWalletWater(walletWaterVo);
            return 1;
        }
    }

    /**
     * 查询余额
     * @param openId
     */
    public BigDecimal queryBalance(String openId) {
         BigDecimal balance = new BigDecimal(0);
        WalletVo walletVo = apiWalletMapper.queryUserWallet(openId);
        if(null != walletVo){
            balance = walletVo.getBalance();
        }
        return balance;
    }

}
