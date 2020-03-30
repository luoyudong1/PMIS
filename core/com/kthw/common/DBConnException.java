package com.kthw.common;

/**
 * 数据库连接异常，通常是不能打开数据库链接
 * 
 */
public class DBConnException extends Exception {
  private static final long serialVersionUID = -8923252842795525349L;

  public DBConnException() {
    super();
  }

  public DBConnException(String message) {
    super(message);
  }
}
