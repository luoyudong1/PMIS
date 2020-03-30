package com.kthw.common;

import com.kthw.common.base.ErrCode;

/**
 * 返回的错误对象
 * 
 * @author ZHYI
 */
public class ErrObj {

  int code; // 错误代码
  String info; // 错误信息
  String exception; // 错误堆栈信息

  public ErrObj(int code) {
    this.setCode(code);
  }

  public ErrObj(int code, String exception) {
    this.setCode(code);
    if (ErrCode.DEBUG) {
      this.exception = exception;
    }
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
    this.info = ErrCode.getMessage(code);
  }

  public String getInfo() {
    return info;
  }

  public void setInfo(String info) {
    this.info = info;
  }

  public String getException() {
    return exception;
  }

  public void setException(String exception) {
    this.exception = exception;
  }
}
