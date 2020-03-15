package com.platform.dao;

import com.platform.entity.WalletVo;
import org.apache.ibatis.annotations.Param;

/**
 * 钱包
 */
public interface ApiWalletMapper extends BaseDao<WalletVo>  {

    WalletVo queryUserWallet(@Param("openId") String openId);

}