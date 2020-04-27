package com.kthw.common.base;

import java.util.List;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class ErrCode {

    public static final boolean DEBUG = true; // 调试状态
    public static final int SUCCESS = 0; // 结果正确
    public static final int NOT_LOGIN = 1; // 用户没有登录
    public static final int USER_NOT_EXIST = 2; // 用户ID不存在
    public static final int PASSWORD_ERROR = 3; // 用户口令错误
    public static final int USER_PASSWORD_FORMAT_ERROR = 4; // 用户ID或口令输入格式错误
    public static final int USER_EXIST_ALREADY = 5; // 用户ID已存在
    public static final int PASSWORD_EMPTY = 6; // 口令为空
    public static final int PASSWORD_NOT_MATCH = 7; // 口令不匹配
    public static final int PASSWORD_ILLEGAL = 8;// 口令非法
    public static final int USER_ID_ILLEGAL = 9; // 用户ID非法
    public static final int USER_NAME_ILLEGAL = 10; // 用户名非法
    public static final int USER_FORBIDDEN = 11; // 用户不允许登录
    public static final int ACTION_FORBIDDEN = 12; // 用户操作不允许

    public static final int ROLE_ALREADY_EXISTS = 13; // 角色名称已存在

    public static final int EMPTY_RESULT = 20; // 返回结果为空
    public static final int INCOMPLETE_INFO = 21; // 信息不完整

    public static final int DB_ERROR = 1001; // 数据库访问错误
    public static final int DB_NO_CONNECTION = 1002; // 无数据库连接

    public static final int PROJECT_ID_EMPTY = 10001; // 项目ID为空
    public static final int CANNOT_NEW_PROJECT = 10002; // 权限错误，不能新建项目
    public static final int START_END_TIME_ERROR = 10003; // 起止时间设置错误
    public static final int INIT_SHIRO_ERROR = 10004;//初始化shiro错误

    public static final int MODIFY_ERROR = 10005;//修改失败
    public static final int STOREHOUSE_NOT_EXISTS = 10006;//仓库信息不存在
    public static final int USER_IS_LOGINED = 10007;//用户已登录
    public static final int PARTS_ALREADY_EXISTS = 14; // 配件信息已添加
    public static final int SHEET_ALREADY_EXISTS = 15; // 单据信息已添加
    public static final int SHEETDETAIL_ALREADY_EXISTS = 16; // 配件信息已添加
    public static final int PARTCODE_ALREADY_EXISTS = 17; // 配件出场编码已添加
    public static final int SHEET_NOT_EXISTS = 18; // 单据信息不存在
    public static final int SHEETDETAIL_IS_NONE = 19; // 单据详情为空
    public static final int PART_ID_IS_EXIST = 30; // 配件编码已存在
    public static final int DETECT_PARTS_NO_MOVED = 31; // 配件编码已存在
    public static final int DELETE_PARTS_NO_ALLOWED = 32; // 配件不允许删除
    public static final int DELETE_ININTED_PARTS_NO_ALLOWED = 33; // 配件初始化不允许删除
    public static final int DELETE_ERROR = 34; // 删除失败
    public static final int ADD_ERROR = 35; //新增失败
    public static final int ADD_NO_WHOLE_ERROR = 36; //探测站未全部新增
    public static final int DELAY_ERROR = 37; //延期失败
    public static final int NO_ALLOW_OPERATE = 38; //不符合条件操作

    public static String getMessage(int code) {
        switch (code) {
            case 0:
                return "";
            case NOT_LOGIN:
                return "用户没有登录";
            case USER_NOT_EXIST:
                return "用户ID不存在";
            case PASSWORD_ERROR:
                return "用户口令不匹配";
            case USER_PASSWORD_FORMAT_ERROR:
                return "用户ID或口令格式错误";
            case USER_EXIST_ALREADY:
                return "用户ID已存在";
            case PASSWORD_EMPTY:
                return "口令不能为空";
            case PASSWORD_NOT_MATCH:
                return "重复口令不匹配";
            case PASSWORD_ILLEGAL:
                return "口令格式非法";
            case USER_ID_ILLEGAL:
                return "用户ID格式非法";
            case USER_NAME_ILLEGAL:
                return "用户名格式非法";
            case USER_FORBIDDEN:
                return "用户登录被禁止";
            case ACTION_FORBIDDEN:
                return "用户无权限进行此操作";
            case ROLE_ALREADY_EXISTS:
                return "角色名称已存在";
            case EMPTY_RESULT:
                return "返回结果为空";
            case INCOMPLETE_INFO:
                return "信息不完整";
            case DB_ERROR:
                return "数据访问错误";
            case DB_NO_CONNECTION:
                return "无数据库连接";
            case PROJECT_ID_EMPTY:
                return "项目ID为空";
            case CANNOT_NEW_PROJECT:
                return "权限错误，不能新建项目";
            case START_END_TIME_ERROR:
                return "起止时间设置错误";
            case INIT_SHIRO_ERROR:
                return "初始化shiro错误";
            case MODIFY_ERROR:
                return "修改错误";
            case PARTS_ALREADY_EXISTS:
                return "配件信息已添加";
            case SHEET_ALREADY_EXISTS:
                return "单据信息已存在，请检查输入信息是否错误！";
            case SHEETDETAIL_ALREADY_EXISTS:
                return " 配件信息已添加";
            case PARTCODE_ALREADY_EXISTS:
                return " 配件出厂编码已添加";
            case SHEET_NOT_EXISTS:
                return "单据信息不存在";
            case SHEETDETAIL_IS_NONE:
                return "单据详情为空，请添加配件";
            case PART_ID_IS_EXIST:
                return "配件编码已存在";
            case USER_IS_LOGINED:
                return "用户已登录";
            case DETECT_PARTS_NO_MOVED:
                return "探测站未完全拆卸，不能删除";
            case DELETE_PARTS_NO_ALLOWED:
                return "该配件已经发生流转，不允许删除，请联系系统管理员";
            case DELETE_ININTED_PARTS_NO_ALLOWED:
                return "该配件为初始化配件，不允许删除，请联系系统管理员";
            case ADD_ERROR:
                return "新增失败";
            case DELETE_ERROR:
                return "删除失败";
            case ADD_NO_WHOLE_ERROR:
                return "探测站未全部新增入单据";
            case DELAY_ERROR:
                return "延期失败";
            case NO_ALLOW_OPERATE:
                return "不符合条件，不能延期";
            default:
                return "异常[" + code + "]";
        }
    }

    public static String getMessages(List<Integer> errList) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; errList != null && i < errList.size(); i++) {
            sb.append(getMessage(errList.get(i).intValue()));
            sb.append(" ");
        }
        return sb.substring(0);
    }

}
