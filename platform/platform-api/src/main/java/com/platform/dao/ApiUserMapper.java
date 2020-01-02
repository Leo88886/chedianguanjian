package com.platform.dao;

import com.platform.entity.SmsLogVo;
import com.platform.entity.UserVo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @gitee https://gitee.com/fuyang_lipengjun/platform
 * @date 2017-03-23 15:22:06
 */
public interface ApiUserMapper extends BaseDao<UserVo> {

    UserVo queryByMobile(String mobile);

    UserVo queryByOpenId(@Param("openId") String openId);

    /**
     * 获取用户最后一条短信
     *
     * @param user_id
     * @return
     */
    SmsLogVo querySmsCodeByUserId(@Param("user_id") Long user_id);

    /**
     * 保存短信
     *
     * @param smsLogVo
     * @return
     */
    int saveSmsCodeLog(SmsLogVo smsLogVo);

    /**
     * 通过openId查询用户
     * @param fromOpenId
     * @return
     */
    UserVo getUserByOpenId(@Param("fromOpenId") String fromOpenId);

    String querySalesAllBySaler(@Param("salerId") Integer salerId);

    String querySalesMonBySaler(@Param("firstday") String firstday,@Param("lastday") String lastday,@Param("salerId") Integer salerId);
}
