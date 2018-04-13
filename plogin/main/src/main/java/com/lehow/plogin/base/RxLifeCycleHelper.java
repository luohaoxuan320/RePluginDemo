package com.lehow.plogin.base;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import org.reactivestreams.Publisher;

public class RxLifeCycleHelper {

  private final BehaviorSubject<ActivityEvent> lifecycleSubject = BehaviorSubject.create();

  public <T> FlowableTransformer io_main() {

    return new FlowableTransformer<T, T>() {
      @Override public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
      }
    };
  }

  /**
   * 不能定义成<T> FlowableTransformer<T, T> 否则否编译报错
   */
  public <T> FlowableTransformer bindTolifecycle(final ActivityEvent lifeEvent) {

    return new FlowableTransformer<T, T>() {
      @Override public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.takeUntil(
            takeUntilEvent(lifecycleSubject, lifeEvent).toFlowable(BackpressureStrategy.LATEST));
      }
    };
  }

  private <R> Observable<R> takeUntilEvent(final Observable<R> lifecycle, final R event) {
    return lifecycle.filter(new Predicate<R>() {
      @Override public boolean test(R lifecycleEvent) throws Exception {
        return lifecycleEvent.equals(event);
      }
    });
  }

  public void unsubscribe(final ActivityEvent lifeEvent) {
    lifecycleSubject.onNext(lifeEvent);
  }

  public enum ActivityEvent {

    CREATE, START, RESUME, PAUSE, STOP, DESTROY

  }
}
