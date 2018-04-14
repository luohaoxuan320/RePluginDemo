package com.lehow.plogin.biz;

public class UserEntity {
  private String userName;
  private String realName;
  private String address;

  public String getUserName() {
    return userName;
  }

  public String getRealName() {
    return realName;
  }

  public String getAddress() {
    return address;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setRealName(String realName) {
    this.realName = realName;
  }

  public void setAddress(String address) {
    this.address = address;
  }
}
