package com.kthw.pmis.service.system;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kthw.pmis.model.system.AuthFunction;
import com.kthw.pmis.model.system.Depot;
import com.kthw.pmis.model.system.Function;
import com.kthw.pmis.model.system.MenuNode;
import com.kthw.pmis.model.system.Role;
import com.kthw.pmis.model.system.Team;
import com.kthw.pmis.model.system.User;
import com.kthw.pmis.model.system.UserLog;
import com.kthw.pmis.model.system.UserRole;
import com.kthw.pmis.model.system.Workshop;

public interface UserService {

    int addUser(User user);

    //增加用户，增加对应的角色
    int addUser(User user,String roleid);

    int updateUser(User user);
    //修改用户信息，修改角色
    int updateUser(User user,String roleid);

    int removeUser(String userId);

    User getUserById(String userId);

    Role getRoleByUserRole(int userRole);    //根据user_roleId 获得Role信息   ZF.add

    void updateUser_Index(User user);        //登录不同系统后更行tbl_user 表中的角色编号和登录日期   ZF.add

    List<User> getAllUsers(Map<String, String> params);

    int getAllUsersCount();
    
    int getUsersParamCount(Map<String, String> params);

    boolean login(UserLog user);

    int logout(String userId);

    List<Role> getRoleList();

    int updateUserIdxUrl(String userId,String idx_url);

    List<Function> getFuncList();

    List<UserRole> getUserRole(String userId);

    Function createFunc(Function function);

    void deleteFunc(int funcId) throws Exception;

    int modifyFunc(Function function);

    List<Integer> getFuncIdsByRole(int roleId);

    void updatePermission(Map<String, String> params) throws Exception;

    int createRole(Role role);

    int modifyRole(Role role);

    void deleteRole(int roleId) throws Exception;
    /**
     * params 用户id
     * desc:获取本用户所具有角色和角色对应的功能，以及功能对应的不可操作页面元素
     *      不可操作元素之间用;隔开,区分id或者class  id则前面为#,class则前面为.
     * */
    List<AuthFunction> buildUserOperation(String userId);

    List<MenuNode> buildUserMenu(String userId);

    List<MenuNode> buildUserMenuByRole(User user);  // 通过登录分系统用户角色获取菜单    ZZF.ADD

    Set<String> getUserRolesAsString(String userId);

    Set<String> getPermissionAsString(String userId);
    
    List<Map<String, Object>> queryFuncPermByRole(Map<String, Object> params);
    
    int modifyPermission(Map<String, Object> params);

	List<Depot> getAllDepot(Map<String, Object> params);

	List<Workshop> getAllWorkshop(Map<String, Object> params);

	List<Team> getAllTeam(Map<String, Object> params);
    
}
