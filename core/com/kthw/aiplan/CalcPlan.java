package com.kthw.aiplan;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

public class CalcPlan {
	static Logger logger = Logger.getLogger(CalcPlan.class);
	private final static String oracleDriverName = "oracle.jdbc.driver.OracleDriver";
	static Connection myConnection = null;
	String dbip,dbport,dbname,dbuser,dbpwd;
	String sql = "";

	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	DateFormat formatAll = new SimpleDateFormat("yyyyMMddHHmmss");
	DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

	List<VehicleInfo> vehList;
	List<DynamicVehInfo> dynamicVehList;
	Map<Integer, List<DynamicVehInfo>> vehGroupMap;
	List<GroupType> groupTypeList;
	Map<String, Integer> dynamicMap;
	Map<Integer, List<Integer>> groupMap;
	Map<Integer, List<Integer>> groupMapAll;

	List<DynamicVehInfo> dynamicGroupedVehList;
	Map<Integer, List<Integer>> groupedMap;
	Map<String, Integer> dynamicGroupedMap;

	int global_grouped_id_seq = 1;

	final String date_zero = "1990-12-31";

	public static void main(String[] args) {
		CalcPlan plan = new CalcPlan();
		plan.readSetting();
		plan.InitDriver();
		plan.InitConnection();

		int total_times = 1;
		plan.CalcUtilizing();
		for (int k = 1; k <= 3; k++) {// k是车型，仅仅计算硬卧/硬座和软卧
			// 优先从在运用的车辆中寻找连续三辆的编成一组
			 plan.Calc(0, 1, k, total_times++);
			 int start = 2, end = 9;
			 for (int i = start; i <= end; i++) {
			 plan.Calc(1, i, k, total_times++);
			 }
			 for (int i = start; i <= end; i++) {
			 plan.Calc(2, -i, k, total_times++);
			 }
			 // 处理遗漏情况，合并交叉分组2018.08.10,将单个车辆合并到已成组2018.08.15
			 plan.CalcOmitted(k);
			// 进行深层次分组，不要求所有参数一致
		}
	}
	
