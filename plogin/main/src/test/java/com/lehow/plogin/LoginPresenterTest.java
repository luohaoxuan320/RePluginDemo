package com.lehow.plogin;

import com.lehow.plogin.biz.LoginBiz;
import com.lehow.plogin.biz.UserEntity;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LoginPresenterTest {

  @Mock LoginBiz loginBiz;
  @Mock LoginContract.View loginView;

  private LoginPresenter loginPresenter;

  @Before public void setupLoginPresenter() {
    RxAndroidPlugins.reset();

    RxAndroidPlugins.setInitMainThreadSchedulerHandler(
        new Function<Callable<Scheduler>, Scheduler>() {
          @Override public Scheduler apply(@NonNull Callable<Scheduler> schedulerCallable)
              throws Exception {
            return Schedulers.trampoline();
          }
        });

    RxJavaPlugins.reset();
    RxJavaPlugins.setIoSchedulerHandler(new Function<Scheduler, Scheduler>() {
      @Override public Scheduler apply(Scheduler scheduler) throws Exception {
        return Schedulers.trampoline();
      }
    });


    MockitoAnnotations.initMocks(this);
    loginPresenter = new LoginPresenter(loginBiz);
  }

  /**
   * 登录成功测试
   */
  @Test public void testLoginSuccess() {
    String userName = "13725333333";
    String pw = "123456";
    UserEntity userEntity = new UserEntity();
    userEntity.setUserName(userName);
    userEntity.setAddress("address");
    loginPresenter.takeView(loginView);
    //mock retrofit返回的observable
    when(loginBiz.login(anyString(), anyString())).thenReturn(Flowable.just(userEntity));
    loginPresenter.login(userName, pw);
    verify(loginBiz).login(userName, pw);
    verify(loginView).show();
    verify(loginView).dismiss();
  }






}