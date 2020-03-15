
package com.platform.service;

import com.platform.dao.ApiStoreMapper;
import com.platform.entity.ApiCusRelationVo;
import com.platform.entity.ApiStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * Service实现类
 *
 * @author heguoqiang
 * @date 2020-01-01 17:19:20
 */
@Service
public class ApiStoreService {

    @Autowired
    private ApiStoreMapper apiStoreDao;

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

    /**
     * 通过openid查询推荐者
     * @param openId
     * @return
     */
    public List<ApiCusRelationVo> queryFromUserByOpenId(String openId){
        return apiStoreDao.queryFromUserByOpenId(openId);
    }

}
