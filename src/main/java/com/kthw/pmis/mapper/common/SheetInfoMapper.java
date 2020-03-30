package com.kthw.pmis.mapper.common;

import com.kthw.pmis.entiy.SheetInfo;
import com.kthw.pmis.entiy.dto.SheetDetailDTO;
import com.kthw.pmis.entiy.ext.SheetInfoExt;

import java.util.List;
import java.util.Map;

public interface SheetInfoMapper {
    int deleteByPrimaryKey(String sheetId);

    int insert(SheetInfo record);

    int insertSelective(SheetInfo record);

    SheetInfo selectByPrimaryKey(String sheetId);

    List<SheetInfoExt> selectByMap(Map<String, Object> params);

    int updateByPrimaryKeySelective(SheetInfo record);

    int updateByPrimaryKey(SheetInfo record);

    int updateByMap(Map<String, Object> params);
    
    int getSheetParamCount(Map<String, Object> params);// 根据条件获取单据总数
    
    List<SheetInfoExt> getAllSheets(Map<String, Object> params);// 根据条件获取单据
    //获取未接收的单据数量
    int getNotReceiptSheetInfo(Map<String, Object> params);
    //获取未审核的单据数量
    int getNotVerifySheetInfo(Map<String, Object> params);
    
    }