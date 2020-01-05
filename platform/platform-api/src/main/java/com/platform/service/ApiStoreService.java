
package com.platform.service;


import com.platform.dao.ApiStoreDao;
import com.platform.entity.ApiStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Service实现类
 *
 * @author heguoqiang
 * @date 2020-01-01 17:19:20
 */
@Service
public class ApiStoreService {

    @Autowired
    ApiStoreDao apiStoreDao;

    /**
     * 查询
     * @param openId
     * @return
     */
    public ApiStore getStoreDataByOpenId(String openId){
        return apiStoreDao.queryByOpenId(openId);
    }

    /**
     * 插入、如果存在即更新
     * @param store
     */
    public void  saveOrUpdateStoreData(ApiStore store){
        apiStoreDao.saveOrUpdate(store);
    }

}
