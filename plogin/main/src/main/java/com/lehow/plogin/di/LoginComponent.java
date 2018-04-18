package com.lehow.plogin.di;

import com.lehow.plogin.LoginFragment;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { LoginModule.class, NetModule.class })
public interface LoginComponent {
  void inject(LoginFragment loginFragment);
}
