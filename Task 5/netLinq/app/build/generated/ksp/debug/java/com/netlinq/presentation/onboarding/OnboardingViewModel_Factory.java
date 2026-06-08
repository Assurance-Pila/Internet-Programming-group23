package com.netlinq.presentation.onboarding;

import com.netlinq.data.preferences.AppPreferences;
import com.netlinq.data.repository.DeviceRepository;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<DeviceRepository> deviceRepositoryProvider;

  private final Provider<AppPreferences> appPreferencesProvider;

  private final Provider<SyncScheduler> syncSchedulerProvider;

  public OnboardingViewModel_Factory(Provider<DeviceRepository> deviceRepositoryProvider,
      Provider<AppPreferences> appPreferencesProvider,
      Provider<SyncScheduler> syncSchedulerProvider) {
    this.deviceRepositoryProvider = deviceRepositoryProvider;
    this.appPreferencesProvider = appPreferencesProvider;
    this.syncSchedulerProvider = syncSchedulerProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(deviceRepositoryProvider.get(), appPreferencesProvider.get(), syncSchedulerProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<DeviceRepository> deviceRepositoryProvider,
      Provider<AppPreferences> appPreferencesProvider,
      Provider<SyncScheduler> syncSchedulerProvider) {
    return new OnboardingViewModel_Factory(deviceRepositoryProvider, appPreferencesProvider, syncSchedulerProvider);
  }

  public static OnboardingViewModel newInstance(DeviceRepository deviceRepository,
      AppPreferences appPreferences, SyncScheduler syncScheduler) {
    return new OnboardingViewModel(deviceRepository, appPreferences, syncScheduler);
  }
}
