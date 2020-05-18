package com.kthw.pmis.helper;

import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.FaultHandle;
import com.kthw.pmis.entiy.PlanCheck;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("faultHandleHelper")
public class FaultHandleHelper {

    public List<FaultHandle> sortNewList(List<FaultHandle> list, Depot depot) {
        short completeFlag1;
        short completeFlag2;
        if(depot==null){
            completeFlag1=2;
            completeFlag2=1;
        }else if(depot.getDepotLevel()==2){
            completeFlag1=2;
            completeFlag2=1;
        } else {
            completeFlag1=1;
            completeFlag2=2;
        }
        List<FaultHandle> newList = new ArrayList<>();
        for (FaultHandle faultHandle : list) {
            Short completeFlag = faultHandle.getCompleteFlag();
            if (completeFlag == completeFlag1 || completeFlag == completeFlag1+2||completeFlag==completeFlag1+4) {
                newList.add(faultHandle);
            }
        }
        for (FaultHandle faultHandle : list) {
            Short completeFlag = faultHandle.getCompleteFlag();
            if (completeFlag == completeFlag2 || completeFlag == completeFlag2+2||completeFlag==completeFlag2+4) {
                newList.add(faultHandle);
            }
        }
        for (FaultHandle faultHandle: list) {
            Short completeFlag = faultHandle.getCompleteFlag();
            if (completeFlag == (short) 7) {
                newList.add(faultHandle);
            }
        }
        return newList;
    }
}
