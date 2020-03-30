package com.kthw.dispatcher;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class Dispatcher {

	List<Node> nodeMap = new ArrayList<Node>();
	List<Route> routeList;
	Route[][] routeArray;
	List<PlatformInfo> platformList;
	List<TaskInfo> taskList;
	List<List<Step>> workStepList;
	private final static String oracleDriverName = "oracle.jdbc.driver.OracleDriver";
	static Connection myConnection = null;
	String sql = "";

	static final int MAX_BACKUP_SIZE = 15;
	static final int MAX_NODE_NUM = 120;
	static final int MAX_TRACK_NUM = 21;

	public void InitDriver() {
		try {
			Class.forName(oracleDriverName);
		} catch (ClassNotFoundException ex) {
			System.out.println("加载jdbc驱动失败,原因:" + ex.getMessage());
		}
	}

	public void InitConnection() {
		try {
			String strUser = "tyxx";
			String strPassword = "tyxx";
			String oracleUrlToConnect = "jdbc:oracle:thin:@192.168.3.211:1521:tyxx";
			myConnection = DriverManager.getConnection(oracleUrlToConnect, strUser, strPassword);
		} catch (Exception ex) {
			System.out.println("Can not get connection:" + ex.getMessage());
			ex.printStackTrace();
			System.out.println("请检测配置文件中的数据库信息是否正确.");
		}
	}

	public void InitMap() {
		if (myConnection == null)
			InitConnection();
		try {
			Statement stmt = myConnection.createStatement();
			ResultSet myResultSet = stmt.executeQuery("select * from tbl_node_map order by node_id asc");
			nodeMap = new ArrayList<Node>();
			while (myResultSet.next()) {
				int node_id = myResultSet.getInt("node_id");
				int track_id = myResultSet.getInt("track_id");
				int garage_id = myResultSet.getInt("garage_id");
				int platform_id = myResultSet.getInt("platform_id");
				int is_block = myResultSet.getInt("is_block");
				int block_node = myResultSet.getInt("block_node");
				int is_outdoor = myResultSet.getInt("is_outdoor");
				int is_branch = myResultSet.getInt("is_branch");
				int is_stop = myResultSet.getInt("is_stop");
				String left_nodes = myResultSet.getString("left_nodes");
				String right_nodes = myResultSet.getString("right_nodes");
				Node node = new Node();
				node.setNode_id(node_id);
				node.setTrack_id(track_id);
				node.setPlatform_id(platform_id);
				node.setGarage_id(garage_id);
				node.setIs_block(is_block);
				node.setBlock_node(block_node);
				node.setIs_outdoor(is_outdoor);
				node.setIs_branch(is_branch);
				node.setIs_stop(is_stop);
				if (left_nodes != null && !"".equals(left_nodes)) {
					String[] temp = left_nodes.split(";");
					int[] nodes = new int[temp.length];
					for (int i = 0; i < temp.length; i++) {
						nodes[i] = Integer.parseInt(temp[i]);
					}
					node.setLeft_nodes(nodes);
				} else {
					node.setLeft_nodes(new int[] { 0, 0 });
				}
				if (right_nodes != null && !"".equals(right_nodes)) {
					String[] temp = right_nodes.split(";");
					int[] nodes = new int[temp.length];
					for (int i = 0; i < temp.length; i++) {
						nodes[i] = Integer.parseInt(temp[i]);
					}
					node.setRight_nodes(nodes);
				} else {
					node.setRight_nodes(new int[] { 0, 0 });
				}
				nodeMap.add(node);
			}
			myConnection.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public static void main(String[] args) {
		Dispatcher dis = new Dispatcher();
		dis.InitDriver();
		dis.InitConnection();
		dis.InitMap();
		dis.InitRoute();
		dis.ExcuteTask();
	}

	private void ExcuteTask() {
		try {
			if (myConnection == null || myConnection.isClosed())
				InitConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Statement stmt = myConnection.createStatement();
			ResultSet myResultSet = stmt.executeQuery("select * from tbl_vehplatform_info order by node_id asc");
			platformList = new ArrayList<PlatformInfo>();
			taskList = new ArrayList<TaskInfo>();
			while (myResultSet.next()) {
				int node_id = myResultSet.getInt("node_id");
				int track_id = myResultSet.getInt("track_id");
				int platform_id = myResultSet.getInt("platform_id");
				int target_track = myResultSet.getInt("target_track");
				int target_platform = myResultSet.getInt("target_platform");
				int target_garage = myResultSet.getInt("target_garage");
				int platform_type = myResultSet.getInt("platform_type");
				Date update_time = myResultSet.getDate("update_time");
				String vehicle_id = myResultSet.getString("vehicle_id");
				String vehicle_type = myResultSet.getString("vehicle_type");
				String remarks = myResultSet.getString("remarks");
				PlatformInfo platform = new PlatformInfo();
				platform.setNode_id(node_id);
				platform.setTrack_id(track_id);
				platform.setPlatform_id(platform_id);
				platform.setVehicle_id(vehicle_id);
				platform.setVehicle_type(vehicle_type);
				platform.setRemarks(remarks);
				platform.setTarget_track(target_track);
				platform.setTarget_garage(target_garage);
				platform.setTarget_platform(target_platform);
				platform.setPlatform_type(platform_type);
				platform.setUpdate_time(update_time);
				platformList.add(platform);
				System.out.println(node_id + "," + target_garage + " " + target_track + " " + target_platform);
				if (target_garage + target_platform + target_track > 0) {
					TaskInfo task = new TaskInfo();
					task.setPlatform(platform);
					taskList.add(task);
				}
				Node node = GetNodeInMap(node_id);
				node.setVehicle_id(vehicle_id);
				if (vehicle_id != null && !"".equals(vehicle_id)) {// 有车的话更新标志位
					node.setIs_empty(0);
				} else {
					node.setIs_empty(1);
				}
			}
			myConnection.commit();
			myConnection.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		/*
		 * 调车步骤生成规则 By chengliang 2018-06-11 0.首先根据条件计算出唯一的目标台位，针对目标是车库或股道的情况
		 * 1.计算单个车辆的调车最短路径，直接调用事先记录好的路径二维数组
		 * 2.根据目标股道号和台位将所有调度车辆归类，同一股道划入一类，台位号大者排在前面，表示优先挂车
		 * 3.开始动态调车（移动中需要动态考虑股道结点的左移值和冲突台位）
		 * 3.1第一步，从字典步骤中选择一种，因为调车任务分为三部分，[20道入库检修车/出库车/倒库车（明日出库）]
		 * 3.2第二步，根据机后尾部车号选择下步动作，有挂车和甩车两个动作。一般来说没有相近目标股道时不再挂车，甩车需要直接入位或甩在缓冲区
		 * 3.3根据以上规则遍历当前所有可能的甩挂分支，并进行适当的剪枝，动态模拟，直到达到目标状态。剪枝标准：
		 * 3.3.1可以深度搜索5层（或更多），进行状态评估，利用每辆车距离目标位置的距离总和来衡量
		 * 3.3.2在固定搜索层数内，选择评估值最小的前N种情况，舍弃其他分支 4.生成最优调车步骤并记录
		 */
		// calculate signle steps
		if (taskList != null) {
			for (int i = 0; i < taskList.size(); i++) {
				Route route = calcStepForOneMove(taskList.get(i).getPlatform());
				taskList.get(i).setRoute(route);
				System.out.println("Task " + (i + 1) + ":[" + taskList.get(i).getPlatform().getNode_id() + ","
						+ route.getNodeList().get(route.getNodeList().size() - 1).getNode_id() + "]"
						+ route.getNodeStr());
			}
		}
		// merge route by target track
		TaskInfo[][] trackDest = new TaskInfo[MAX_TRACK_NUM][15];
		int[] track_index = new int[MAX_TRACK_NUM];
		for (int i = 0; i < MAX_TRACK_NUM; i++) {// init track node display
			track_index[i] = 0;
		}
		for (int i = 0; i < taskList.size(); i++) {
			TaskInfo task = taskList.get(i);
			trackDest[task.getPlatform().getTarget_track()][track_index[task.getPlatform().getTarget_track()]++] = task;
		}
		// sort task sequence by platform order
		for (int i = 0; i < MAX_TRACK_NUM; i++) {
			for (int j = 0; j < track_index[i]; j++) {
				for (int k = j + 1; k < track_index[i]; k++) {
					if (trackDest[i][j].getPlatform().getTarget_platform() < trackDest[i][k].getPlatform()
							.getTarget_platform()) {
						TaskInfo temp = trackDest[i][j];
						trackDest[i][j] = trackDest[i][k];
						trackDest[i][k] = temp;
					}
				}
			}
		} // sort task over
		workStepList = new ArrayList<List<Step>>();
		List<Step> steps = new ArrayList<Step>();
		List<PlatformInfo> locomotiveBackNodes = new ArrayList<PlatformInfo>();
		GenerateSteps(locomotiveBackNodes, steps, trackDest, track_index, 1);
		return;
	}

	private void GenerateSteps(List<PlatformInfo> locomotive, List<Step> steps, TaskInfo[][] track, int[] track_index, int seq) {
		// 寻找可用的最长临停股道
		updateNodeLeftAvaliableNum(nodeMap);
		int lastPos = 0;
		// 计算需要变换的股道号
		List<Integer> trackMoveList = new ArrayList<Integer>();
		for (int i = 0; i < MAX_TRACK_NUM; i++) {
			if (track_index[i] > 0) {
				trackMoveList.add(i);
			}
		}
		//先出20道的车
		int start_track = 20;//trackMoveList.get(0);
		for (int i = 0; i < track_index[start_track]; i++) {
			Step step = new Step();
			step.setSeq(seq);
			step.setTrack_id(track[start_track][i].getPlatform().getTrack_id());
			step.setNum(1);
			step.setOperation(1);
			step.setRemain(locomotive.size() + 1);
			steps.add(step);
			locomotive.add(track[start_track][i].getPlatform());
			GetNodeInMap(track[start_track][i].getPlatform().getNode_id()).setIs_empty(1);
			lastPos = track[start_track][i].getPlatform().getNode_id();
		}
		Node lastNode = GetNodeInMap(lastPos);
		track_index[start_track] = 0;
		//检查是否可以直接停入目标股道，否则就找一个位置放下当前挂的车，把目标股道腾出来
		updateMapWithLocomotive(locomotive, lastNode);
	}

	private void updateMapWithLocomotive(List<PlatformInfo> locomotive, Node lastNode) {
		
	}

	private void updateNodeLeftAvaliableNum(List<Node> nodeMap2) {
		for (int i = 0; i < nodeMap2.size(); i++) {
			Node node = nodeMap2.get(i);
			if (node.getLeft_nodes()[0] == 0 && node.getLeft_nodes()[1] == 0) {
				node.setLeftAvaliableNodeNum(1);
			} else {
				if (node.getIs_branch() == 1) {
					node.setLeftAvaliableNodeNum(MaxLeft(node));
				} else {
					if (node.getIs_empty() == 1) {
						node.setLeftAvaliableNodeNum(MaxLeft(node) + 1);
					} else {
						node.setLeftAvaliableNodeNum(0);
					}
				}
			}
		}
	}

	private int MaxLeft(Node node) {
		int len = 0;
		if (node.getLeft_nodes() != null) {
			for (int i = 0; i < node.getLeft_nodes().length; i++) {
				Node temp = GetNodeInMap(node.getLeft_nodes()[i]);
				if (temp != null && temp.getLeftAvaliableNodeNum() > len) {
					len = temp.getLeftAvaliableNodeNum();
				}
			}
		}
		return len;
	}

	private Route calcStepForOneMove(PlatformInfo task) {
		int node_start = task.getNode_id();
		int[] node_end = new int[MAX_BACKUP_SIZE];
		if (task.getTarget_platform() != 0) {
			node_end[0] = GetNodeByTrackAndPlatform(task.getTarget_track(), task.getTarget_platform()).getNode_id();
		} else if (task.getTarget_track() != 0) {
			for (int i = 0, j = 0; i < nodeMap.size(); i++) {
				Node temp = nodeMap.get(i);
				if (temp.getTrack_id() == task.getTarget_track()) {
					if ((temp.getIs_empty() == 1 && NodeIsNotEnd(temp.getNode_id()))
							|| (temp.getIs_empty() == 0 && NodeIsStart(temp.getNode_id()))) {
						node_end[j++] = temp.getNode_id();
					}
				}
			}
		} else {
			for (int i = 0, j = 0; i < nodeMap.size(); i++) {
				Node temp = nodeMap.get(i);
				if (temp.getGarage_id() == task.getTarget_garage()) {
					if ((temp.getIs_empty() == 1 && NodeIsNotEnd(temp.getNode_id()))
							|| (temp.getIs_empty() == 0 && NodeIsStart(temp.getNode_id()))) {
						node_end[j++] = temp.getNode_id();
					}
				}
			}
		} // if(task.getTarget_platform() != 0) {
		return routeArray[node_start][node_end[0]];
	}

	private boolean NodeIsStart(int node_id) {
		for (int i = 0; i < taskList.size(); i++) {
			Node node = GetNodeByTrackAndPlatform(taskList.get(i).getPlatform().getTrack_id(),
					taskList.get(i).getPlatform().getPlatform_id());
			if (node != null && node.getNode_id() == node_id) {
				return false;
			}
		}
		return false;
	}

	private boolean NodeIsNotEnd(int node_id) {
		for (int i = 0; i < taskList.size(); i++) {
			Node node = GetNodeByTrackAndPlatform(taskList.get(i).getPlatform().getTarget_track(),
					taskList.get(i).getPlatform().getTarget_platform());
			if (node != null && node.getNode_id() == node_id) {
				return false;
			}
		}
		return true;
	}

	private Node GetNodeByTrackAndPlatform(int track, int platform) {
		for (int i = 0; i < nodeMap.size(); i++) {
			if (nodeMap.get(i).getTrack_id() == track && nodeMap.get(i).getPlatform_id() == platform) {
				return nodeMap.get(i);
			}
		}
		return null;
	}

	private void InitRoute() {
		String nodes = "1";
		routeList = new ArrayList<Route>();
		routeArray = new Route[MAX_NODE_NUM][MAX_NODE_NUM];
		BFS_ALL_ROUTE(1, nodes, 1);
		for (int i = 0; i < routeList.size(); i++) {
			System.out.println(routeList.get(i).getNodeStr());
			String[] temp = routeList.get(i).getNodeStr().split(";");
			routeList.get(i).getNodeList().clear();
			for (int j = 0; j < temp.length; j++) {
				Node node = GetNodeInMap(Integer.parseInt(temp[j]));
				routeList.get(i).getNodeList().add(node);
			}
		}
		//
		for (int i = 0; i < nodeMap.size(); i++) {
			for (int j = 0; j < nodeMap.size(); j++) {
				if (i == j)
					continue;
				routeList.clear();
				nodes = String.valueOf(nodeMap.get(i).getNode_id());
				BFS_NODES_DISTANCE(nodeMap.get(i).getNode_id(), nodeMap.get(j).getNode_id(), nodes, 0);
				Collections.sort(routeList, new Comparator<Route>() {
					@Override
					public int compare(Route r1, Route r2) {
						return r1.getNodeNum() - r2.getNodeNum();
					}
				});
				String[] temp = routeList.get(0).getNodeStr().split(";");
				routeList.get(0).getNodeList().clear();
				for (int k = 0; k < temp.length; k++) {
					Node node = GetNodeInMap(Integer.parseInt(temp[k]));
					routeList.get(0).getNodeList().add(node);
				}
				routeArray[nodeMap.get(i).getNode_id()][nodeMap.get(j).getNode_id()] = routeList.get(0);
			}
		}
		System.out.println("InitRoute success!");
	}

	private void BFS_NODES_DISTANCE(int id1, int id2, String nodes, int len) {
		if (id1 == id2) {
			Route r = new Route();
			r.setNodeStr(nodes);
			r.setNodeNum(len);
			routeList.add(r);
			return;
		}
		Node node1 = GetNodeInMap(id1);
		if (node1.left_nodes != null) {
			for (int i = 0; i < node1.left_nodes.length; i++) {
				if (node1.left_nodes[i] > 0 && !Visited(nodes, node1.left_nodes[i])) {
					BFS_NODES_DISTANCE(node1.left_nodes[i], id2, nodes + ";" + node1.left_nodes[i], len + 1);
				}
			}
		}
		if (node1.right_nodes != null) {
			for (int i = 0; i < node1.right_nodes.length; i++) {
				if (node1.right_nodes[i] > 0 && !Visited(nodes, node1.right_nodes[i])) {
					BFS_NODES_DISTANCE(node1.right_nodes[i], id2, nodes + ";" + node1.right_nodes[i], len + 1);
				}
			}
		}
	}

	private boolean Visited(String nodes, int id) {
		String[] nodesStr = nodes.split(";");
		String idStr = String.valueOf(id);
		for (int i = 0; i < nodesStr.length; i++) {
			if (nodesStr[i].equals(idStr)) {
				return true;
			}
		}
		return false;
	}

	private void BFS_ALL_ROUTE(int id, String nodes, int len) {
		Node node = GetNodeInMap(id);
		int flag = 0;
		for (int i = 0; i < node.getRight_nodes().length; i++) {
			int temp_id = node.getRight_nodes()[i];
			if (temp_id != 0) {
				String temp_str = nodes + ";" + temp_id;
				BFS_ALL_ROUTE(temp_id, temp_str, len + 1);
			} else {
				flag++;
			}
		}
		if (flag == 2) {
			Route route = new Route();
			route.setNodeStr(nodes);
			route.setNodeNum(len);
			routeList.add(route);
		}
	}

	private Node GetNodeInMap(int id) {
		for (int i = 0; i < nodeMap.size(); i++) {
			if (nodeMap.get(i).getNode_id() == id) {
				return nodeMap.get(i);
			}
		}
		return null;
	}
}
