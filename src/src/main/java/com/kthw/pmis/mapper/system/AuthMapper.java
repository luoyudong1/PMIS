package com.kthw.pmis.mapper.system;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.kthw.pmis.model.system.Function;
import com.kthw.pmis.model.system.Role;
import com.kthw.pmis.model.system.UserRole;

@Repository
public interface AuthMapper {

    List<UserRole> getRoleByUser(String userId);

    Role getRoleByUserRole(int userRole);  ////根据user_roleId 获得Role信息

    void addUserRole(UserRole userrole);

    void removeUserRole(String userId);

    List<Function> getFuncList();

    int insertFunc(Function function);

    int deleteAuthorityByFid(int funcId);

    int deleteFuncByFid(int funcId);

    int updateFunc(Function function);

    List<Integer> queryFuncIdsByRole(int roleId);

    void insertNewAuth(Map<String, Object> params);

    void deleteOldAuth(Map<String, Object> params);

    boolean checkRoleNameExists(Role role);

    int insertRole(Role role);

    int updateRole(Role role);

    int deleteRole(int roleId);

    int deletePermissionByRole(int roleId);

    int deleteUserRole(int roleId);
    
    List<Map<String, Object>> queryFuncPermByRole(Map<String, Object> params);
    
    int updatePermission(Map<String, Object> params);


}
