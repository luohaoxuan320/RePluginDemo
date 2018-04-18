package com.lehow.plogin;

import com.google.gson.Gson;
import com.lehow.mygsonconvert.MyGsonConverterFactory;
import com.lehow.net.NetApiException;
import com.lehow.plogin.biz.LoginApi;
import com.lehow.plogin.biz.UserEntity;
import io.reactivex.functions.Predicate;
import io.reactivex.subscribers.TestSubscriber;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import static com.lehow.plogin.di.NetModule.HOST;

public class TestMyGsonConverte {

  @Before public void setUp() {
  }

  private Retrofit getRetrofit(final String responseString) {
    return new Retrofit.Builder().baseUrl(HOST)
        .addConverterFactory(MyGsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(getOkHttpClient(responseString))
        .build();
  }

  private OkHttpClient getOkHttpClient(final String responseString) {
    return new OkHttpClient.Builder().addInterceptor(new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        Response response = new Response.Builder().code(200)
            .message(responseString)
            .request(chain.request())
            .protocol(Protocol.HTTP_1_0)
            .body(
                ResponseBody.create(MediaType.parse("application/json"), responseString.getBytes()))
            .addHeader("content-type", "application/json")
            .build();
        return response;
      }
    }).build();
  }

  @Test public void testLoginSuccess() {

    //构造测试数据
    final UserEntity userEntity = new UserEntity();
    userEntity.setAddress("深圳南山");
    userEntity.setUserName("lehow");
    userEntity.setRealName("Lehow");
    HttpResult<UserEntity> httpResult = new HttpResult<>();
    httpResult.code = 1;
    httpResult.msg = "请求成功";
    httpResult.data = userEntity;

    Gson gson = new Gson();
    //将测试的数据，反序列化为json字符串
    Retrofit retrofit = getRetrofit(gson.toJson(httpResult));
    LoginApi loginApi = retrofit.create(LoginApi.class);

    TestSubscriber<UserEntity> testSubscriber = loginApi.login("13212341234", "pass").test();

    testSubscriber.assertSubscribed();
    testSubscriber.assertValueCount(1);
    //校验解析后的返回。检查预期是否一致
    testSubscriber.assertValue(new Predicate<UserEntity>() {
      @Override public boolean test(UserEntity entity) throws Exception {
        if (entity.getUserName().equals(userEntity.getUserName())) {
          return true;
        }
        return false;
      }
    });
    //这种方式判断，需要重写UserEntity的 equal方法
    //testSubscriber.assertValue(userEntity);

  }

  @Test public void testLoginFailed() {

    //构造测试数据
    final UserEntity userEntity = new UserEntity();
    userEntity.setAddress("深圳南山");
    userEntity.setUserName("lehow");
    userEntity.setRealName("Lehow");
    HttpResult<UserEntity> httpResult = new HttpResult<>();
    httpResult.code = 0;
    httpResult.msg = "用户不存在";
    httpResult.data = userEntity;

    Gson gson = new Gson();
    //将测试的数据，反序列化为json字符串
    Retrofit retrofit = getRetrofit(gson.toJson(httpResult));
    LoginApi loginApi = retrofit.create(LoginApi.class);

    TestSubscriber<UserEntity> testSubscriber = loginApi.login("13212341234", "pass").test();

    testSubscriber.assertSubscribed();

    testSubscriber.assertError(new Predicate<Throwable>() {
      @Override public boolean test(Throwable throwable) throws Exception {
        if (throwable instanceof NetApiException) {
          return true;
        }
        return false;
      }
    });

    //校验解析后的返回，检查预期是否一致
    testSubscriber.assertNever(new Predicate<UserEntity>() {
      @Override public boolean test(UserEntity entity) throws Exception {
        if (entity.getUserName().equals(userEntity.getUserName())) {
          return true;
        }
        return false;
      }
    });
    //这种方式判断，需要重写UserEntity的 equal方法
    //testSubscriber.assertValue(userEntity);

  }

  //映射一个HttpResult，方便构造json字符串
  public class HttpResult<T> {
    private int code;
    private String msg;
    private T data;

    public int getCode() {
      return code;
    }

    public String getMsg() {
      return msg;
    }

    public T getData() {
      return data;
    }
  }
}
