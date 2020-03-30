package com.kthw.pmis.helper;

import static org.hamcrest.CoreMatchers.nullValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.StoreHouse;
import com.kthw.pmis.mapper.common.StoreHouseMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.kthw.pmis.entiy.AssetAttributes;
import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.mapper.common.AssetAttributesMapper;
import com.kthw.pmis.mapper.common.DepotMapper;
import com.mysql.fabric.xmlrpc.base.Array;

@Component("depotHelper")
public class DepotHelper {
    @Autowired
    private DepotMapper depotMapper;
    @Autowired
    private AssetAttributesMapper assetAttributesMapper;
    @Autowired
    private StoreHouseMapper storeHouseMapper;

    /**
     * 获取资产属性
     *
     * @param depotId
     * @return
     */
    public AssetAttributes getAssetAttribute(Long depotId) {
        depotId = getWorkShop(depotId);
        if (depotId != null) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("eqDepotId", depotId);
            List<AssetAttributes> assetAttribute = assetAttributesMapper.selectByMap(params);
            if (assetAttribute.size() > 0) {
                return assetAttribute.get(0);
//                }
            }

        }

        return null;
    }

    public AssetAttributes getAssetAttributeByStoreHouseId(SheetInfo sheetInfo) {
        Map<String, Object> params = new HashMap<>();
        StoreHouse storeHouse = storeHouseMapper.selectByPrimaryKey(sheetInfo.getObjectStoreHouseId());
        if (storeHouse.getDepotId() != 0) {
//            //循环获取父部门，全部车辆段的部门level为3
//            while (depot.getDepotLevel() > 3) {
//                depot = depotMapper.selectByPrimaryKey(depot.getParentId());
//
//            }
//            //车辆段对应资产属性
//            if (depot.getDepotLevel() == 3) {//全部车辆段的部门level为3
            params.clear();
            params.put("eqDepotId", storeHouse.getDepotId());
            List<AssetAttributes> assetAttribute = assetAttributesMapper.selectByMap(params);
            if (assetAttribute.size() > 0) {
                return assetAttribute.get(0);
//                }
            }

        }

        return null;
    }

    public List<Depot> getChildrens(Long depotId) {
        Depot depot = depotMapper.selectByPrimaryKey(depotId);
        List<Depot> list = new ArrayList<Depot>();
        if (depot != null) {
            Map<String, Object> params = new HashMap<String, Object>();
            if (depot.getDepotLevel() == 3) {//段级别
                List<Depot> workShops = new ArrayList<Depot>();
                params.put("eqParentId", depot.getDepotId());
                workShops=depotMapper.selectByMap(params);
                for (Depot workShop:workShops){
                    list.addAll(getChildrens(workShop.getDepotId()));
                }
            }else if (depot.getDepotLevel() == 4) {//车间级别
                params.put("eqParentId", depot.getDepotId());
                list.addAll(depotMapper.selectByMap(params));
            } else if (depot.getDepotLevel() <= 2) {//检测所
                list.addAll(depotMapper.selectByMap(params));
            }
            list.add(depot);
            return list;

            //车辆段对应资产属性

        }
        return null;

    }

    /**
     * 获取用户管理的部门
     *
     * @param
     * @return
     */
    public List<Depot> getDepots(Long depotId) {
        List<Depot> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Depot depot = depotMapper.selectByPrimaryKey(depotId);
        if (depot != null) {
            //机辆检测所级别部门
            if (depot.getDepotLevel() < 3) {
                list = depotMapper.selectByMap(null);
                //车辆段级别部门
            } else if (depot.getDepotLevel() == 3) {
                //获取车间部门
                List<Depot> workShopDepots = getChildDepots(depot);
                list.addAll(workShopDepots);
                for (Depot workShop : workShopDepots) {
                    //获取班下面部门
                    List<Depot> groups = new ArrayList<>();
                    groups = getChildrens(workShop.getDepotId());
                    list.addAll(groups);
                }

                //车间级别部门。获取下面班组的部门
            } else if (depot.getDepotLevel() == 4) {
                List<Depot> depots = getChildDepots(depot);
                list.addAll(depots);

            }
        }

        //添加当前部门到list
        list.add(depot);
        return list;
    }

    /**
     * 获取直接子部门
     *
     * @param depot
     * @return
     */
    public List<Depot> getChildDepots(Depot depot) {
        List<Depot> depots = new ArrayList<Depot>();
        Map<String, Object> params = new HashMap<>();
        params.put("eqParentId", depot.getDepotId());
        depots = depotMapper.selectByMap(params);
        return depots;

    }

    /**
     * 判断是否为车间或者检测所,获取管理的车间
     */
    public List<Depot> getManageWorkShopDepots(Long depotId) {
        List<Depot> depots = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        Depot depot = depotMapper.selectByPrimaryKey(depotId);
        if (depot.getDepotLevel() == 2) {//机辆所获取所有车间
            params.put("eqDepotLevel", 4);
            depots = depotMapper.selectByMap(params);
        }else if(depot.getDepotLevel() == 3){//车辆段获取下属车间
            params.put("eqParentId", depot.getDepotId());
            depots=depotMapper.selectByMap(params);
        } else if (depot.getDepotLevel() == 4) {//单个车间
            depots.add(depot);
        }
        return depots;
    }

    /**
     * 获取所属车间
     * @param depotId
     * @return
     */
    public Long getWorkShop(Long depotId){
        Map<String, Object> params = new HashMap<>();
        params.put("eqDepotId",depotId);
        Depot depot = depotMapper.selectByPrimaryKey(depotId);
        if(depot.getDepotLevel()==4){
            return depot.getDepotId();
        }else if(depot.getDepotLevel()==5){
           return depot.getParentId();
        }else {
            return null;
        }
    }
}
