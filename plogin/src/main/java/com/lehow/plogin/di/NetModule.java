package com.lehow.plogin.di;

import com.lehow.plogin.BuildConfig;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class NetModule {

  public static final String HOST = BuildConfig.API_HOST_URL;
  @Provides OkHttpClient
  provideOkHttpClient() {
    return new OkHttpClient.Builder().build();
  }

  @Provides Retrofit provideRetrofit(OkHttpClient okHttpClient) {
    return new Retrofit.Builder().baseUrl("http://172.18.84.243:8080/agent_cloud/")
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(okHttpClient)
        .build();
  }

/*
  Interceptor headInterceptor = new Interceptor() {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
      Request original = chain.request();

      Request request = original.newBuilder()
          .header("X-Token", "")
          .header("X-appOS", "android")
          .header("X-version", BuildConfig.VERSION_NAME)
          .header("X-CaseId", "")
          .method(original.method(), original.body())
          .build();

      return chain.proceed(request);
    }
  };

  Interceptor loggingInterceptor = new Interceptor() {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
      Request request = chain.request();

      final long t1 = System.nanoTime();
      System.out.println(String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));

      okhttp3.Response response = chain.proceed(request);

      final long t2 = System.nanoTime();
      final String responseBody = response.body().string();
      System.out.println(String.format("Received response for %s in %.1fms%n%s%s", response.request().url(), (t2 - t1) / 1e6d, response.headers(), responseBody));
      return response.newBuilder()
          .body(ResponseBody.create(response.body().contentType(), responseBody))
          .build();
    }
  };*/
}
