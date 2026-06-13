package com.netlinq.presentation.settings;

import com.netlinq.data.preferences.AppPreferences;
import com.netlinq.sync.SyncScheduler;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<AppPreferences> appPreferencesProvider;

  private final Provider<SyncScheduler> syncSchedulerProvider;

  public SettingsViewModel_Factory(Provider<AppPreferences> appPreferencesProvider,
      Provider<SyncScheduler> syncSchedulerProvider) {
    this.appPreferencesProvider = appPreferencesProvider;
    this.syncSchedulerProvider = syncSchedulerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(appPreferencesProvider.get(), syncSchedulerProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<AppPreferences> appPreferencesProvider,
      Provider<SyncScheduler> syncSchedulerProvider) {
    return new SettingsViewModel_Factory(appPreferencesProvider, syncSchedulerProvider);
  }

  public static SettingsViewModel newInstance(AppPreferences appPreferences,
      SyncScheduler syncScheduler) {
    return new SettingsViewModel(appPreferences, syncScheduler);
  }
}
