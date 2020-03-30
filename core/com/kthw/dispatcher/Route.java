package com.kthw.dispatcher;

import java.util.ArrayList;
import java.util.List;

public class Route {
	String nodeStr;
	int nodeNum;
	
	List<Node> nodeList;
	
	public Route() {
		nodeList = new ArrayList<Node>();
	}
	
	public String getNodeStr() {
		return nodeStr;
	}
	public void setNodeStr(String nodeStr) {
		this.nodeStr = nodeStr;
	}
	public int getNodeNum() {
		return nodeNum;
	}
	public void setNodeNum(int nodeNum) {
		this.nodeNum = nodeNum;
	}
	public List<Node> getNodeList() {
		return nodeList;
	}
	public void setNodeList(List<Node> nodeList) {
		this.nodeList = nodeList;
	}
	
}
