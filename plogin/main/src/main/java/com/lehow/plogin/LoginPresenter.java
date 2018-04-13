package com.lehow.plogin;

import android.text.TextUtils;
import android.util.Log;
import com.lehow.loading.ILoadingView;
import com.lehow.loading.LoadingSubscriber;
import com.lehow.plogin.base.RxLifeCycleHelper;
import com.lehow.plogin.biz.LoginBiz;
import com.lehow.plogin.biz.UserEntity;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.FlowableSubscriber;
import io.reactivex.FlowableTransformer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import java.util.concurrent.Callable;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

public class LoginPresenter implements LoginContract.Presenter {

  private static final String TAG = "LoginPresenter";
  private LoginContract.View loginView;
  private LoginBiz loginBiz;

  private RxLifeCycleHelper rxLifeCycleHelper = new RxLifeCycleHelper();

  public LoginPresenter(LoginBiz loginBiz) {
    this.loginBiz = loginBiz;
  }

  Disposable subscribe;
  @Override public void login(String userName, String pw) {

     /*subscribe = Flowable.create(new FlowableOnSubscribe<Long>() {
      @Override public void subscribe(FlowableEmitter<Long> emitter) throws Exception {
        for (int i = 1; i > 0; i++) {
          Log.i(TAG, "subscribe: i=" + i + " Thread=" + Thread.currentThread());
          emitter.onNext((long) i);
          try {
            Thread.sleep(2 * 1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
            emitter.onError(e);
          }
        }
      }
    }, BackpressureStrategy.BUFFER)
         .onTerminateDetach()
        .compose(rxLifeCycleHelper.io_main())
         .onTerminateDetach()
        .subscribe(new Consumer<Long>() {
          @Override public void accept(Long o) throws Exception {
            Log.i(TAG, "accept: o=" + o);
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) throws Exception {
            Log.i(TAG, "accept: throwable=" + throwable);
          }
        }, new Action() {
          @Override public void run() throws Exception {
            Log.i(TAG, "run: onComplete");
          }
        });*/


    /*RxJavaPlugins.setErrorHandler(new Consumer<Throwable>() {
      @Override public void accept(Throwable throwable) throws Exception {
        Log.i(TAG, "accept: errorHandle ="+throwable);
      }
    });*/

    dialogWithCompose();
    //dialogWithUsing();
    if (true) return;

    //TODO 基本的本地校验逻辑
    if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(pw)) {
      // 不能为空
      return;
    }

