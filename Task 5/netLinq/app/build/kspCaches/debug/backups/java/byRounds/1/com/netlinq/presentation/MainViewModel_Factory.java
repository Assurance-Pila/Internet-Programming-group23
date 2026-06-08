package com.netlinq.presentation;

import com.netlinq.data.preferences.AppPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<AppPreferences> appPreferencesProvider;

  public MainViewModel_Factory(Provider<AppPreferences> appPreferencesProvider) {
    this.appPreferencesProvider = appPreferencesProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(appPreferencesProvider.get());
  }

  public static MainViewModel_Factory create(Provider<AppPreferences> appPreferencesProvider) {
    return new MainViewModel_Factory(appPreferencesProvider);
  }

  public static MainViewModel newInstance(AppPreferences appPreferences) {
    return new MainViewModel(appPreferences);
  }
}
