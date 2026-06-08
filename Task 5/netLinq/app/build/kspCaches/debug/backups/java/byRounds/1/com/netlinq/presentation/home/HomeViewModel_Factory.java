package com.netlinq.presentation.home;

import com.netlinq.data.repository.NetworkMetricRepository;
import com.netlinq.data.repository.SyncRepository;
import com.netlinq.monitoring.NetworkMonitorService;
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
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<NetworkMonitorService> networkMonitorServiceProvider;

  private final Provider<NetworkMetricRepository> networkMetricRepositoryProvider;

  private final Provider<SyncRepository> syncRepositoryProvider;

  public HomeViewModel_Factory(Provider<NetworkMonitorService> networkMonitorServiceProvider,
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider,
      Provider<SyncRepository> syncRepositoryProvider) {
    this.networkMonitorServiceProvider = networkMonitorServiceProvider;
    this.networkMetricRepositoryProvider = networkMetricRepositoryProvider;
    this.syncRepositoryProvider = syncRepositoryProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(networkMonitorServiceProvider.get(), networkMetricRepositoryProvider.get(), syncRepositoryProvider.get());
  }

  public static HomeViewModel_Factory create(
      Provider<NetworkMonitorService> networkMonitorServiceProvider,
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider,
      Provider<SyncRepository> syncRepositoryProvider) {
    return new HomeViewModel_Factory(networkMonitorServiceProvider, networkMetricRepositoryProvider, syncRepositoryProvider);
  }

  public static HomeViewModel newInstance(NetworkMonitorService networkMonitorService,
      NetworkMetricRepository networkMetricRepository, SyncRepository syncRepository) {
    return new HomeViewModel(networkMonitorService, networkMetricRepository, syncRepository);
  }
}
