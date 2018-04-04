package com.lehow.loading;

import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.Consumer;
import org.reactivestreams.Subscription;

public class LoadingSubscriber<T> implements FlowableSubscriber<T> {

  private ILoadingView iLoadingView;

  private Consumer<T> onNext;
  private Consumer<Throwable> onError;

  public LoadingSubscriber(ILoadingView iLoadingView, Consumer onNext, Consumer onError) {
    this.iLoadingView = iLoadingView;
    if (iLoadingView == null) throw new NullPointerException("ILoadingView 不能为空");
    this.onNext = onNext;
    this.onError = onError;
  }

  @Override public void onSubscribe(Subscription s) {
    iLoadingView.show();
    s.request(1);
  }

  @Override public void onNext(T t) {
    iLoadingView.dismiss();
    if (onNext != null) {
      try {
        onNext.accept(t);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override public void onError(Throwable t) {
    iLoadingView.dismiss();
    if (onError != null) {
      try {
        onError.accept(t);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @Override public void onComplete() {
  }
}
