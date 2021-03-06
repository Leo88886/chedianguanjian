package com.platform.dao;

import com.platform.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 会员Dao
 *
 * @author lipengjun
 * @email 939961241@qq.com
 * @date 2017-08-16 15:02:28
 */
public interface UserDao extends BaseDao<UserEntity> {

    List<UserEntity> querySalesMonBySaler(@Param("thisMonth") String thisMonth,
                                          @Param("nextMonth") String nextMonth);

    List<UserEntity> querySalesAllBySaler();

}
