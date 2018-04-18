package com.lehow.net;

public class NetApiException extends RuntimeException {
  private int code;
  private String msg;

  public NetApiException(int code, String msg) {
    super("Code=" + code + "   Msg=" + msg);
    this.code = code;
    this.msg = msg;
  }

  @Override public String toString() {
    return "Code=" + code + "   Msg=" + msg;
  }
}
