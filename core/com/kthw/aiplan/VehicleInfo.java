package com.kthw.aiplan;

import java.io.Serializable;
import java.util.Date;

public class VehicleInfo implements Serializable{

private static final long serialVersionUID = 1277229204760770L;

String a1_unit;
String a1_expire;
int a1_distance;
String a2_time;
String a2_unit;
String a2_expire;
int a2_distance;
String a3_time;
String a3_unit;
String a3_expire;
int a3_distance;
String a4_time;
String a4_unit;
String a4_expire;
int a4_distance;
String a5_time;
String a5_unit;
String a5_expire;
int a5_distance;
String state;
String state_detail;
String current_use_unit;
String current_in_unit;
String routing_train_num;
String running_range;
int veh_num;
int cis_position;
int distance_avg_day;
String parking_place;
int parking_cis_position;
String last_repair;
String current_repair;
String current_repair_unit;
String veh_id;
String veh_type_merge;
String veh_class_merge;
String made_factory;
String made_date;
int total_length;
double change_length;
double self_weight;
double load_weight;
String volume;
String attach_bureau;
String attach_depot;
String attach_place;
int fixed_number;
String veh_color;
String agent_bureau;
String agent_depot;
String agent_place;
String attach_class;
String bfs;
String veh_class;
String veh_type;
double mark_speed;
String wind_block_form;
String hook_type;
String wind_supply_form;
String main_line_volt;
String dwb_form;
String train_tail_dev;
String child_mother_car;
String announce_car;
String work_place;
String engineer_car;
String high_cold_car;
String tcds_server;
String renovate_car;
String bogie_type;
int fixed_axle_dis;
String frame_form;
String brake_form;
String pillow_spring_form;
String height_valve_type;
String diff_pressure_valve_type;
String buffer_dev_type;
String brake_dev_type;
String handful_brake_form;
String brake_cylinder_form;
String empty_car_valve;
String electric_antislide_dev;
String brakeshoe_span_ajust_dev;
String warm_dev;
String toilet_form;
String toilet_factory;
String dirty_box_volume;
String warm_water_box;
String water_box_volume;
String wind_block_damping;
String window_form;
String wind_block_door_form;
String wheel_axle_type;
String axle_end_earth;
String axle_box_obturation_form;
String sidedoor_form;
String plugdoor_form;
String oxygen_dev;
String inner_door_form;
String axle_drive_generator;
String video_system;
String lighting_system;
String info_screen;
String viceline_insulation_dect_dev;
String power_supply_request;
String smokefire_alert_dev;
String axle_power_supply_form;
String electric_empty_seat;
String tripple_inverter;
String single_inverter;
String charge_dev;
String mutual_power;
String air_con_form;
String air_con_power;
String air_con_fac;
String cabinet_form;
String diesel_engine_model;
String diesel_engine_power;
String alternator_model;
String cooling_mode;
String diesel_heat_dev;
String generator_monitor;
String generator_control_form;
String generator_emergency_dev;
String under_tank_capacity;
String window_glass;
String veh_monitor;
String food_model;
String kitchen_dev_fac;
String brake_monitor;
String brake_monitor_fac;
String spare_room;
String attach_prop;
String depart_attach_time;
String depart_attach_order;
String bureau_attach_time;
String bureau_attach_order;
String depart_change_type;
String depart_change_unit;
String depart_change_time;
String depart_change_order;
String bureau_change_type;
String bureau_change_unit;
String bureau_change_time;
String bureau_change_order;
int total_distance;
String a1_time;

int[] ax_expire_flag; //a2时间，a2里程，a3时间，a3里程。。。
String ax_which_expire;

public VehicleInfo() {
	ax_expire_flag = new int[8];
}

public String getA1_unit() {
    return a1_unit;
}
public void setA1_unit(String a1_unit) {
    this.a1_unit = a1_unit;
}
public String getA1_expire() {
    return a1_expire;
}
public void setA1_expire(String a1_expire) {
    this.a1_expire = a1_expire;
}
public int getA1_distance() {
    return a1_distance;
}
public void setA1_distance(int a1_distance) {
    this.a1_distance = a1_distance;
}
public String getA2_time() {
    return a2_time;
}
public void setA2_time(String a2_time) {
    this.a2_time = a2_time;
}
public String getA2_unit() {
    return a2_unit;
}
public void setA2_unit(String a2_unit) {
    this.a2_unit = a2_unit;
}
public String getA2_expire() {
    return a2_expire;
}
public void setA2_expire(String a2_expire) {
    this.a2_expire = a2_expire;
}
public int getA2_distance() {
    return a2_distance;
}
public void setA2_distance(int a2_distance) {
    this.a2_distance = a2_distance;
}
public String getA3_time() {
    return a3_time;
}
public void setA3_time(String a3_time) {
    this.a3_time = a3_time;
}
public String getA3_unit() {
    return a3_unit;
}
public void setA3_unit(String a3_unit) {
    this.a3_unit = a3_unit;
}
public String getA3_expire() {
    return a3_expire;
}
public void setA3_expire(String a3_expire) {
    this.a3_expire = a3_expire;
}
public int getA3_distance() {
    return a3_distance;
}
public void setA3_distance(int a3_distance) {
    this.a3_distance = a3_distance;
}
public String getA4_time() {
    return a4_time;
}
public void setA4_time(String a4_time) {
    this.a4_time = a4_time;
}
public String getA4_unit() {
    return a4_unit;
}
public void setA4_unit(String a4_unit) {
    this.a4_unit = a4_unit;
}
public String getA4_expire() {
    return a4_expire;
}
public void setA4_expire(String a4_expire) {
    this.a4_expire = a4_expire;
}
public int getA4_distance() {
    return a4_distance;
}
public void setA4_distance(int a4_distance) {
    this.a4_distance = a4_distance;
}
public String getA5_time() {
    return a5_time;
}
public void setA5_time(String a5_time) {
    this.a5_time = a5_time;
}
public String getA5_unit() {
    return a5_unit;
}
public void setA5_unit(String a5_unit) {
    this.a5_unit = a5_unit;
}
public String getA5_expire() {
    return a5_expire;
}
public void setA5_expire(String a5_expire) {
    this.a5_expire = a5_expire;
}
public int getA5_distance() {
    return a5_distance;
}
public void setA5_distance(int a5_distance) {
    this.a5_distance = a5_distance;
}
public String getState() {
    return state;
}
public void setState(String state) {
    this.state = state;
}
public String getState_detail() {
    return state_detail;
}
public void setState_detail(String state_detail) {
    this.state_detail = state_detail;
}
public String getCurrent_use_unit() {
    return current_use_unit;
}
public void setCurrent_use_unit(String current_use_unit) {
    this.current_use_unit = current_use_unit;
}
public String getCurrent_in_unit() {
    return current_in_unit;
}
public void setCurrent_in_unit(String current_in_unit) {
    this.current_in_unit = current_in_unit;
}
public String getRouting_train_num() {
    return routing_train_num;
}
public void setRouting_train_num(String routing_train_num) {
    this.routing_train_num = routing_train_num;
}
public String getRunning_range() {
    return running_range;
}
public void setRunning_range(String running_range) {
    this.running_range = running_range;
}
public int getVeh_num() {
    return veh_num;
}
public void setVeh_num(int veh_num) {
    this.veh_num = veh_num;
}
public int getCis_position() {
    return cis_position;
}
public void setCis_position(int cis_position) {
    this.cis_position = cis_position;
}
public int getDistance_avg_day() {
    return distance_avg_day;
}
public void setDistance_avg_day(int distance_avg_day) {
    this.distance_avg_day = distance_avg_day;
}
public String getParking_place() {
    return parking_place;
}
public void setParking_place(String parking_place) {
    this.parking_place = parking_place;
}
public int getParking_cis_position() {
    return parking_cis_position;
}
public void setParking_cis_position(int parking_cis_position) {
    this.parking_cis_position = parking_cis_position;
}
public String getLast_repair() {
    return last_repair;
}
public void setLast_repair(String last_repair) {
    this.last_repair = last_repair;
}
public String getCurrent_repair() {
    return current_repair;
}
public void setCurrent_repair(String current_repair) {
    this.current_repair = current_repair;
}
public String getCurrent_repair_unit() {
    return current_repair_unit;
}
public void setCurrent_repair_unit(String current_repair_unit) {
    this.current_repair_unit = current_repair_unit;
}
public String getVeh_id() {
    return veh_id;
}
public void setVeh_id(String veh_id) {
    this.veh_id = veh_id;
}
public String getVeh_type_merge() {
    return veh_type_merge;
}
public void setVeh_type_merge(String veh_type_merge) {
    this.veh_type_merge = veh_type_merge;
}
public String getVeh_class_merge() {
    return veh_class_merge;
}
public void setVeh_class_merge(String veh_class_merge) {
    this.veh_class_merge = veh_class_merge;
}
public String getMade_factory() {
    return made_factory;
}
public void setMade_factory(String made_factory) {
    this.made_factory = made_factory;
}
public String getMade_date() {
    return made_date;
}
public void setMade_date(String made_date) {
    this.made_date = made_date;
}
public int getTotal_length() {
    return total_length;
}
public void setTotal_length(int total_length) {
    this.total_length = total_length;
}
public double getChange_length() {
    return change_length;
}
public void setChange_length(double change_length) {
    this.change_length = change_length;
}
public double getSelf_weight() {
    return self_weight;
}
public void setSelf_weight(double self_weight) {
    this.self_weight = self_weight;
}
public double getLoad_weight() {
    return load_weight;
}
public void setLoad_weight(double load_weight) {
    this.load_weight = load_weight;
}
public String getVolume() {
    return volume;
}
public void setVolume(String volume) {
    this.volume = volume;
}
public String getAttach_bureau() {
    return attach_bureau;
}
public void setAttach_bureau(String attach_bureau) {
    this.attach_bureau = attach_bureau;
}
public String getAttach_depot() {
    return attach_depot;
}
public void setAttach_depot(String attach_depot) {
    this.attach_depot = attach_depot;
}
public String getAttach_place() {
    return attach_place;
}
public void setAttach_place(String attach_place) {
    this.attach_place = attach_place;
}
public int getFixed_number() {
    return fixed_number;
}
public void setFixed_number(int fixed_number) {
    this.fixed_number = fixed_number;
}
public String getVeh_color() {
    return veh_color;
}
public void setVeh_color(String veh_color) {
    this.veh_color = veh_color;
}
public String getAgent_bureau() {
    return agent_bureau;
}
public void setAgent_bureau(String agent_bureau) {
    this.agent_bureau = agent_bureau;
}
public String getAgent_depot() {
    return agent_depot;
}
public void setAgent_depot(String agent_depot) {
    this.agent_depot = agent_depot;
}
public String getAgent_place() {
    return agent_place;
}
public void setAgent_place(String agent_place) {
    this.agent_place = agent_place;
}
public String getAttach_class() {
    return attach_class;
}
public void setAttach_class(String attach_class) {
    this.attach_class = attach_class;
}
public String getBfs() {
    return bfs;
}
public void setBfs(String bfs) {
    this.bfs = bfs;
}
public String getVeh_class() {
    return veh_class;
}
public void setVeh_class(String veh_class) {
    this.veh_class = veh_class;
}
public String getVeh_type() {
    return veh_type;
}
public void setVeh_type(String veh_type) {
    this.veh_type = veh_type;
}
public double getMark_speed() {
    return mark_speed;
}
public void setMark_speed(double mark_speed) {
    this.mark_speed = mark_speed;
}
public String getWind_block_form() {
    return wind_block_form;
}
public void setWind_block_form(String wind_block_form) {
    this.wind_block_form = wind_block_form;
}
public String getHook_type() {
    return hook_type;
}
public void setHook_type(String hook_type) {
    this.hook_type = hook_type;
}
public String getWind_supply_form() {
    return wind_supply_form;
}
public void setWind_supply_form(String wind_supply_form) {
    this.wind_supply_form = wind_supply_form;
}
public String getMain_line_volt() {
    return main_line_volt;
}
public void setMain_line_volt(String main_line_volt) {
    this.main_line_volt = main_line_volt;
}
public String getDwb_form() {
    return dwb_form;
}
public void setDwb_form(String dwb_form) {
    this.dwb_form = dwb_form;
}
public String getTrain_tail_dev() {
    return train_tail_dev;
}
public void setTrain_tail_dev(String train_tail_dev) {
    this.train_tail_dev = train_tail_dev;
}
public String getChild_mother_car() {
    return child_mother_car;
}
public void setChild_mother_car(String child_mother_car) {
    this.child_mother_car = child_mother_car;
}
public String getAnnounce_car() {
    return announce_car;
}
public void setAnnounce_car(String announce_car) {
    this.announce_car = announce_car;
}
public String getWork_place() {
    return work_place;
}
public void setWork_place(String work_place) {
    this.work_place = work_place;
}
public String getEngineer_car() {
    return engineer_car;
}
public void setEngineer_car(String engineer_car) {
    this.engineer_car = engineer_car;
}
public String getHigh_cold_car() {
    return high_cold_car;
}
public void setHigh_cold_car(String high_cold_car) {
    this.high_cold_car = high_cold_car;
}
public String getTcds_server() {
    return tcds_server;
}
public void setTcds_server(String tcds_server) {
    this.tcds_server = tcds_server;
}
public String getRenovate_car() {
    return renovate_car;
}
public void setRenovate_car(String renovate_car) {
    this.renovate_car = renovate_car;
}
public String getBogie_type() {
    return bogie_type;
}
public void setBogie_type(String bogie_type) {
    this.bogie_type = bogie_type;
}
public int getFixed_axle_dis() {
    return fixed_axle_dis;
}
public void setFixed_axle_dis(int fixed_axle_dis) {
    this.fixed_axle_dis = fixed_axle_dis;
}
public String getFrame_form() {
    return frame_form;
}
public void setFrame_form(String frame_form) {
    this.frame_form = frame_form;
}
public String getBrake_form() {
    return brake_form;
}
public void setBrake_form(String brake_form) {
    this.brake_form = brake_form;
}
public String getPillow_spring_form() {
    return pillow_spring_form;
}
public void setPillow_spring_form(String pillow_spring_form) {
    this.pillow_spring_form = pillow_spring_form;
}
public String getHeight_valve_type() {
    return height_valve_type;
}
public void setHeight_valve_type(String height_valve_type) {
    this.height_valve_type = height_valve_type;
}
public String getDiff_pressure_valve_type() {
    return diff_pressure_valve_type;
}
public void setDiff_pressure_valve_type(String diff_pressure_valve_type) {
    this.diff_pressure_valve_type = diff_pressure_valve_type;
}
public String getBuffer_dev_type() {
    return buffer_dev_type;
}
public void setBuffer_dev_type(String buffer_dev_type) {
    this.buffer_dev_type = buffer_dev_type;
}
public String getBrake_dev_type() {
    return brake_dev_type;
}
public void setBrake_dev_type(String brake_dev_type) {
    this.brake_dev_type = brake_dev_type;
}
public String getHandful_brake_form() {
    return handful_brake_form;
}
public void setHandful_brake_form(String handful_brake_form) {
    this.handful_brake_form = handful_brake_form;
}
public String getBrake_cylinder_form() {
    return brake_cylinder_form;
}
public void setBrake_cylinder_form(String brake_cylinder_form) {
    this.brake_cylinder_form = brake_cylinder_form;
}
public String getEmpty_car_valve() {
    return empty_car_valve;
}
public void setEmpty_car_valve(String empty_car_valve) {
    this.empty_car_valve = empty_car_valve;
}
public String getElectric_antislide_dev() {
    return electric_antislide_dev;
}
public void setElectric_antislide_dev(String electric_antislide_dev) {
    this.electric_antislide_dev = electric_antislide_dev;
}
public String getBrakeshoe_span_ajust_dev() {
    return brakeshoe_span_ajust_dev;
}
public void setBrakeshoe_span_ajust_dev(String brakeshoe_span_ajust_dev) {
    this.brakeshoe_span_ajust_dev = brakeshoe_span_ajust_dev;
}
public String getWarm_dev() {
    return warm_dev;
}
public void setWarm_dev(String warm_dev) {
    this.warm_dev = warm_dev;
}
public String getToilet_form() {
    return toilet_form;
}
public void setToilet_form(String toilet_form) {
    this.toilet_form = toilet_form;
}
public String getToilet_factory() {
    return toilet_factory;
}
public void setToilet_factory(String toilet_factory) {
    this.toilet_factory = toilet_factory;
}
public String getDirty_box_volume() {
    return dirty_box_volume;
}
public void setDirty_box_volume(String dirty_box_volume) {
    this.dirty_box_volume = dirty_box_volume;
}
public String getWarm_water_box() {
    return warm_water_box;
}
public void setWarm_water_box(String warm_water_box) {
    this.warm_water_box = warm_water_box;
}
public String getWater_box_volume() {
    return water_box_volume;
}
public void setWater_box_volume(String water_box_volume) {
    this.water_box_volume = water_box_volume;
}
public String getWind_block_damping() {
    return wind_block_damping;
}
public void setWind_block_damping(String wind_block_damping) {
    this.wind_block_damping = wind_block_damping;
}
public String getWindow_form() {
    return window_form;
}
public void setWindow_form(String window_form) {
    this.window_form = window_form;
}
public String getWind_block_door_form() {
    return wind_block_door_form;
}
public void setWind_block_door_form(String wind_block_door_form) {
    this.wind_block_door_form = wind_block_door_form;
}
public String getWheel_axle_type() {
    return wheel_axle_type;
}
public void setWheel_axle_type(String wheel_axle_type) {
    this.wheel_axle_type = wheel_axle_type;
}
public String getAxle_end_earth() {
    return axle_end_earth;
}
public void setAxle_end_earth(String axle_end_earth) {
    this.axle_end_earth = axle_end_earth;
}
public String getAxle_box_obturation_form() {
    return axle_box_obturation_form;
}
public void setAxle_box_obturation_form(String axle_box_obturation_form) {
    this.axle_box_obturation_form = axle_box_obturation_form;
}
public String getSidedoor_form() {
    return sidedoor_form;
}
public void setSidedoor_form(String sidedoor_form) {
    this.sidedoor_form = sidedoor_form;
}
public String getPlugdoor_form() {
    return plugdoor_form;
}
public void setPlugdoor_form(String plugdoor_form) {
    this.plugdoor_form = plugdoor_form;
}
public String getOxygen_dev() {
    return oxygen_dev;
}
public void setOxygen_dev(String oxygen_dev) {
    this.oxygen_dev = oxygen_dev;
}
public String getInner_door_form() {
    return inner_door_form;
}
public void setInner_door_form(String inner_door_form) {
    this.inner_door_form = inner_door_form;
}
public String getAxle_drive_generator() {
    return axle_drive_generator;
}
public void setAxle_drive_generator(String axle_drive_generator) {
    this.axle_drive_generator = axle_drive_generator;
}
public String getVideo_system() {
    return video_system;
}
public void setVideo_system(String video_system) {
    this.video_system = video_system;
}
public String getLighting_system() {
    return lighting_system;
}
public void setLighting_system(String lighting_system) {
    this.lighting_system = lighting_system;
}
public String getInfo_screen() {
    return info_screen;
}
public void setInfo_screen(String info_screen) {
    this.info_screen = info_screen;
}
public String getViceline_insulation_dect_dev() {
    return viceline_insulation_dect_dev;
}
public void setViceline_insulation_dect_dev(String viceline_insulation_dect_dev) {
    this.viceline_insulation_dect_dev = viceline_insulation_dect_dev;
}
public String getPower_supply_request() {
    return power_supply_request;
}
public void setPower_supply_request(String power_supply_request) {
    this.power_supply_request = power_supply_request;
}
public String getSmokefire_alert_dev() {
    return smokefire_alert_dev;
}
public void setSmokefire_alert_dev(String smokefire_alert_dev) {
    this.smokefire_alert_dev = smokefire_alert_dev;
}
public String getAxle_power_supply_form() {
    return axle_power_supply_form;
}
public void setAxle_power_supply_form(String axle_power_supply_form) {
    this.axle_power_supply_form = axle_power_supply_form;
}
public String getElectric_empty_seat() {
    return electric_empty_seat;
}
public void setElectric_empty_seat(String electric_empty_seat) {
    this.electric_empty_seat = electric_empty_seat;
}
public String getTripple_inverter() {
    return tripple_inverter;
}
public void setTripple_inverter(String tripple_inverter) {
    this.tripple_inverter = tripple_inverter;
}
public String getSingle_inverter() {
    return single_inverter;
}
public void setSingle_inverter(String single_inverter) {
    this.single_inverter = single_inverter;
}
public String getCharge_dev() {
    return charge_dev;
}
public void setCharge_dev(String charge_dev) {
    this.charge_dev = charge_dev;
}
public String getMutual_power() {
    return mutual_power;
}
public void setMutual_power(String mutual_power) {
    this.mutual_power = mutual_power;
}
public String getAir_con_form() {
    return air_con_form;
}
public void setAir_con_form(String air_con_form) {
    this.air_con_form = air_con_form;
}
public String getAir_con_power() {
    return air_con_power;
}
public void setAir_con_power(String air_con_power) {
    this.air_con_power = air_con_power;
}
public String getAir_con_fac() {
    return air_con_fac;
}
public void setAir_con_fac(String air_con_fac) {
    this.air_con_fac = air_con_fac;
}
public String getCabinet_form() {
    return cabinet_form;
}
public void setCabinet_form(String cabinet_form) {
    this.cabinet_form = cabinet_form;
}
public String getDiesel_engine_model() {
    return diesel_engine_model;
}
public void setDiesel_engine_model(String diesel_engine_model) {
    this.diesel_engine_model = diesel_engine_model;
}
public String getDiesel_engine_power() {
    return diesel_engine_power;
}
public void setDiesel_engine_power(String diesel_engine_power) {
    this.diesel_engine_power = diesel_engine_power;
}
public String getAlternator_model() {
    return alternator_model;
}
public void setAlternator_model(String alternator_model) {
    this.alternator_model = alternator_model;
}
public String getCooling_mode() {
    return cooling_mode;
}
public void setCooling_mode(String cooling_mode) {
    this.cooling_mode = cooling_mode;
}
public String getDiesel_heat_dev() {
    return diesel_heat_dev;
}
public void setDiesel_heat_dev(String diesel_heat_dev) {
    this.diesel_heat_dev = diesel_heat_dev;
}
public String getGenerator_monitor() {
    return generator_monitor;
}
public void setGenerator_monitor(String generator_monitor) {
    this.generator_monitor = generator_monitor;
}
public String getGenerator_control_form() {
    return generator_control_form;
}
public void setGenerator_control_form(String generator_control_form) {
    this.generator_control_form = generator_control_form;
}
public String getGenerator_emergency_dev() {
    return generator_emergency_dev;
}
public void setGenerator_emergency_dev(String generator_emergency_dev) {
    this.generator_emergency_dev = generator_emergency_dev;
}
public String getUnder_tank_capacity() {
    return under_tank_capacity;
}
public void setUnder_tank_capacity(String under_tank_capacity) {
    this.under_tank_capacity = under_tank_capacity;
}
public String getWindow_glass() {
    return window_glass;
}
public void setWindow_glass(String window_glass) {
    this.window_glass = window_glass;
}
public String getVeh_monitor() {
    return veh_monitor;
}
public void setVeh_monitor(String veh_monitor) {
    this.veh_monitor = veh_monitor;
}
public String getFood_model() {
    return food_model;
}
public void setFood_model(String food_model) {
    this.food_model = food_model;
}
public String getKitchen_dev_fac() {
    return kitchen_dev_fac;
}
public void setKitchen_dev_fac(String kitchen_dev_fac) {
    this.kitchen_dev_fac = kitchen_dev_fac;
}
public String getBrake_monitor() {
    return brake_monitor;
}
public void setBrake_monitor(String brake_monitor) {
    this.brake_monitor = brake_monitor;
}
public String getBrake_monitor_fac() {
    return brake_monitor_fac;
}
public void setBrake_monitor_fac(String brake_monitor_fac) {
    this.brake_monitor_fac = brake_monitor_fac;
}
public String getSpare_room() {
    return spare_room;
}
public void setSpare_room(String spare_room) {
    this.spare_room = spare_room;
}
public String getAttach_prop() {
    return attach_prop;
}
public void setAttach_prop(String attach_prop) {
    this.attach_prop = attach_prop;
}
public String getDepart_attach_time() {
    return depart_attach_time;
}
public void setDepart_attach_time(String depart_attach_time) {
    this.depart_attach_time = depart_attach_time;
}
public String getDepart_attach_order() {
    return depart_attach_order;
}
public void setDepart_attach_order(String depart_attach_order) {
    this.depart_attach_order = depart_attach_order;
}
public String getBureau_attach_time() {
    return bureau_attach_time;
}
public void setBureau_attach_time(String bureau_attach_time) {
    this.bureau_attach_time = bureau_attach_time;
}
public String getBureau_attach_order() {
    return bureau_attach_order;
}
public void setBureau_attach_order(String bureau_attach_order) {
    this.bureau_attach_order = bureau_attach_order;
}
public String getDepart_change_type() {
    return depart_change_type;
}
public void setDepart_change_type(String depart_change_type) {
    this.depart_change_type = depart_change_type;
}
public String getDepart_change_unit() {
    return depart_change_unit;
}
public void setDepart_change_unit(String depart_change_unit) {
    this.depart_change_unit = depart_change_unit;
}
public String getDepart_change_time() {
    return depart_change_time;
}
public void setDepart_change_time(String depart_change_time) {
    this.depart_change_time = depart_change_time;
}
public String getDepart_change_order() {
    return depart_change_order;
}
public void setDepart_change_order(String depart_change_order) {
    this.depart_change_order = depart_change_order;
}
public String getBureau_change_type() {
    return bureau_change_type;
}
public void setBureau_change_type(String bureau_change_type) {
    this.bureau_change_type = bureau_change_type;
}
public String getBureau_change_unit() {
    return bureau_change_unit;
}
public void setBureau_change_unit(String bureau_change_unit) {
    this.bureau_change_unit = bureau_change_unit;
}
public String getBureau_change_time() {
    return bureau_change_time;
}
public void setBureau_change_time(String bureau_change_time) {
    this.bureau_change_time = bureau_change_time;
}
public String getBureau_change_order() {
    return bureau_change_order;
}
public void setBureau_change_order(String bureau_change_order) {
    this.bureau_change_order = bureau_change_order;
}
public int getTotal_distance() {
    return total_distance;
}
public void setTotal_distance(int total_distance) {
    this.total_distance = total_distance;
}
public String getA1_time() {
    return a1_time;
}
public void setA1_time(String a1_time) {
    this.a1_time = a1_time;
}

public int[] getAx_expire_flag() {
	return ax_expire_flag;
}

public void setAx_expire_flag(int[] ax_expire_flag) {
	this.ax_expire_flag = ax_expire_flag;
}

public String getAx_which_expire() {
	return ax_which_expire;
}

public void setAx_which_expire(String ax_which_expire) {
	this.ax_which_expire = ax_which_expire;
}
}

