package com.platform.dao;

import com.platform.entity.WalletWaterVo;
import org.apache.ibatis.annotations.Param;

public interface ApiWalletWaterMapper extends BaseDao<WalletWaterVo> {

    /**
     * 保存余额流水
     * @param walletWaterVo
     */
    void saveWalletWater(@Param("walletWaterVo") WalletWaterVo walletWaterVo);

}
