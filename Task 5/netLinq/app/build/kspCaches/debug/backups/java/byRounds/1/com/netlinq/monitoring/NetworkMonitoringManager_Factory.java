package com.netlinq.monitoring;

import android.content.Context;
import com.netlinq.data.preferences.AppPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class NetworkMonitoringManager_Factory implements Factory<NetworkMonitoringManager> {
  private final Provider<Context> contextProvider;

  private final Provider<NetworkMonitorService> networkMonitorServiceProvider;

  private final Provider<NetworkTypeDetector> networkTypeDetectorProvider;

  private final Provider<NetworkDegradationDetector> degradationDetectorProvider;

  private final Provider<AppPreferences> appPreferencesProvider;

  public NetworkMonitoringManager_Factory(Provider<Context> contextProvider,
      Provider<NetworkMonitorService> networkMonitorServiceProvider,
      Provider<NetworkTypeDetector> networkTypeDetectorProvider,
      Provider<NetworkDegradationDetector> degradationDetectorProvider,
      Provider<AppPreferences> appPreferencesProvider) {
    this.contextProvider = contextProvider;
    this.networkMonitorServiceProvider = networkMonitorServiceProvider;
    this.networkTypeDetectorProvider = networkTypeDetectorProvider;
    this.degradationDetectorProvider = degradationDetectorProvider;
    this.appPreferencesProvider = appPreferencesProvider;
  }

  @Override
  public NetworkMonitoringManager get() {
    return newInstance(contextProvider.get(), networkMonitorServiceProvider.get(), networkTypeDetectorProvider.get(), degradationDetectorProvider.get(), appPreferencesProvider.get());
  }

  public static NetworkMonitoringManager_Factory create(Provider<Context> contextProvider,
      Provider<NetworkMonitorService> networkMonitorServiceProvider,
      Provider<NetworkTypeDetector> networkTypeDetectorProvider,
      Provider<NetworkDegradationDetector> degradationDetectorProvider,
      Provider<AppPreferences> appPreferencesProvider) {
    return new NetworkMonitoringManager_Factory(contextProvider, networkMonitorServiceProvider, networkTypeDetectorProvider, degradationDetectorProvider, appPreferencesProvider);
  }

  public static NetworkMonitoringManager newInstance(Context context,
      NetworkMonitorService networkMonitorService, NetworkTypeDetector networkTypeDetector,
      NetworkDegradationDetector degradationDetector, AppPreferences appPreferences) {
    return new NetworkMonitoringManager(context, networkMonitorService, networkTypeDetector, degradationDetector, appPreferences);
  }
}
