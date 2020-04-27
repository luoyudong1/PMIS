package com.kthw.pmis.serviceimpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kthw.common.DateConvertor;
import com.kthw.common.Utils;
import com.kthw.pmis.mapper.SequenceMapper;
import com.kthw.pmis.service.SequenceService;

@Service
public class SequenceServiceImpl implements SequenceService {

	
	@Autowired
	private SequenceMapper sequenceMapper;
	

	public int getTrainSeq(){
		return sequenceMapper.selectTrainSeq();
	}
	
	public String getTrainSeqStr(){
		String pattern = "YYYYMMDD";
		int seq = sequenceMapper.selectTrainSeq();//获得序列
		String strseq = Utils.makeId(seq,8);//补足8位
		return DateConvertor.getTimeString(pattern)+strseq;//当前时间+序列
	}

	@Override
	public int selectPictureNum() {
		return sequenceMapper.selectPictureNum();
	}
	
}