    loginBiz.login(userName, pw).compose(rxLifeCycleHelper.<UserEntity>io_main())
        .subscribe(new LoadingSubscriber<UserEntity>(loginView, new Consumer<UserEntity>() {
          @Override public void accept(UserEntity userEntity) throws Exception {
            Log.i(TAG, "accept: userEntity=" + userEntity.getUserName());
          }
        }, new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) throws Exception {
            if (throwable instanceof LoginBiz.LoginInfoException) {
              Log.i(TAG, "accept:登录信息错误 " + throwable.getMessage());
            } else {
              Log.i(TAG, "accept: " + throwable.getMessage());
            }
          }
        }));

  }

  @Override public void register(String userName, String pw) {

  }

  @Override public void onDestory() {
    //subscribe.dispose();
    rxLifeCycleHelper.unsubscribe(RxLifeCycleHelper.ActivityEvent.DESTROY);
  }

  @Override public void takeView(LoginContract.View view) {
    this.loginView = view;
  }

  @Override public void dropView() {
    this.loginView = null;
  }

  /**
   * 这个必须放在subscribe之前，紧贴着subscribe，否则doOnComplete可能不会被调用
   */
  public <T> FlowableTransformer waitLoadingTransformerDoOn(final ILoadingView iLoadingView) {

    return new FlowableTransformer<T, T>() {
      @Override public Publisher<T> apply(Flowable<T> upstream) {
        return upstream.doOnSubscribe(new Consumer<Subscription>() {
          @Override public void accept(Subscription subscription) throws Exception {
            Log.i(TAG, "doOnSubscribe accept: ");
            iLoadingView.show();
          }
        }).doOnNext(new Consumer() {
          @Override public void accept(Object o) throws Exception {
            Log.i(TAG, "doOnNext accept: ");
          }
        }).doOnError(new Consumer<Throwable>() {
          @Override public void accept(Throwable throwable) throws Exception {
            Log.i(TAG, "doOnError accept: ");
            iLoadingView.dismiss();
          }
        }).doOnComplete(new Action() {
          @Override public void run() throws Exception {
            Log.i(TAG, "doOnComplete run: ");
            iLoadingView.dismiss();
          }
        });
      }
    };
  }

  public <T> FlowableTransformer waitLoadingTransformerUsing(final ILoadingView iLoadingView) {

    return new FlowableTransformer<T, T>() {
      @Override public Publisher<T> apply(final Flowable<T> upstream) {
        return Flowable.using(new Callable<ILoadingView>() {
          @Override public ILoadingView call() throws Exception {
            Log.i(TAG, "using call: 11");
            //初始化
            iLoadingView.show();
            return iLoadingView;
          }
        }, new Function<ILoadingView, Publisher<? extends T>>() {
          @Override public Publisher<? extends T> apply(ILoadingView iLoadingView)
              throws Exception {
            Log.i(TAG, "using apply: 22");
            return upstream;
          }
        }, new Consumer<ILoadingView>() {
          @Override public void accept(ILoadingView iLoadingView) throws Exception {
            Log.i(TAG, "using accept:333 ");
            //释放资源
            iLoadingView.dismiss();
          }
        });
      }
    };
  }

  private void dialogWithUsing() {

    Flowable.create(new FlowableOnSubscribe<Long>() {
      @Override public void subscribe(FlowableEmitter<Long> emitter) throws Exception {
        for (int i = 1; i > 0; i++) {
          Log.i(TAG, "subscribe: i=" + i + " Thread=" + Thread.currentThread());
          emitter.onNext((long) i);
          try {
            Thread.sleep(2 * 1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
            return;
          }
          if (i == 10) {
            emitter.onError(new Throwable("TTT"));
            return;
          }
        }
      }
    }, BackpressureStrategy.BUFFER)
        .compose(waitLoadingTransformerUsing(loginView))
        .compose(rxLifeCycleHelper.io_main())
        .compose(rxLifeCycleHelper.bindTolifecycle(RxLifeCycleHelper.ActivityEvent.DESTROY))
        .subscribe(new FlowableSubscriber<Long>() {
          @Override public void onSubscribe(Subscription s) {
            Log.i(TAG, "onSubscribe: ");
            s.request(Integer.MAX_VALUE);
          }

          @Override public void onNext(Long aLong) {
            Log.i(TAG, "onNext: " + aLong);
          }

          @Override public void onError(Throwable t) {
            Log.i(TAG, "onError: " + t);
          }

          @Override public void onComplete() {
            Log.i(TAG, "onComplete: ");
          }
        });
  }

  private void dialogWithCompose() {
    Flowable.create(new FlowableOnSubscribe<Long>() {
      @Override public void subscribe(FlowableEmitter<Long> emitter) throws Exception {
        for (int i = 1; i > 0; i++) {
          Log.i(TAG, "subscribe: i=" + i + " Thread=" + Thread.currentThread());
          emitter.onNext((long) i);
          try {
            Thread.sleep(2 * 1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
            return;
          }
          if (i == 10) {
            emitter.onError(new Throwable("TTT"));
            return;
          }
        }
      }
    }, BackpressureStrategy.BUFFER)
        .compose(rxLifeCycleHelper.io_main())
        .compose(rxLifeCycleHelper.bindTolifecycle(RxLifeCycleHelper.ActivityEvent.DESTROY))
        .compose(waitLoadingTransformerDoOn(loginView))
        .subscribe(new FlowableSubscriber<Long>() {
          @Override public void onSubscribe(Subscription s) {
            Log.i(TAG, "onSubscribe: ");
            s.request(Integer.MAX_VALUE);
          }

          @Override public void onNext(Long aLong) {
            Log.i(TAG, "onNext: " + aLong);
          }

          @Override public void onError(Throwable t) {
            Log.i(TAG, "onError: " + t);
          }

          @Override public void onComplete() {
            Log.i(TAG, "onComplete: ");
          }
        });
  }
}
