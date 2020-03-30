package com.kthw.pmis.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface SequenceMapper {

    int selectTrainSeq();
    
    public int selectPictureNum();

}
