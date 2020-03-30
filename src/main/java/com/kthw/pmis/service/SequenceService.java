package com.kthw.pmis.service;

public interface SequenceService {

	/**
	 * 返回int型列车序列
	 * */
	public int getTrainSeq();
	
	/***
	 * 返回String型列车序列
	 * 如果序列是16位，则方法为：
	 *     日期+序列
	 *     日期格式：yyyymmdd
	 *     序列格式：查询获得序列值最长8位，99999999，转换格式之后，不足8位前面补零
	 * */
	public String getTrainSeqStr();
	
	/**
     * 查询图片编号
     * */
    public int selectPictureNum();

}
