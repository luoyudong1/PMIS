package com.kthw.pmis.websocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YFZX-WB on 2017/5/12.
 */
public class TXDSMessageSend<T> {

    private List<T> trainPass;

    private String type;

    public TXDSMessageSend(List<T> trainPass, String type) {
        if (trainPass == null) {
            trainPass = new ArrayList<>();
        }
        this.trainPass = trainPass;
        this.type = type;
    }

    public List<T> getTrainPass() {
        return trainPass;
    }

    public void setTrainPass(List<T> trainPass) {
        this.trainPass = trainPass;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
