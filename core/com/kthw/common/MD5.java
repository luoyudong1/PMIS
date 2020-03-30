package com.kthw.common;

import java.security.*;

public class MD5 {
  public static String getMD5(String str) {
    return getKeyedDigest(str.getBytes(), getInfo());
  }

  private static String getKeyedDigest(byte[] buffer, byte[] key) {
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      md5.update(buffer);
      return byte2hex(md5.digest(key));
    }
    catch (NoSuchAlgorithmException e) {
    }
    return null;
  }

  public static void main(String args[]) {
    System.out.println(MD5.getMD5("admin"));
    System.out.println(MD5.getMD5("user1"));
    System.out.println(MD5.getMD5("user2"));
    System.out.println(MD5.getMD5("user3"));
    System.out.println(MD5.getMD5("user4"));
    System.out.println(MD5.getMD5("user5"));
  }

  private static String byte2hex(byte[] b) {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1)
        hs = hs + "0" + stmp;
      else
        hs = hs + stmp;
    }
    return hs.toUpperCase();
  }

  private static byte[] getInfo() {
    byte[] b = { 67, 76, 54, 50, 51 };
    for (int i = 0; i < b.length; i++) {
      if (i % 2 == 0) {
        b[i] = (byte) (b[i] - 1);
      }
      else {
        b[i] = (byte) (b[i] - 2);
      }
    }
    return b;
  }
}
