package com.lehow.plogin.base;

import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;

public class RxTransformers {

  public static <T> FlowableTransformer io_main() {

    return new FlowableTransformer<T, T>() {
      @Override public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
      }
    };
  }

  public static <T> FlowableTransformer waitLoadingTransformer(final ILoadingView iLoadingView) {

    return new FlowableTransformer<T, T>() {
      @Override public Publisher<T> apply(final Flowable<T> upstream) {
        return Flowable.using(new Callable<ILoadingView>() {
          @Override public ILoadingView call() throws Exception {
            //初始化
            iLoadingView.show();
            return iLoadingView;
          }
        }, new Function<ILoadingView, Publisher<? extends T>>() {
          @Override public Publisher<? extends T> apply(ILoadingView iLoadingView)
              throws Exception {
            return upstream;
          }
        }, new Consumer<ILoadingView>() {
          @Override public void accept(ILoadingView iLoadingView) throws Exception {
            //释放资源
            iLoadingView.dismiss();
          }
        });
      }
    };
  }
}
