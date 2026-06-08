package com.netlinq.data.repository;

import com.netlinq.data.preferences.AppPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DeviceRepository_Factory implements Factory<DeviceRepository> {
  private final Provider<AppPreferences> appPreferencesProvider;

  public DeviceRepository_Factory(Provider<AppPreferences> appPreferencesProvider) {
    this.appPreferencesProvider = appPreferencesProvider;
  }

  @Override
  public DeviceRepository get() {
    return newInstance(appPreferencesProvider.get());
  }

  public static DeviceRepository_Factory create(Provider<AppPreferences> appPreferencesProvider) {
    return new DeviceRepository_Factory(appPreferencesProvider);
  }

  public static DeviceRepository newInstance(AppPreferences appPreferences) {
    return new DeviceRepository(appPreferences);
  }
}
