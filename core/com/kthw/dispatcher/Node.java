package com.kthw.dispatcher;

public class Node {
	final static int MAX_BLOCK_NODES_NUM = 5;
	final static int MAX_SIDE_NODES_NUM = 2;
	int node_id;
	int is_empty;
	String vehicle_id;
	int garage_id;
	int platform_id;
	int track_id;
	int is_block;
	int block_node;
	int is_outdoor;
	int is_branch;
	int is_stop;//是否为临停节点
	int[] left_nodes;//0，表示没有
	int[] right_nodes;//0，表示没有
	
	int leftAvaliableNodeNum;
	
	public Node() {
		is_empty = 1;
		vehicle_id = "";
		track_id = 0;
		is_block = 0;
		block_node = 0;
		is_stop = 0;
		is_outdoor = 0;
		is_branch = 0;
		left_nodes = new int[MAX_SIDE_NODES_NUM];
		right_nodes = new int[MAX_SIDE_NODES_NUM];
		for(int i = 0; i < MAX_SIDE_NODES_NUM; i ++) {
			left_nodes[i] = right_nodes[i] = 0;
		}
		leftAvaliableNodeNum = 0;
	}
	public void Init() {
		
	}
	public int getNode_id() {
		return node_id;
	}
	public void setNode_id(int node_id) {
		this.node_id = node_id;
	}
	public int getIs_empty() {
		return is_empty;
	}
	public void setIs_empty(int is_empty) {
		this.is_empty = is_empty;
	}
	public int getTrack_id() {
		return track_id;
	}
	public void setTrack_id(int track_id) {
		this.track_id = track_id;
	}
	public int getIs_block() {
		return is_block;
	}
	public void setIs_block(int is_block) {
		this.is_block = is_block;
	}
	public int getBlock_node() {
		return block_node;
	}
	public void setBlock_node(int block_node) {
		this.block_node = block_node;
	}
	public int getIs_outdoor() {
		return is_outdoor;
	}
	public void setIs_outdoor(int is_outdoor) {
		this.is_outdoor = is_outdoor;
	}
	public int getIs_branch() {
		return is_branch;
	}
	public void setIs_branch(int is_branch) {
		this.is_branch = is_branch;
	}
	public int getIs_stop() {
		return is_stop;
	}
	public void setIs_stop(int is_stop) {
		this.is_stop = is_stop;
	}
	public int[] getLeft_nodes() {
		return left_nodes;
	}
	public void setLeft_nodes(int[] left_nodes) {
		this.left_nodes = left_nodes;
	}
	public int[] getRight_nodes() {
		return right_nodes;
	}
	public void setRight_nodes(int[] right_nodes) {
		this.right_nodes = right_nodes;
	}
	public int getLeftAvaliableNodeNum() {
		return leftAvaliableNodeNum;
	}
	public void setLeftAvaliableNodeNum(int leftAvaliableNodeNum) {
		this.leftAvaliableNodeNum = leftAvaliableNodeNum;
	}
	public int getGarage_id() {
		return garage_id;
	}
	public void setGarage_id(int garage_id) {
		this.garage_id = garage_id;
	}
	public int getPlatform_id() {
		return platform_id;
	}
	public void setPlatform_id(int platform_id) {
		this.platform_id = platform_id;
	}
	public String getVehicle_id() {
		return vehicle_id;
	}
	public void setVehicle_id(String vehicle_id) {
		this.vehicle_id = vehicle_id;
	}
}
