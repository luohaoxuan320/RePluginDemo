package com.lehow.plogin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import io.reactivex.FlowableSubscriber;
import io.reactivex.functions.Consumer;
import org.reactivestreams.Subscription;

public class ProgressDialogSubscriber<T> implements FlowableSubscriber<T> {

  private static final String TAG = "ProgressSubscriber";
  private MyDialogFragment myDialogFragment;
  private FragmentManager fragmentManager;
  private Consumer<T> onNext;
  private Consumer<Throwable> onError;

  public ProgressDialogSubscriber(FragmentManager fragmentManager, Consumer<T> onNext,
      Consumer<Throwable> onError) {
    myDialogFragment = new MyDialogFragment();
    this.fragmentManager = fragmentManager;
    this.onNext = onNext;
    this.onError = onError;
  }

  @Override public void onSubscribe(Subscription s) {
    Log.i(TAG, "onSubscribe: ");
    myDialogFragment.show(fragmentManager, "dialog");
    s.request(1);
  }

  @Override public void onError(Throwable t) {
    Log.i(TAG, "onError: ");
    myDialogFragment.dismiss();
    try {
      onError.accept(t);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override public void onComplete() {
    Log.i(TAG, "onComplete: ");
    myDialogFragment.dismiss();
  }

  @Override public void onNext(T o) {
    Log.i(TAG, "onNext: " + o);
    myDialogFragment.dismiss();
    try {
      onNext.accept(o);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static class MyDialogFragment extends DialogFragment {

    @Nullable @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
      return inflater.inflate(R.layout.layout_dialog_fragment, container, false);
    }
  }
}