	public void InitConnection() {
		try {
			String strUser = dbuser;//"tyxx";
			String strPassword = dbpwd;//"tyxx";
//			String oracleUrlToConnect = "jdbc:oracle:thin:@192.168.31.122:1521:kthw"; //local
//			String oracleUrlToConnect = "jdbc:oracle:thin:@192.168.3.211:1521:tyxx"; // 211
			String oracleUrlToConnect = "jdbc:oracle:thin:@"+dbip+":"+dbport+":"+dbname+""; // 现场数据库
			myConnection = DriverManager.getConnection(oracleUrlToConnect, strUser, strPassword);
		} catch (Exception ex) {
			System.out.println("Can not get connection:" + ex.getMessage());
			ex.printStackTrace();
			System.out.println("请检测配置文件中的数据库信息是否正确.");
		}
	}

	
	private void readSetting() {
		File iniFile = new File("config.ini");
		if (!iniFile.exists()) {
			logger.error("configuration file not exist.");
			System.exit(-1);
			return;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(iniFile));
			String line = null;
			String[] parts;
			while ((line = reader.readLine()) != null) {
				line = line.trim();
				if (line.startsWith("#") || line.startsWith(";") || line.startsWith("[")) {
					continue;
				} else {
					parts = line.split("=");
					if (parts.length == 2) {
						String key = parts[0].trim();
						String value = parts[1].trim();
						if ("LocalDbIp".equals(key)) {
							dbip = value;
						} else if ("LocalDbPort".equals(key)) {
							dbport = (value);
						} else if ("LocalDbName".equals(key)) {
							dbname = value;
						} else if ("LocalDbUser".equals(key)) {
							dbuser = value;
						} else if ("LocalDbPwd".equals(key)) {
							dbpwd = value;
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException ex1) {
				}
			}
		}
	}
	
	
	private void CalcUtilizing() {
		try {
			if (myConnection == null || myConnection.isClosed())
				InitConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			Statement stmt = myConnection.createStatement();
			ResultSet myResultSet;
			myResultSet = stmt.executeQuery("select * from TBL_TRAIN_INFO t where t.start_date is not null");
			List<List<String>> trainList = new ArrayList<List<String>>();
			while (myResultSet.next()) {
				String trains = myResultSet.getString("trains");
				if (trains != null) {
					String[] temp = trains.split(";");
					List<String> list = new ArrayList<String>();
					for (int i = 0; i < temp.length; i++) {
						list.add(temp[i]);
					}
					trainList.add(list);
				}
			}

			myResultSet = stmt.executeQuery("select v.*,d.* from tbl_vehicle v,tbl_dynamic_vehicle d "
					+ "where v.veh_id = d.vehicle_id and d.grouped_status = " + 0 + " order by v.veh_id asc");
			dynamicVehList = new ArrayList<DynamicVehInfo>();
			dynamicMap = new HashMap<String, Integer>();
			Date date0 = new Date();
			try {
				date0 = formatDate.parse(date_zero);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			int seq = 0;
			while (myResultSet.next()) {
				DynamicVehInfo veh = new DynamicVehInfo();
				veh.setVehicle_id(myResultSet.getString("vehicle_id"));
				veh.setApplication_state(myResultSet.getString("application_state"));
				veh.setCurrent_unit(myResultSet.getString("current_unit"));
				veh.setRoad_traffic(myResultSet.getString("road_traffic"));
				veh.setRoute(myResultSet.getString("route"));
				veh.setAverage_daymile(myResultSet.getInt("average_daymile"));
				veh.setTotal_mile(myResultSet.getInt("total_mile"));
				veh.setVehicle_order(myResultSet.getInt("vehicle_order"));
				veh.setStart_date(myResultSet.getDate("start_date"));
				veh.setArrival_date(myResultSet.getDate("arrival_date"));
				veh.setUpdate_time(myResultSet.getDate("update_time"));
				veh.setGroup_id(myResultSet.getInt("group_id"));
				veh.setGrouptype_id(myResultSet.getInt("grouptype_id"));
				veh.setTrain_number(myResultSet.getString("train_number"));
				veh.setStatus(myResultSet.getInt("status"));
				veh.setA1_expire(myResultSet.getString("a1_time") + "-01");
				veh.setA2_expire(myResultSet.getString("a2_time") + "-01");
				veh.setA3_expire(myResultSet.getString("a3_time") + "-01");
				veh.setA4_expire(myResultSet.getString("a4_time") + "-01");
				veh.setA5_expire(myResultSet.getString("a5_time") + "-01");
				veh.setA1_distance(myResultSet.getInt("a1_distance"));
				veh.setA2_distance(myResultSet.getInt("a2_distance"));
				veh.setA3_distance(myResultSet.getInt("a3_distance"));
				veh.setA4_distance(myResultSet.getInt("a4_distance"));
				veh.setA5_distance(myResultSet.getInt("a5_distance"));
				try {
					veh.setAx_expire_date(2, formatDate.parse(veh.getA2_expire()));
					veh.setAx_expire_date(3, formatDate.parse(veh.getA3_expire()));
					long day = 0;
					for (int s = 2; s <= 3; s++) {
						day = (veh.getAx_expire_date(s).getTime() - date0.getTime()) / (24 * 60 * 60 * 1000);
						veh.setAx_expire_day(s, day);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				veh.setAx_mile(1, veh.getA1_distance());
				veh.setAx_mile(2, veh.getA2_distance());
				veh.setAx_mile(3, veh.getA3_distance());
				veh.setAx_mile(4, veh.getA4_distance());
				veh.setAx_mile(5, veh.getA5_distance());
				veh.setWheel_last_fix_miles(myResultSet.getInt("wheel_last_fix_miles"));
				veh.setWheel_last_fix_time(myResultSet.getString("wheel_last_fix_time"));
				veh.setDifference_value(myResultSet.getInt("difference_value"));
				dynamicVehList.add(veh);
				dynamicMap.put(veh.getVehicle_id(), seq);// 记录根据vehid寻找所在dynamicVehList索引
				seq++;
			}

			for (int i = 0; i < trainList.size(); i++) {
				int veh_num = trainList.get(i).size();
				for (int j = 0; j < veh_num - 1;) {// 减1表示至少2辆成组
					if (!dynamicMap.containsKey(trainList.get(i).get(j))) {
						j ++;
						continue;
					}
					DynamicVehInfo veh = dynamicVehList.get(dynamicMap.get(trainList.get(i).get(j)));
					int k = j + 1;
					while (true) {
						System.out.println("i,k = " + i + "," + k);
						if (dynamicMap.containsKey(trainList.get(i).get(k))) {
							DynamicVehInfo temp = dynamicVehList.get(dynamicMap.get(trainList.get(i).get(k)));
							if (CheckSimilar(veh, temp, 1)) {
								k++;
								if(k >= veh_num) {
									break;
								}
							} else {
								break;
							}
						}else {
							break;
						}
					}
					// 从j到k-1都是一组
					if (k - 1 > j) {// 至少两个一组
						int rand = (int) (Math.random() * 10000000);
						for (int s = j; s < k; s ++) {
							dynamicVehList.get(dynamicMap.get(trainList.get(i).get(s))).setGrouped_id(rand);
						}
					}
					j = k;
				}
			}

			String sql = "update tbl_dynamic_vehicle set grouped_id = ?,grouped_status = ?,grouped_time = to_date(?,'yyyy-mm-dd hh24:mi:ss') where vehicle_id = ?";
			PreparedStatement pstmt = myConnection.prepareStatement(sql);
			for (int i = 0; i < dynamicVehList.size(); i++) {
				DynamicVehInfo veh = dynamicVehList.get(i);
				if (veh.getGrouped_id() != 0) {
					pstmt.setInt(1, veh.getGrouped_id());
					pstmt.setInt(2, 3);
					pstmt.setString(3, format.format(new Date()));
					pstmt.setString(4, veh.getVehicle_id());
					pstmt.executeQuery();
				}
			}
			pstmt.close();
			myConnection.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	/*
	 * 计算分组入口函数 status：表示grouped_status的值，表示进计算当前状态的车
	 * times：次数，表示当前计算是第几次，用以调整阈值，正值扩大阈值，负值缩小阈值 class_id：车辆类型，123分别代表硬卧/硬座和软卧
	 * total_times：总次数，用以对车辆grouped_id区分，作为前缀
	 */
	private void Calc(int status, int times, int class_id, int total_times) {
		initList(status, class_id, false);
		vehGroupMap = new HashMap<Integer, List<DynamicVehInfo>>();
		System.out.println(dynamicVehList.size());
		for (int i = 0; i < dynamicVehList.size(); i++) {
			if (vehGroupMap.containsKey(dynamicVehList.get(i).getGrouptype_id())) {
				vehGroupMap.get(dynamicVehList.get(i).getGrouptype_id()).add(dynamicVehList.get(i));
			} else {
				List<DynamicVehInfo> list = new ArrayList<DynamicVehInfo>();
				list.add(dynamicVehList.get(i));
				vehGroupMap.put(dynamicVehList.get(i).getGrouptype_id(), list);
			}
		}
		groupMapAll = new HashMap<Integer, List<Integer>>();
		for (Entry<Integer, List<DynamicVehInfo>> entry : vehGroupMap.entrySet()) {
			DeepGrouping(entry.getValue(), entry.getKey(), class_id, times, total_times);
		}
		updateGroupedIds(class_id);
		System.out.println("Calc[status=" + status + ",class_id=" + class_id + ",times=" + times + "] end!");
	}

	/*
	 * 计算分组补充函数，处理前面分组时要求同组内车辆各参数都在同一区间时，导致划分交叉的情况 class_id：车辆类型，123分别代表硬卧/硬座和软卧
	 */
	private void CalcOmitted(int class_id) {
		// 首先做零散车辆与已分组车辆的合并
		initList(1, class_id, true);
		vehGroupMap = new HashMap<Integer, List<DynamicVehInfo>>();
		System.out.println(dynamicGroupedVehList.size());
		for (int i = 0; i < dynamicGroupedVehList.size(); i++) {
			if (vehGroupMap.containsKey(dynamicGroupedVehList.get(i).getGrouped_id())) {
				vehGroupMap.get(dynamicGroupedVehList.get(i).getGrouped_id()).add(dynamicGroupedVehList.get(i));
			} else {
				List<DynamicVehInfo> list = new ArrayList<DynamicVehInfo>();
				list.add(dynamicGroupedVehList.get(i));
				vehGroupMap.put(dynamicGroupedVehList.get(i).getGrouped_id(), list);
			}
		}
		for (int i = 0; i < dynamicVehList.size(); i++) {
			DynamicVehInfo single = dynamicVehList.get(i);
			boolean flag = false;
			int grouped_id = 0;
			for (Entry<Integer, List<DynamicVehInfo>> entry : vehGroupMap.entrySet()) {
				List<DynamicVehInfo> list = entry.getValue();
				if (list.get(0).getGroup_id() != single.getGroup_id()) {
					continue;
				}
				boolean flag_find = true;
				for (int j = 0; j < list.size(); j++) {
					if (Math.abs(single.getAx_expire_day(2) - list.get(j).getAx_expire_day(2)) > Const.DATE_THRESHOLD) {
						flag_find = false;
						break;
					}
					if (Math.abs(single.getAx_expire_day(3) - list.get(j).getAx_expire_day(3)) > Const.DATE_THRESHOLD) {
						flag_find = false;
						break;
					}
					if (Math.abs(single.getAx_mile(2) - list.get(j).getAx_mile(2)) > Const.MILE_THRESHOLD) {
						flag_find = false;
						break;
					}
					if (Math.abs(single.getAx_mile(3) - list.get(j).getAx_mile(3)) > Const.MILE_THRESHOLD) {
						flag_find = false;
						break;
					}
					if (Math.abs(single.getWheel_last_fix_miles()
							- list.get(j).getWheel_last_fix_miles()) > Const.WHEEL_FIX_THRESHOLD) {
						flag_find = false;
						break;
					}
				}
				if (flag_find) {// find one group
					flag = true;
					grouped_id = entry.getKey();
					break;
				}
			}
			//
			if (flag) {
				single.setGrouped_id(grouped_id);
			}
		}
		updateGroupIdDirectly(class_id);

		// 然后做零散车辆之间合并
		initList(1, class_id, false);
		vehGroupMap = new HashMap<Integer, List<DynamicVehInfo>>();
		System.out.println(dynamicVehList.size());
		for (int i = 0; i < dynamicVehList.size(); i++) {
			if (vehGroupMap.containsKey(dynamicVehList.get(i).getGrouptype_id())) {
				vehGroupMap.get(dynamicVehList.get(i).getGrouptype_id()).add(dynamicVehList.get(i));
			} else {
				List<DynamicVehInfo> list = new ArrayList<DynamicVehInfo>();
				list.add(dynamicVehList.get(i));
				vehGroupMap.put(dynamicVehList.get(i).getGrouptype_id(), list);
			}
		}
		groupMapAll = new HashMap<Integer, List<Integer>>();
		for (Entry<Integer, List<DynamicVehInfo>> entry : vehGroupMap.entrySet()) {
			MergeGrouping(entry.getValue(), entry.getKey(), class_id);
		}
		updateGroupId(class_id);
		System.out.println("CalcOmitted[status=" + 1 + ",class_id=" + class_id + "] end!");
	}

	private void updateGroupIdDirectly(int class_id) {
		try {
			if (myConnection == null || myConnection.isClosed())
				InitConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			String sql = "update tbl_dynamic_vehicle set grouped_id = ?,grouped_status = ?,grouped_time = to_date(?,'yyyy-mm-dd hh24:mi:ss') where vehicle_id = ?";
			PreparedStatement pstmt = myConnection.prepareStatement(sql);
			for (int i = 0; i < dynamicVehList.size(); i++) {
				DynamicVehInfo veh = dynamicVehList.get(i);
				if (veh.getGrouped_id() != 0) {
					pstmt.setInt(1, veh.getGrouped_id());
					pstmt.setInt(2, 3);
					pstmt.setString(3, format.format(new Date()));
					pstmt.setString(4, veh.getVehicle_id());
					pstmt.executeQuery();
				}
			}
			pstmt.close();
			myConnection.commit();
			myConnection.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void updateGroupId(int class_id) {
		try {
			if (myConnection == null || myConnection.isClosed())
				InitConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			String sql = "update tbl_dynamic_vehicle set grouped_id = ?,grouped_status = ?,grouped_time = to_date(?,'yyyy-mm-dd hh24:mi:ss') where vehicle_id = ?";
			PreparedStatement pstmt = myConnection.prepareStatement(sql);
			for (Entry<Integer, List<DynamicVehInfo>> entry : vehGroupMap.entrySet()) {
				List<DynamicVehInfo> list = entry.getValue();
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						System.out.println(list.get(i).getGroup_num());
						if (list.get(i).getGroup_num() > 1) {
							pstmt.setInt(1, list.get(i).getGrouped_id());
							pstmt.setInt(2, 3);
							pstmt.setString(3, format.format(new Date()));
							pstmt.setString(4, list.get(i).getVehicle_id());
							pstmt.executeQuery();
						}
					}
				}
			}
			pstmt.close();
			myConnection.commit();
			myConnection.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private void updateGroupedIds(int class_id) {
		try {
			if (myConnection == null || myConnection.isClosed())
				InitConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			String sql = "update tbl_dynamic_vehicle set grouped_id_a2_time = ?," + "grouped_id_a3_time = ?,"
					+ "grouped_id_a2_mile = ?," + "grouped_id_a3_mile = ?," + "grouped_id_wheel_fix = ? "
					+ "where vehicle_id = ?";
			PreparedStatement pstmt = myConnection.prepareStatement(sql);
			// 更新各子指标分组号
			for (Entry<Integer, List<DynamicVehInfo>> entry : vehGroupMap.entrySet()) {
				List<DynamicVehInfo> list = entry.getValue();
				if (list != null) {
					for (int i = 0; i < list.size(); i++) {
						pstmt.setInt(1, list.get(i).getGrouped_id_ax_time(2));
						pstmt.setInt(2, list.get(i).getGrouped_id_ax_time(3));
						pstmt.setInt(3, list.get(i).getGrouped_id_ax_mile(2));
						pstmt.setInt(4, list.get(i).getGrouped_id_ax_mile(3));
						pstmt.setInt(5, list.get(i).getGrouped_id_wheel_fix());
						pstmt.setString(6, list.get(i).getVehicle_id());
						// System.out.println(dynamicVehList.get(i).getGrouped_id_ax_time(2)+","+dynamicVehList.get(i).getGrouped_id_ax_time(3)+","+dynamicVehList.get(i).getGrouped_id_ax_mile(2)+","+dynamicVehList.get(i).getGrouped_id_ax_mile(3));
						pstmt.executeQuery();
					}
				}
			}
			pstmt.close();
			// 更新总的分组号
			String sql2 = "select t.grouped_id_a2_time,t.grouped_id_a2_mile,t.grouped_id_a3_time,t.grouped_id_a3_mile,t.grouped_id_wheel_fix,"
					+ "count(*) as grouped_num,wm_concat(vehicle_id) as vehicle_ids from TBL_DYNAMIC_VEHICLE t,tbl_vehicle v  "
					+ "where t.grouped_status < " + Const.GROUPED_STATUS_NORMAL + "and t.grouped_id_a2_time != 0 "
					+ " and v.veh_id = t.vehicle_id and v.veh_class_id = " + class_id
					+ "group by t.grouped_id_a2_time,t.grouped_id_a2_mile,t.grouped_id_a3_time,t.grouped_id_a3_mile,t.grouped_id_wheel_fix";
			PreparedStatement pstmt2 = myConnection.prepareStatement(sql2);
			ResultSet myResultSet;
			myResultSet = pstmt2.executeQuery();
			List<GroupInfo> groupInfoList = new ArrayList<GroupInfo>();
			while (myResultSet.next()) {
				GroupInfo groupinfo = new GroupInfo();
				groupinfo.setGrouped_id_a2_time(myResultSet.getInt("grouped_id_a2_time"));
				groupinfo.setGrouped_id_a3_time(myResultSet.getInt("grouped_id_a3_time"));
				groupinfo.setGrouped_id_a2_mile(myResultSet.getInt("grouped_id_a2_mile"));
				groupinfo.setGrouped_id_a3_mile(myResultSet.getInt("grouped_id_a3_mile"));
				groupinfo.setGrouped_id_wheel_fix(myResultSet.getInt("grouped_id_wheel_fix"));
				groupinfo.setGrouped_num(myResultSet.getInt("grouped_num"));
				groupinfo.setVehicle_ids_str(myResultSet.getString("vehicle_ids"));
				String[] temp = groupinfo.getVehicle_ids_str().split(",");
				for (int j = 0; j < temp.length; j++) {
					groupinfo.getVehicle_ids_list().add(temp[j]);
				}
				groupInfoList.add(groupinfo);
			}
			pstmt2.close();
			for (int i = 0; i < groupInfoList.size(); i++) {
				List<String> gList = groupInfoList.get(i).getVehicle_ids_list();
				int size = gList.size();
				String sql4 = "update tbl_dynamic_vehicle set grouped_id = ?,grouped_status = ? where vehicle_id = ?";
				PreparedStatement pstmt4 = myConnection.prepareStatement(sql4);
				if (size > Const.GROUPED_NUM_SUP) {// 继续分组
					for (int j = 0; j < size; j++) {
						pstmt4.setInt(1, 0);
						pstmt4.setInt(2, 2);
						pstmt4.setString(3, gList.get(j));
						pstmt4.executeQuery();
					}
				} else if (size >= Const.GROUPED_NUM_SUB) {// 已经成一组
					int num = (int) (Math.random() * 10000000);
					for (int j = 0; j < size; j++) {
						pstmt4.setInt(1, num);
						pstmt4.setInt(2, 3);
						pstmt4.setString(3, gList.get(j));
						pstmt4.executeQuery();
					}
					global_grouped_id_seq++;
				} else {// 只有一辆，需要再次合并分组
					for (int j = 0; j < size; j++) {
						pstmt4.setInt(1, 0);
						pstmt4.setInt(2, 1);
						pstmt4.setString(3, gList.get(j));
						pstmt4.executeQuery();
					}
				}
				pstmt4.close();
			}
			myConnection.commit();
			myConnection.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	private int calcGroupStatus(int grouped_id) {
		int ret = 0;
		int size = groupMapAll.get(grouped_id).size();
		if (size > Const.GROUPED_NUM_SUP) {
			ret = 2;
		} else if (size < Const.GROUPED_NUM_SUB) {
			ret = 1;
		} else {
			ret = 3;
		}
		return ret;
	}

	private void DeepGrouping(List<DynamicVehInfo> list, int grouptype_id, int class_id, int times, int total_times) {
		if (list == null || list.size() == 0) {
			System.out.println("list is null,grouptype is " + grouptype_id);
			return;
		}
		int total = list.size();
		int seq;
		// 修时间一致的
		for (int k = 2; k <= 3; k++) {
			seq = class_id * 10000000 + total_times * 10000 + grouptype_id * 100 + 1;
			groupMap = new HashMap<Integer, List<Integer>>();
			for (int i = 0; i < total; i++) {
				int id = findGroupIdTime(list, i, k, times);
				if (id == 0) {
					list.get(i).setGrouped_id_ax_time(k, seq);
					List<Integer> tmpList = new ArrayList<Integer>();
					tmpList.add(i);
					groupMap.put(seq, tmpList);
					seq++;
				} else {
					list.get(i).setGrouped_id_ax_time(k, id);
					groupMap.get(id).add(i);
				}
			}
			groupMapAll.putAll(groupMap);
		}
		// 走行里程一致的
		for (int k = 2; k <= 3; k++) {
			seq = class_id * 10000000 + total_times * 10000 + grouptype_id * 100 + 1;
			groupMap = new HashMap<Integer, List<Integer>>();
			for (int i = 0; i < total; i++) {
				int id = findGroupIdMile(list, i, k, times);
				if (id == 0) {
					list.get(i).setGrouped_id_ax_mile(k, seq);
					List<Integer> tmpList = new ArrayList<Integer>();
					tmpList.add(i);
					groupMap.put(seq, tmpList);
					seq++;
				} else {
					list.get(i).setGrouped_id_ax_mile(k, id);
					groupMap.get(id).add(i);
				}
			}
			groupMapAll.putAll(groupMap);
		}
		// 修形时间一致的
		// seq = (int)(Math.random() * 10000000);
		seq = class_id * 10000000 + total_times * 10000 + grouptype_id * 100 + 1;
		groupMap = new HashMap<Integer, List<Integer>>();
		for (int i = 0; i < total; i++) {
			int id = findGroupIdWheelFix(list, i, times);
			if (id == 0) {
				list.get(i).setGrouped_id_wheel_fix(seq);
				List<Integer> tmpList = new ArrayList<Integer>();
				tmpList.add(i);
				groupMap.put(seq, tmpList);
				seq++;
			} else {
				list.get(i).setGrouped_id_wheel_fix(id);
				groupMap.get(id).add(i);
			}
		}
		groupMapAll.putAll(groupMap);
		// 其他情况
		// System.out.println("DeepGrouping end!");
	}

	private void MergeGrouping(List<DynamicVehInfo> list, int grouptype_id, int class_id) {
		if (list == null || list.size() == 0) {
			System.out.println("list is null,grouptype is " + grouptype_id);
			return;
		}
		int total = list.size();
		int rand = 0;
		for (int i = 0; i < total; i++) {
			if (list.get(i).isVisited()) {
				continue;
			}
			// 为每一辆车寻找与之条件相近的所有车辆
			int temp = 0;
			for (int j = i + 1; j < total; j++) {
				if (!list.get(j).isVisited() && CheckSimilar(list.get(i), list.get(j), 1.5)) {
					rand = (int) (Math.random() * 10000000);
					list.get(i).setGrouped_id(rand);
					list.get(j).setGrouped_id(rand);
					list.get(j).setVisited(true);
					temp = list.get(i).getGroup_num();
					list.get(i).setGroup_num(temp + 1);
					temp = list.get(j).getGroup_num();
					list.get(j).setGroup_num(temp + 1);
				}
			}
		}
		// System.out.println("MergeGrouping end!");
	}

	private boolean CheckSimilar(DynamicVehInfo src, DynamicVehInfo dest, double percentage) {
		boolean ret = false;
		int date_threshold = (int)(Const.DATE_THRESHOLD * percentage);
		int mile_threshold = (int)(Const.MILE_THRESHOLD * percentage);
		int wheel_fix_threshold = (int)(Const.WHEEL_FIX_THRESHOLD * percentage);
		short count = 0;
		if (src.getGrouptype_id() == dest.getGrouptype_id()) {
			for (int i = 2; i <= 3; i++) {
				if (Math.abs(src.getAx_expire_day(i) - dest.getAx_expire_day(i)) <= date_threshold) {
					count++;
				}
				if (Math.abs(src.getAx_mile(i) - dest.getAx_mile(i)) <= mile_threshold) {
					count++;
				}
			}
			if (Math.abs(src.getDifference_value() - dest.getDifference_value()) <= wheel_fix_threshold) {
				count++;
			}
			if (count == 5) {
				ret = true;
			} else {
				ret = false;
			}
		}
		return ret;
	}

	private int findGroupIdTime(List<DynamicVehInfo> list, int n, int type, int times) {
		int ret = 0;
		int sign = times > 0 ? 1 : -1;
		int threshold = (int) (Const.DATE_THRESHOLD
				* Math.pow(1 + sign * Const.THRESHOLD_FLOAT_PERCENTAGE, Math.abs(times)));
		if (threshold > Const.DATE_THRESHOLD_MAX) {
			threshold = Const.DATE_THRESHOLD_MAX;
		}
		long expire_day = list.get(n).getAx_expire_day(type);
		for (int i = 0; i < n; i++) {
			if (Math.abs(expire_day - list.get(i).getAx_expire_day(type)) <= threshold) {
				boolean flag = true;
				if (groupMap.get(list.get(i).getGrouped_id_ax_time(type)) != null) {
					List<Integer> his = groupMap.get(list.get(i).getGrouped_id_ax_time(type));
					for (int k = 0; k < his.size(); k++) {
						if (Math.abs(expire_day - list.get(his.get(k)).getAx_expire_day(type)) > threshold) {
							flag = false;
							break;
						}
					}
				}
				if (flag) {
					ret = list.get(i).getGrouped_id_ax_time(type);
					break;
				}
			}
		}
		return ret;
	}

	private int findGroupIdMile(List<DynamicVehInfo> list, int n, int type, int times) {
		int ret = 0;
		int sign = times > 0 ? 1 : -1;
		int threshold = (int) (Const.MILE_THRESHOLD
				* Math.pow(1 + sign * Const.THRESHOLD_FLOAT_PERCENTAGE, Math.abs(times)));
		if (threshold > Const.MILE_THRESHOLD_MAX) {
			threshold = Const.MILE_THRESHOLD_MAX;
		}
		long mile = list.get(n).getAx_mile(type);
		for (int i = 0; i < n; i++) {
			if (Math.abs(mile - list.get(i).getAx_mile(type)) <= threshold) {
				boolean flag = true;
				if (groupMap.get(list.get(i).getGrouped_id_ax_mile(type)) != null) {
					List<Integer> his = groupMap.get(list.get(i).getGrouped_id_ax_mile(type));
					for (int k = 0; k < his.size(); k++) {
						if (Math.abs(mile - list.get(his.get(k)).getAx_mile(type)) > threshold) {
							flag = false;
							break;
						}
					}
				}
				if (flag) {
					ret = list.get(i).getGrouped_id_ax_mile(type);
					break;
				}
			}
		}
		return ret;
	}

	private int findGroupIdWheelFix(List<DynamicVehInfo> list, int n, int times) {
		int ret = 0;
		int sign = times > 0 ? 1 : -1;
		int threshold = (int) (Const.WHEEL_FIX_THRESHOLD
				* Math.pow(1 + sign * Const.THRESHOLD_FLOAT_PERCENTAGE, Math.abs(times)));
		if (threshold > Const.WHEEL_FIX_THRESHOLD_MAX) {
			threshold = Const.WHEEL_FIX_THRESHOLD_MAX;
		}
		long mile = list.get(n).getDifference_value();
		for (int i = 0; i < n; i++) {
			if (Math.abs(mile - list.get(i).getDifference_value()) <= threshold) {
				boolean flag = true;
				if (groupMap.get(list.get(i).getGrouped_id_wheel_fix()) != null) {
					List<Integer> his = groupMap.get(list.get(i).getGrouped_id_wheel_fix());
					for (int k = 0; k < his.size(); k++) {
						if (Math.abs(mile - list.get(his.get(k)).getDifference_value()) > threshold) {
							flag = false;
							break;
						}
					}
				}
				if (flag) {
					ret = list.get(i).getGrouped_id_wheel_fix();
					break;
				}
			}
		}
		return ret;
	}

	/*
	 * 初始化列表，传递参数如下： status：车辆状态，即数据库中grouped_status class_id：车种类型，123硬卧/硬座/软卧
	 * with_grouped: 是否初始化已分组列表，true是，false否
	 */
	private void initList(int status, int class_id, boolean with_grouped) {
		try {
			if (myConnection == null || myConnection.isClosed())
				InitConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			// 初始化组号
			Statement stmt0 = myConnection.createStatement();
			ResultSet myResultSet0;
			myResultSet0 = stmt0.executeQuery("select max(grouped_id) as id from tbl_dynamic_vehicle");
			while (myResultSet0.next()) {
				global_grouped_id_seq = myResultSet0.getInt("id");
			}
			if (global_grouped_id_seq == 0) {
				global_grouped_id_seq = 1;
			}
			// 初始化组字典信息
			Statement stmt = myConnection.createStatement();
			ResultSet myResultSet;
			myResultSet = stmt.executeQuery("select * from tbl_grouptype_dict order by type_id asc");
			groupTypeList = new ArrayList<GroupType>();
			// groupTypeList = ConvertToList(myResultSet, GroupType.class);
			while (myResultSet.next()) {
				GroupType grouptype = new GroupType();
				grouptype.setType_id(myResultSet.getInt("type_id"));
				grouptype.setType_name(myResultSet.getString("type_name"));
				grouptype.setType_desc(myResultSet.getString("type_desc"));
				grouptype.setVeh_type(myResultSet.getString("veh_type"));
				grouptype.setVeh_class(myResultSet.getString("veh_class"));
				grouptype.setMain_line_volt(myResultSet.getString("main_line_volt"));
				grouptype.setBrake_form(myResultSet.getString("brake_form"));
				grouptype.setWind_supply_form(myResultSet.getString("wind_supply_form"));
				// grouptype.setMutual_power(myResultSet.getString("mutual_power"));
				groupTypeList.add(grouptype);
			}

			// myResultSet = stmt.executeQuery("select v.*,d.* from tbl_vehicle
			// v,tbl_dynamic_vehicle d where v.veh_id = d.vehicle_id order by v.veh_id
			// asc");
			// vehList = new ArrayList<VehicleInfo>();
			// vehList = ConvertToList(myResultSet, VehicleInfo.class);
			// 初始化待分组车辆,veh_class_id<=3表示硬座/硬卧/软卧
			myResultSet = stmt.executeQuery("select v.*,d.* from tbl_vehicle v,tbl_dynamic_vehicle d "
					+ "where v.veh_id = d.vehicle_id and d.grouped_status = " + status + " and v.veh_class_id = "
					+ class_id + " order by v.veh_id asc");
			dynamicVehList = new ArrayList<DynamicVehInfo>();
			dynamicMap = new HashMap<String, Integer>();
			Date date0 = new Date();
			try {
				date0 = formatDate.parse(date_zero);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
			int seq = 0;
			while (myResultSet.next()) {
				DynamicVehInfo veh = new DynamicVehInfo();
				veh.setVehicle_id(myResultSet.getString("vehicle_id"));
				veh.setApplication_state(myResultSet.getString("application_state"));
				veh.setCurrent_unit(myResultSet.getString("current_unit"));
				veh.setRoad_traffic(myResultSet.getString("road_traffic"));
				veh.setRoute(myResultSet.getString("route"));
				veh.setAverage_daymile(myResultSet.getInt("average_daymile"));
				veh.setTotal_mile(myResultSet.getInt("total_mile"));
				veh.setVehicle_order(myResultSet.getInt("vehicle_order"));
				veh.setStart_date(myResultSet.getDate("start_date"));
				veh.setArrival_date(myResultSet.getDate("arrival_date"));
				veh.setUpdate_time(myResultSet.getDate("update_time"));
				veh.setGroup_id(myResultSet.getInt("group_id"));
				veh.setGrouptype_id(myResultSet.getInt("grouptype_id"));
				veh.setTrain_number(myResultSet.getString("train_number"));
				veh.setStatus(myResultSet.getInt("status"));
				veh.setA1_expire(myResultSet.getString("a1_time") + "-01");
				veh.setA2_expire(myResultSet.getString("a2_time") + "-01");
				veh.setA3_expire(myResultSet.getString("a3_time") + "-01");
				veh.setA4_expire(myResultSet.getString("a4_time") + "-01");
				veh.setA5_expire(myResultSet.getString("a5_time") + "-01");
				veh.setA1_distance(myResultSet.getInt("a1_distance"));
				veh.setA2_distance(myResultSet.getInt("a2_distance"));
				veh.setA3_distance(myResultSet.getInt("a3_distance"));
				veh.setA4_distance(myResultSet.getInt("a4_distance"));
				veh.setA5_distance(myResultSet.getInt("a5_distance"));
				try {
					veh.setAx_expire_date(2, formatDate.parse(veh.getA2_expire()));
					veh.setAx_expire_date(3, formatDate.parse(veh.getA3_expire()));
					long day = 0;
					for (int s = 2; s <= 3; s++) {
						day = (veh.getAx_expire_date(s).getTime() - date0.getTime()) / (24 * 60 * 60 * 1000);
						veh.setAx_expire_day(s, day);
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				veh.setAx_mile(1, veh.getA1_distance());
				veh.setAx_mile(2, veh.getA2_distance());
				veh.setAx_mile(3, veh.getA3_distance());
				veh.setAx_mile(4, veh.getA4_distance());
				veh.setAx_mile(5, veh.getA5_distance());
				veh.setWheel_last_fix_miles(myResultSet.getInt("wheel_last_fix_miles"));
				veh.setWheel_last_fix_time(myResultSet.getString("wheel_last_fix_time"));
				veh.setDifference_value(myResultSet.getInt("difference_value"));
				dynamicVehList.add(veh);
				dynamicMap.put(veh.getVehicle_id(), seq);// 记录根据vehid寻找所在dynamicVehList索引
				seq++;
			}

			if (with_grouped) {// 初始化已分组车辆
				myResultSet = stmt.executeQuery("select v.*,d.* from tbl_vehicle v,tbl_dynamic_vehicle d "
						+ "where v.veh_id = d.vehicle_id and d.grouped_status = " + 3 + " and v.veh_class_id = "
						+ class_id + " order by v.veh_id asc");
				dynamicGroupedVehList = new ArrayList<DynamicVehInfo>();
				dynamicGroupedMap = new HashMap<String, Integer>();
				seq = 0;
				while (myResultSet.next()) {
					DynamicVehInfo veh = new DynamicVehInfo();
					veh.setVehicle_id(myResultSet.getString("vehicle_id"));
					veh.setApplication_state(myResultSet.getString("application_state"));
					veh.setCurrent_unit(myResultSet.getString("current_unit"));
					veh.setRoad_traffic(myResultSet.getString("road_traffic"));
					veh.setRoute(myResultSet.getString("route"));
					veh.setAverage_daymile(myResultSet.getInt("average_daymile"));
					veh.setTotal_mile(myResultSet.getInt("total_mile"));
					veh.setVehicle_order(myResultSet.getInt("vehicle_order"));
					veh.setStart_date(myResultSet.getDate("start_date"));
					veh.setArrival_date(myResultSet.getDate("arrival_date"));
					veh.setUpdate_time(myResultSet.getDate("update_time"));
					veh.setGroup_id(myResultSet.getInt("group_id"));
					veh.setGrouptype_id(myResultSet.getInt("grouptype_id"));
					veh.setTrain_number(myResultSet.getString("train_number"));
					veh.setStatus(myResultSet.getInt("status"));
					veh.setA1_expire(myResultSet.getString("a1_time") + "-01");
					veh.setA2_expire(myResultSet.getString("a2_time") + "-01");
					veh.setA3_expire(myResultSet.getString("a3_time") + "-01");
					veh.setA4_expire(myResultSet.getString("a4_time") + "-01");
					veh.setA5_expire(myResultSet.getString("a5_time") + "-01");
					veh.setA1_distance(myResultSet.getInt("a1_distance"));
					veh.setA2_distance(myResultSet.getInt("a2_distance"));
					veh.setA3_distance(myResultSet.getInt("a3_distance"));
					veh.setA4_distance(myResultSet.getInt("a4_distance"));
					veh.setA5_distance(myResultSet.getInt("a5_distance"));
					try {
						veh.setAx_expire_date(2, formatDate.parse(veh.getA2_expire()));
						veh.setAx_expire_date(3, formatDate.parse(veh.getA3_expire()));
						long day = 0;
						for (int s = 2; s <= 3; s++) {
							day = (veh.getAx_expire_date(s).getTime() - date0.getTime()) / (24 * 60 * 60 * 1000);
							veh.setAx_expire_day(s, day);
						}
					} catch (ParseException e) {
						e.printStackTrace();
					}
					veh.setAx_mile(1, veh.getA1_distance());
					veh.setAx_mile(2, veh.getA2_distance());
					veh.setAx_mile(3, veh.getA3_distance());
					veh.setAx_mile(4, veh.getA4_distance());
					veh.setAx_mile(5, veh.getA5_distance());
					veh.setWheel_last_fix_miles(myResultSet.getInt("wheel_last_fix_miles"));
					veh.setWheel_last_fix_time(myResultSet.getString("wheel_last_fix_time"));
					veh.setDifference_value(myResultSet.getInt("difference_value"));
					dynamicGroupedVehList.add(veh);
					dynamicGroupedMap.put(veh.getVehicle_id(), seq);// 记录根据vehid寻找所在dynamicGroupedVehList索引
					seq++;
				}
			}
			myConnection.close();
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

	public DynamicVehInfo getVehicleByVehId(String vehId) {
		DynamicVehInfo ret = null;
		// for(int i = 0; i < dynamicVehList.size(); i ++) {
		// if(vehId.equals(dynamicVehList.get(i).getVehicle_id())){
		// ret = dynamicVehList.get(i);
		// }
		// }
		if (dynamicMap.containsKey(vehId)) {
			ret = dynamicVehList.get(dynamicMap.get(vehId));
		} else {
			ret = null;
		}
		return ret;
	}

	public static <T> List<T> ConvertToList(ResultSet rs, Class<T> t) throws SQLException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		ResultSetMetaData md = (ResultSetMetaData) rs.getMetaData();
		int columnCount = md.getColumnCount();
		while (rs.next()) {
			Map<String, Object> rowData = new HashMap<String, Object>();
			for (int i = 1; i <= columnCount; i++) {
				// System.out.println(md.getColumnName(i) + ",type " + md.getColumnType(i));
				if (md.getColumnType(i) != 93) {
					rowData.put(md.getColumnName(i), rs.getObject(i) == null ? "" : rs.getObject(i));
				} else {
					if (rs.getObject(i) != null) {
						rowData.put(md.getColumnName(i), rs.getObject(i));
					} else {
						rowData.put(md.getColumnName(i), null);
					}
				}
			}
			list.add(rowData);
		}
		// JsonConfig jsonConfig = new JsonConfig();
		// jsonConfig.registerJsonValueProcessor(Date.class, new
		// JsonDateValueProcessor());
		JSONArray jr = JSONArray.fromObject(list);// , jsonConfig);
		List<T> resultList = JSONArray.toList(jr, t);
		return resultList;
	}

	public void InitDriver() {
		try {
			Class.forName(oracleDriverName);
		} catch (ClassNotFoundException ex) {
			System.out.println("加载jdbc驱动失败,原因:" + ex.getMessage());
		}
	}

}
