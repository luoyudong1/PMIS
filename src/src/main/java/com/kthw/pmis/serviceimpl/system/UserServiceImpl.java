package com.kthw.pmis.serviceimpl.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kthw.common.base.ErrCode;
import com.kthw.pmis.mapper.system.AuthMapper;
import com.kthw.pmis.mapper.system.UserMapper;
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
import com.kthw.pmis.service.system.UserService;

@Service
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AuthMapper authMapper;

    public int addUser(User user) {
        int errCode = 0;
        try {
            userMapper.addUser(user);
        } catch (Exception e) {
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }

    public int addUser(User user, String roleIds) {
        int errCode = 0;
        try {
            userMapper.addUser(user); // 增加用户
            if (StringUtils.isNotBlank(roleIds)) { // 增加用户角色
                String[] array = roleIds.split(",");
                for (String rid : array) {
                    UserRole entity = new UserRole();
                    entity.setUser_id(user.getUser_id());
                    entity.setRole_id(Integer.valueOf(rid));
                    authMapper.addUserRole(entity);
                }
            }
        } catch (Exception e) {
            logger.error("addUser error: ", e);
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }

    public int updateUser(User user) {
        int errCode = 0;
        try {
            userMapper.updateUser(user);
        } catch (Exception e) {
            logger.error("updateUser error: ", e);
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }

    public int updateUser(User user, String roleId) {
        int errCode = 0;
        try {
            userMapper.updateUser(user); // 修改用户信息
            authMapper.removeUserRole(user.getUser_id()); // 删除角色信息
            if (StringUtils.isNotBlank(roleId)) { // 增加用户角色
                String[] array = roleId.split(",");
                for (String rid : array) {
                    UserRole entity = new UserRole();
                    entity.setRole_id(Integer.valueOf(rid));
                    entity.setUser_id(user.getUser_id());
                    authMapper.addUserRole(entity);
                }
            }
        } catch (Exception e) {
            logger.error("updateUser error: ", e);
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }

    public int removeUser(String userId) {
        int errCode = 0;
        try {
            userMapper.removeUser(userId);
            authMapper.removeUserRole(userId); // 删除角色信息
        } catch (Exception e) {
            logger.error("removeUser error: ", e);
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }

    public User getUserById(String userId) {
        User user = null;
        try {
            user = userMapper.getUserById(userId);
        } catch (Exception e) {
            logger.error("getUserById error: ", e);
        }
        return user;
    }

    public boolean login(UserLog record) {
        if (record != null) {
            return userMapper.login(record) > 0 && userMapper.addUserLog(record) > 0;
        }
        return false;
    }

    public List<User> getAllUsers(Map<String, String> params) {
        List<User> users = null;
        try {
            users = userMapper.getAllUsers(params);
        } catch (Exception e) {
            logger.error("getAllUsers error: ", e);
        }
        return users;
    }


    public int getAllUsersCount() {
        int count = 0;
        try {
            count = userMapper.getAllUsersCount();
        } catch (Exception e) {
            logger.error("getAllUsersCount error: ", e);
        }
        return count;
    }

    public int getUsersParamCount(Map<String, String> params){
        int count = 0;
        try {
            count = userMapper.getUsersParamCount(params);
        } catch (Exception e) {
            logger.error("getAllUsersCount error: ", e);
        }
        return count;
    }
    
    public int logout(String userId) {
        int errCode = 0;
        try {
            userMapper.logout(userId);
        } catch (Exception e) {
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }

    public List<Role> getRoleList() {
        List<Role> roles = null;
        try {
            roles = userMapper.getRolesList();
        } catch (Exception e) {
            logger.error("getRoleList error: ", e);
        }
        return roles;
    }

    public int updateUserIdxUrl(String userId, String idx_url) {
        int errCode = 0;
        try {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("user_id", userId);
            map.put("idx_url", idx_url);
            userMapper.updateUserUrl(map);
        } catch (Exception e) {
            logger.error("updateUserIdxUrl error: ", e);
            errCode = ErrCode.DB_ERROR;
        }
        return errCode;
    }

    public List<Function> getFuncList() {
        List<Function> func = null;
        try {
            func = authMapper.getFuncList();
        } catch (Exception e) {
            logger.error("getFuncList error: ", e);
        }
        return func;
    }

    public List<UserRole> getUserRole(String userId) {
        List<UserRole> roles = null;
        try {
            roles = authMapper.getRoleByUser(userId);
        } catch (Exception e) {
            logger.error("getUserRole error: ", e);
        }
        return roles;
    }

    /**
     * //根据user_roleId 获得Role信息   ZFF.ADD
     * @param userRole
     * @return
     */
    @Override
    public Role getRoleByUserRole(int userRole) {
        Role roles = null;
        try {
            roles = authMapper.getRoleByUserRole(userRole);
        } catch (Exception e) {
            logger.error("getUserRole error: ", e);
        }
        return roles;
    }

    /**
     * 登录不同系统后更行tbl_user 表中的角色编号和登录日期   ZF.add
     * @param user
     */
    @Override
    public void updateUser_Index(User user) {
        try {
            userMapper.updateUser_Index(user);
        } catch (Exception e) {
            logger.error("getUserRole error: ", e);
        }
    }

    @Override
    public Function createFunc(Function function) {
        try {
            if (authMapper.insertFunc(function) > 0) {
                return function;
            }
        } catch (Exception e) {
            logger.error("create function error: ", e);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFunc(int funcId) throws Exception {
        try {
            authMapper.deleteAuthorityByFid(funcId);
            authMapper.deleteFuncByFid(funcId);
        } catch (Exception e) {
            logger.error("delete function error: ", e);
            throw e;
        }
    }

    @Override
    public int modifyFunc(Function function) {
        int code = 0;
        try {
            authMapper.updateFunc(function);
        } catch (Exception e) {
            logger.error("modify function error: ", e);
            code = ErrCode.DB_ERROR;
        }
        return code;
    }

    @Override
    public List<Integer> getFuncIdsByRole(int roleId) {
        List<Integer> funcIds = null;
        try {
            funcIds = authMapper.queryFuncIdsByRole(roleId);
        } catch (Exception e) {
            logger.error("getFuncByRoleId error: ", e);
        }
        return funcIds;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermission(Map<String, String> params) throws Exception {
        try {
            if (StringUtils.isNotBlank(params.get("add"))) {
                Map<String, Object> addMap = new HashMap<String, Object>();
                addMap.put("roleId", params.get("roleId"));
                addMap.put("newIds", params.get("add").split(","));
                authMapper.insertNewAuth(addMap);
            }
            if (StringUtils.isNotBlank(params.get("cancel"))) {
                Map<String, Object> cancelMap = new HashMap<String, Object>();
                cancelMap.put("roleId", params.get("roleId"));
                cancelMap.put("deleteIds", params.get("cancel").split(","));
                authMapper.deleteOldAuth(cancelMap);
            }
        } catch (Exception e) {
            logger.error("updatePermission error: ", e);
            throw e;
        }
    }

    @Override
    public int createRole(Role role) {
        int code = 0;
        try {
            if (!authMapper.checkRoleNameExists(role)) {
                authMapper.insertRole(role);
            } else {
                code = ErrCode.ROLE_ALREADY_EXISTS;
            }
        } catch (Exception e) {
            logger.error("createRole error: ", e);
            code = ErrCode.DB_ERROR;
        }
        return code;
    }

    @Override
    public int modifyRole(Role role) {
        int code = 0;
        try {
            if (!authMapper.checkRoleNameExists(role)) {
                authMapper.updateRole(role);
            } else {
                code = ErrCode.ROLE_ALREADY_EXISTS;
            }
        } catch (Exception e) {
            logger.error("modifyRole error: ", e);
            code = ErrCode.DB_ERROR;
        }
        return code;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(int roleId) throws Exception {
        try {
            authMapper.deleteRole(roleId);
            authMapper.deletePermissionByRole(roleId);
            authMapper.deleteUserRole(roleId);
        } catch (Exception e) {
            logger.error("deleteRole error: ", e);
            throw e;
        }
    }

    @Override
    public List<AuthFunction> buildUserOperation(String userId) {
    	List<AuthFunction> authopers = new ArrayList<AuthFunction>();
        try {
            List<Function> funcList = userMapper.queryAuthByUser(userId);
            for (Function f : funcList) {
                	int funcid = f.getId();
                	int roleid = f.getRole_id();
                	String[] operation = f.getPerm_name().split(";");
                	for (String op : operation){
                		if(op != null && !op.equals("")){
	                		AuthFunction af = new AuthFunction();
	                		af.setFunc_id(funcid);
	                		af.setRole_id(roleid);	  
	                		String[] optauth = op.split(":");
	                		//等于1可用 0不可用
	                		if(optauth[0] != null && optauth[0].equals("1")){
		                		if(optauth[1].startsWith("#")){	                			 
		                			//af.setOperation("$('"+op+"').hide();"); 
		                			af.setOpt_type("ids");
		                			af.setOperation(optauth[1].substring(1));
		                		}else if(optauth[1].startsWith(".")){
		                			//af.setOperation("$('"+op+"').css('display','none');");
		                			af.setOpt_type("cla");
		                			af.setOperation(optauth[1].substring(1));
		                		}
		                		authopers.add(af);    
	                		}
                		}
                	}                	
                }
           }catch (Exception e) {
               logger.error("buildUserOperation error: ", e);
           }
        return authopers;
    }
    
    @Override
    public List<MenuNode> buildUserMenu(String userId) {
        List<MenuNode> menus = new ArrayList<MenuNode>();
        try {
            List<Function> funcList = userMapper.queryFuncByUser(userId);
            if (CollectionUtils.isNotEmpty(funcList)) {
                Map<Integer, List<MenuNode>> map = new HashMap<Integer, List<MenuNode>>();
                for (Function f : funcList) {
                    if (String.valueOf(f.getFunc_level()).compareTo("3") <= 0) {
                        MenuNode m = new MenuNode(f.getId(), f.getFunc_name(), f.getFunc_url(), f.getShow_order());
                        if (f.getParent_id() != null && f.getParent_id() > 0) {
                            if (map.get(f.getParent_id()) == null) {
                                map.put(f.getParent_id(), new ArrayList<MenuNode>());
                            }
                            map.get(f.getParent_id()).add(m);
                        } else {
                            menus.add(m);
                        }
                    }
                }
                for (MenuNode m : menus) {
                    m.setChildren(map.get(m.getId()));
                    if (m.getChildren() != null) {
                        Collections.sort(m.getChildren());
                        for(MenuNode m3 :m.getChildren()){
                        	 m3.setChildren(map.get(m3.getId()));
                        	 if(m3.getChildren()!=null){
                        		 Collections.sort(m3.getChildren());
                        	 }
                        }
                    }
                }
                Collections.sort(menus);
            }
        } catch (Exception e) {
            logger.error("buildUserMenu error: ", e);
        }
        return menus;
    }

    /**
     * 通过登录分系统用户角色获取菜单    ZZF.ADD
     * @param user
     * @return
     */
    @Override
    public List<MenuNode> buildUserMenuByRole(User user) {
        List<MenuNode> menus = new ArrayList<MenuNode>();
        try {
            List<Function> funcList = userMapper.queryFuncByUserRole(user);
            if (CollectionUtils.isNotEmpty(funcList)) {
                Map<Integer, List<MenuNode>> map = new HashMap<Integer, List<MenuNode>>();
                for (Function f : funcList) {
                    if (String.valueOf(f.getFunc_level()).compareTo("3") <= 0) {
                        MenuNode m = new MenuNode(f.getId(), f.getFunc_name(), f.getFunc_url(), f.getShow_order());
                        if (f.getParent_id() != null && f.getParent_id() > 0) {
                            if (map.get(f.getParent_id()) == null) {
                                map.put(f.getParent_id(), new ArrayList<MenuNode>());
                            }
                            map.get(f.getParent_id()).add(m);
                        } else {
                            menus.add(m);
                        }
                    }
                }
                for (MenuNode m : menus) {
                    m.setChildren(map.get(m.getId()));
                    if (m.getChildren() != null) {
                        Collections.sort(m.getChildren());
                        for(MenuNode m3 :m.getChildren()){
                            m3.setChildren(map.get(m3.getId()));
                            if(m3.getChildren()!=null){
                                Collections.sort(m3.getChildren());
                            }
                        }
                    }
                }
                Collections.sort(menus);
            }
        } catch (Exception e) {
            logger.error("buildUserMenu error: ", e);
        }
        return menus;
    }

    @Override
    public Set<String> getUserRolesAsString(String userId) {
        Set<String> ret = new HashSet<String>();
        List<UserRole> roles = getUserRole(userId);
        if (roles != null) {
            for (UserRole role : roles) {
            	int roleid = role.getRole_id();
                ret.add(String.valueOf(roleid));
            }
        }
        return ret;
    }

    @Override
    public Set<String> getPermissionAsString(String userId) {
        Set<String> ret = new HashSet<String>();
        try {
            List<Function> funcList = userMapper.queryFuncByUser(userId);
            Map<Integer, Function> map = new HashMap<Integer, Function>();
            for (Function func : funcList) {
                map.put(func.getId(), func);
            }
            for (Function func : funcList) {
                StringBuffer sb = new StringBuffer();
                do {
                    sb.insert(0, String.format("/%s", func.getFunc_url()));
                    func = map.get(func.getParent_id());
                } while (func != null);
                ret.add(sb.toString());
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return ret;
    }
    
	@Override
	public List<Map<String, Object>> queryFuncPermByRole(Map<String, Object> params) {
		return authMapper.queryFuncPermByRole(params);
	}
	
	@Override
	public int modifyPermission(Map<String, Object> params){
		return authMapper.updatePermission(params);
	}

	@Override
	public List<Depot> getAllDepot(Map<String, Object> params) {
		List<Depot> depots = new ArrayList<Depot>();
		try {
			depots = userMapper.getAllDepot(params);
		} catch (Exception e) {
			logger.error("getAllDepot error: ", e);
		}
		return depots;
	}

	@Override
	public List<Workshop> getAllWorkshop(Map<String, Object> params) {
		List<Workshop> workshops = new ArrayList<Workshop>();
		try {
			workshops = userMapper.getAllWorkshop(params);
		} catch (Exception e) {
			logger.error("getAllWorkshop error: ", e);
		}
		return workshops;
	}

	@Override
	public List<Team> getAllTeam(Map<String, Object> params) {
		List<Team> teams = new ArrayList<Team>();
		try {
			teams = userMapper.getAllTeam(params);
		} catch (Exception e) {
			logger.error("getAllTeam error: ", e);
		}
		return teams;
	}
}