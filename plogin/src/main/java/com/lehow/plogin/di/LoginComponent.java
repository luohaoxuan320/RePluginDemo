package com.lehow.plogin.di;

import com.lehow.plogin.LoginActivity;
import dagger.Component;
import javax.inject.Singleton;

@Singleton @Component(modules = { LoginModule.class, NetModule.class })
public interface LoginComponent {
  void inject(LoginActivity loginActivity);
}
