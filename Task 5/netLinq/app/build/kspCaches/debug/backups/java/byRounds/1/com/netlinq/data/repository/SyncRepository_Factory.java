package com.netlinq.data.repository;

import com.netlinq.data.remote.SupabaseApi;
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
public final class SyncRepository_Factory implements Factory<SyncRepository> {
  private final Provider<SupabaseApi> supabaseApiProvider;

  private final Provider<DeviceRepository> deviceRepositoryProvider;

  private final Provider<NetworkMetricRepository> networkMetricRepositoryProvider;

  private final Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider;

  public SyncRepository_Factory(Provider<SupabaseApi> supabaseApiProvider,
      Provider<DeviceRepository> deviceRepositoryProvider,
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider,
      Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider) {
    this.supabaseApiProvider = supabaseApiProvider;
    this.deviceRepositoryProvider = deviceRepositoryProvider;
    this.networkMetricRepositoryProvider = networkMetricRepositoryProvider;
    this.qoeFeedbackRepositoryProvider = qoeFeedbackRepositoryProvider;
  }

  @Override
  public SyncRepository get() {
    return newInstance(supabaseApiProvider.get(), deviceRepositoryProvider.get(), networkMetricRepositoryProvider.get(), qoeFeedbackRepositoryProvider.get());
  }

  public static SyncRepository_Factory create(Provider<SupabaseApi> supabaseApiProvider,
      Provider<DeviceRepository> deviceRepositoryProvider,
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider,
      Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider) {
    return new SyncRepository_Factory(supabaseApiProvider, deviceRepositoryProvider, networkMetricRepositoryProvider, qoeFeedbackRepositoryProvider);
  }

  public static SyncRepository newInstance(SupabaseApi supabaseApi,
      DeviceRepository deviceRepository, NetworkMetricRepository networkMetricRepository,
      QoeFeedbackRepository qoeFeedbackRepository) {
    return new SyncRepository(supabaseApi, deviceRepository, networkMetricRepository, qoeFeedbackRepository);
  }
}
