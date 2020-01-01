package com.platform.service;

import com.platform.entity.SaleVo;
import org.springframework.stereotype.Service;

@Service
public interface SaleService {

    int save(String openId);

    void delete(String openId);
}
