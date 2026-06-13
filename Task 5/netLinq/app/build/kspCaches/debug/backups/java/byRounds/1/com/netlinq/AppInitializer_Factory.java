package com.netlinq;

import com.netlinq.data.preferences.AppPreferences;
import com.netlinq.sync.SyncScheduler;
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
public final class AppInitializer_Factory implements Factory<AppInitializer> {
  private final Provider<AppPreferences> appPreferencesProvider;

  private final Provider<SyncScheduler> syncSchedulerProvider;

  public AppInitializer_Factory(Provider<AppPreferences> appPreferencesProvider,
      Provider<SyncScheduler> syncSchedulerProvider) {
    this.appPreferencesProvider = appPreferencesProvider;
    this.syncSchedulerProvider = syncSchedulerProvider;
  }

  @Override
  public AppInitializer get() {
    return newInstance(appPreferencesProvider.get(), syncSchedulerProvider.get());
  }

  public static AppInitializer_Factory create(Provider<AppPreferences> appPreferencesProvider,
      Provider<SyncScheduler> syncSchedulerProvider) {
    return new AppInitializer_Factory(appPreferencesProvider, syncSchedulerProvider);
  }

  public static AppInitializer newInstance(AppPreferences appPreferences,
      SyncScheduler syncScheduler) {
    return new AppInitializer(appPreferences, syncScheduler);
  }
}
