package com.lehow.mygsonconvert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Converter.Factory;
import retrofit2.Retrofit;

public final class MyGsonConverterFactory<T> extends Factory {
  private final Gson gson;

  public static MyGsonConverterFactory create() {
    return create(new Gson());
  }

  public static MyGsonConverterFactory create(Gson gson) {
    if (gson == null) {
      throw new NullPointerException("gson == null");
    } else {
      return new MyGsonConverterFactory(gson);
    }
  }

  private MyGsonConverterFactory(Gson gson) {
    this.gson = gson;
  }

  @Override
  public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
      Retrofit retrofit) {
    return new GsonResponseBodyConverter(this.gson, TypeToken.get(type));
  }

  @Override public Converter<?, RequestBody> requestBodyConverter(Type type,
      Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
    TypeAdapter<?> adapter = this.gson.getAdapter(TypeToken.get(type));
    return new GsonRequestBodyConverter(this.gson, adapter);
  }
}
