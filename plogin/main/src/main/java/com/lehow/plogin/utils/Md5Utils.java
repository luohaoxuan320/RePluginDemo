package com.lehow.plogin.utils;

import java.security.MessageDigest;

public class Md5Utils {
  /**
   * 生成 md5 摘要(16 位)
   */
  public static String to16Md5(String src) {
    return toMd5(src).substring(8, 24);
  }

  /**
   * 生成 md5 摘要(32 位)
   */
  public static String toMd5(String src) {
    return toHash(src, "md5").toUpperCase();
  }

  /**
   * 生成 sha-1 摘要(40 位)
   */
  public static String toSha1(String src) {
    return toHash(src, "sha-1");
  }

  /**
   * 生成 sha-224 摘要(56 位)
   */
  public static String toSha224(String src) {
    return toHash(src, "sha-224");
  }

  /**
   * 生成 sha-256 摘要(64 位)
   */
  public static String toSha256(String src) {
    return toHash(src, "sha-256");
  }

  /**
   * 生成 sha-384 摘要(96 位)
   */
  public static String toSha384(String src) {
    return toHash(src, "sha-384");
  }

  /**
   * 生成 sha-512 摘要(128 位)
   */
  public static String toSha512(String src) {
    return toHash(src, "sha-512");
  }

  private static String toHash(String src, String method) {
    try {
      MessageDigest md = MessageDigest.getInstance(method);
      md.update(src.getBytes());
      byte[] byteArray = md.digest();

      StringBuilder sbd = new StringBuilder();
      for (byte b : byteArray) {
        sbd.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
      }
      return sbd.toString();
    } catch (Exception e) {
      throw new RuntimeException("无法给(" + src + ")生成(" + method + ")摘要");
    }
  }
}
