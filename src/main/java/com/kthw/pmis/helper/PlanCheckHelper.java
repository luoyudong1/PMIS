package com.kthw.pmis.helper;

import com.kthw.pmis.entiy.Depot;
import com.kthw.pmis.entiy.PlanCheck;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("planCheckHelper")
public class PlanCheckHelper {


    public List<PlanCheck> sortNewList(List<PlanCheck> list, Depot depot) {
        short status1;
        short status2;
        List<PlanCheck> newList = new ArrayList<>();
        if(depot==null||depot.getDepotLevel()==2) {
            for (PlanCheck planCheck : list) {
                Short status = planCheck.getStatus();
                if (status == 3 || status == 5) {
                    newList.add(planCheck);
                }
            }
            for (PlanCheck planCheck : list) {
                Short status = planCheck.getStatus();
                if (status == 2||status == 4) {
                    newList.add(planCheck);
                }
            }
        }
        else {
            for (PlanCheck planCheck : list) {
                Short status = planCheck.getStatus();
                if (status == 4) {
                    newList.add(planCheck);
                }
            }
            for (PlanCheck planCheck : list) {
                Short status = planCheck.getStatus();
                if (status == 2) {
                    newList.add(planCheck);
                }
            }
            for (PlanCheck planCheck : list) {
                Short status = planCheck.getStatus();
                if (status == 3 || status == 5) {
                    newList.add(planCheck);
                }
            }
        }
        for (PlanCheck planCheck : list) {
            Short status = planCheck.getStatus();
            if (status == (short) 6) {
                newList.add(planCheck);
            }

        }
        return newList;
    }
}
