package com.lehow.plogin;

import io.reactivex.Flowable;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.Consumer;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class TestRxJava {


  @Test
  public void  testRxjava(){
    /*Observable.create(new ObservableOnSubscribe<Integer>() {
      @Override
      public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
        System.out.println("emit 1");
        emitter.onNext(1);
       System.out.println("emit 2");
        emitter.onNext(2);
       System.out.println("emit 3");
        emitter.onNext(3);
       System.out.println("emit complete");
        emitter.onComplete();
       System.out.println("emit 4");
        emitter.onNext(4);
      }
    }).subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) throws Exception {
       System.out.println("onNext: " + integer);
      }
    });

    Observable.just(1,2,3,4).subscribe(new Consumer<Integer>() {
      @Override
      public void accept(Integer integer) throws Exception {
        System.out.println("just onNext: " + integer);
      }
    });*/

/*
    Observable.just(1,2,3,4).subscribe(new Observer<Integer>() {
      @Override public void onSubscribe(Disposable d) {
        System.out.println("onSubscribe: ");

      }

      @Override public void onNext(Integer integer) {
        System.out.println("onNext: ");

      }

      @Override public void onError(Throwable e) {
        System.out.println("onError: ");

      }

      @Override public void onComplete() {
        System.out.println("onComplete: ");

      }
    });*/

    Flowable.just(1,2,3,4).subscribe(new Subscriber<Integer>() {
      @Override public void onSubscribe(Subscription s) {
        System.out.println("onSubscribe: ");
        s.request(1);
      }

      @Override public void onNext(Integer integer) {
        System.out.println("onNext: "+integer);

      }

      @Override public void onError(Throwable t) {
        System.out.println("onError: ");
      }

      @Override public void onComplete() {
        System.out.println("onComplete: ");
      }
    });



    Flowable.just(1).subscribe(new FlowableSubscriber<Integer>() {
      @Override public void onSubscribe(Subscription s) {
        System.out.println("onSubscribe: ");
        s.request(1);//必须要这个

      }

      @Override public void onNext(Integer integer) {
        System.out.println("onNext: integer="+integer);
      }

      @Override public void onError(Throwable t) {
        System.out.println("onError: ");
      }

      @Override public void onComplete() {
        System.out.println("onComplete: ");
      }
    });

    Flowable.just(1).subscribe(new Consumer<Integer>() {
      @Override public void accept(Integer integer) throws Exception {
        System.out.println("accept: integer="+integer);
      }
    });
  }
}
