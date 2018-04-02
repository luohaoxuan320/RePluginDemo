package com.lehow.plogin;

import com.lehow.plogin.biz.LoginBiz;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LoginPresenterTest {

  @Mock LoginBiz loginBiz;
  @Mock LoginContract.View loginView;

  private LoginPresenter loginPresenter;
  @Before
  void setupLoginPresenter(){
    MockitoAnnotations.initMocks(this);
    loginPresenter = new LoginPresenter(loginBiz);
  }

  /**
   * 登录成功测试
   */
  @Test
  void testLoginSuccess() {
    String userName = "13725333333";
    String pw = "123456";
    loginPresenter.login(userName, pw);

  }






}