package com.kthw.pmis.mapper.system;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kthw.pmis.model.system.Depot;
import com.kthw.pmis.model.system.Function;
import com.kthw.pmis.model.system.Role;
import com.kthw.pmis.model.system.Team;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.model.system.UserLog;
import com.kthw.pmis.model.system.ViewLogStat;
import com.kthw.pmis.model.system.Workshop;

@Repository
public interface UserMapper {

    User getUserById(String userId);

    void addUser(User user);

    void updateUser(User user);

    void removeUser(String userId);

    List<User> getAllUsers(Map<String, String> params);

    int getAllUsersCount();
    
    int getUsersParamCount(Map<String, String> map);

    int login(UserLog user);

    void logout(String userId);

    List<Role> getRolesList();

    List<Function> queryFuncByUser(String userId);
    //获得用户的功能和权限
    List<Function> queryAuthByUser(String userId);    

    void updateUserUrl(Map<String, Object> map);

    int addUserLog(UserLog userlog);

    //获得登录信息  某一天的
    List<ViewLogStat> getUserOneLog(Map<String, Object> map);

    List<ViewLogStat> getUserListLog(Map<String, Object> map);
    

	List<Depot> getAllDepot(Map<String, Object> params);

	List<Workshop> getAllWorkshop(Map<String, Object> params);

	List<Team> getAllTeam(Map<String, Object> params);

}
