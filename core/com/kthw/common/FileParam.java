package com.kthw.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

/**
 * 配置文件参数操作
 * 
 * @author Administrator 2017-09-15
 */
public class FileParam {

  /**
   * @param param
   * @return
   */
  public static String readSetting(HttpServletRequest request,String str) {
    File iniFile = new File(request.getSession().getServletContext().getRealPath("/WEB-INF/classes"), "file.ini");
    if (!iniFile.exists()) {
      return "error";
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
        }
        else {
          parts = line.split("=");
          if (parts.length == 2) {
            String key = parts[0].trim();
            String value = parts[1].trim();
            if (str.equalsIgnoreCase(key)) {
              return value;
            }
          }
        }
      }
    }
    catch (Exception ex) {
    }
    finally {
      if (reader != null) {
        try {
          reader.close();
        }
        catch (IOException ex1) {
        }
      }
    }
    return "error";
  }
}
