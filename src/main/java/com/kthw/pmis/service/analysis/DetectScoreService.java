package com.kthw.pmis.service.analysis;

import com.kthw.pmis.entiy.DetectScore;

import java.util.List;

public interface DetectScoreService {
    public void setMonthScore(Integer year,Integer month);
    public List<DetectScore> getMonthScore(Integer year, Integer month);
}
