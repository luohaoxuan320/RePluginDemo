package com.lehow.mygsonconvert;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.lehow.net.HttpResult;
import com.lehow.net.NetApiException;
import java.io.IOException;
import okhttp3.ResponseBody;
import retrofit2.Converter;

final class GsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
  private final Gson gson;
  private final TypeAdapter<HttpResult<T>> adapter;

  GsonResponseBodyConverter(Gson gson, TypeToken<T> typeToken) {
    this.gson = gson;
    //给Entity 包裹到HttpResult 的泛型里，成为HttpResult<Entity>
    Utils.ParameterizedTypeImpl parameterizedType =
        new Utils.ParameterizedTypeImpl(null, HttpResult.class, typeToken.getType());
    this.adapter =
        (TypeAdapter<HttpResult<T>>) this.gson.getAdapter(TypeToken.get(parameterizedType));
  }

  @Override public T convert(ResponseBody value) throws IOException {
    JsonReader jsonReader = this.gson.newJsonReader(value.charStream());

    HttpResult<T> result;
    try {
      result = this.adapter.read(jsonReader);
      //code!=1,服务器校验返回的错误信息
      if (result != null && result.getCode() != 1) {
        throw new NetApiException(result.getCode(), result.getMsg());
      }
    } finally {
      value.close();
    }
    //返回实际的Entity
    return result == null ? null : result.getData();
  }
}
