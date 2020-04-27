package com.kthw.pmis.helper;

import com.kthw.pmis.entiy.StoreHouse;
import com.kthw.pmis.mapper.common.StoreHouseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("storeHouseHelper")
public class StoreHouseHelper {
    @Autowired
    private StoreHouseMapper storeHouseMapper;

    /**
     * 根据部门获取用户所管理的仓库
     */
    public List<StoreHouse> getStoreHosueByDepotId(){

        return null;

    }
}
