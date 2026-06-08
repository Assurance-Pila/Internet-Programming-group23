package com.netlinq.monitoring;

import com.netlinq.data.repository.NetworkMetricRepository;
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
public final class NetworkMonitorService_Factory implements Factory<NetworkMonitorService> {
  private final Provider<NetworkTypeDetector> networkTypeDetectorProvider;

  private final Provider<SignalStrengthCollector> signalStrengthCollectorProvider;

  private final Provider<LatencyMeasurer> latencyMeasurerProvider;

  private final Provider<NetworkMetricRepository> networkMetricRepositoryProvider;

  public NetworkMonitorService_Factory(Provider<NetworkTypeDetector> networkTypeDetectorProvider,
      Provider<SignalStrengthCollector> signalStrengthCollectorProvider,
      Provider<LatencyMeasurer> latencyMeasurerProvider,
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider) {
    this.networkTypeDetectorProvider = networkTypeDetectorProvider;
    this.signalStrengthCollectorProvider = signalStrengthCollectorProvider;
    this.latencyMeasurerProvider = latencyMeasurerProvider;
    this.networkMetricRepositoryProvider = networkMetricRepositoryProvider;
  }

  @Override
  public NetworkMonitorService get() {
    return newInstance(networkTypeDetectorProvider.get(), signalStrengthCollectorProvider.get(), latencyMeasurerProvider.get(), networkMetricRepositoryProvider.get());
  }

  public static NetworkMonitorService_Factory create(
      Provider<NetworkTypeDetector> networkTypeDetectorProvider,
      Provider<SignalStrengthCollector> signalStrengthCollectorProvider,
      Provider<LatencyMeasurer> latencyMeasurerProvider,
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider) {
    return new NetworkMonitorService_Factory(networkTypeDetectorProvider, signalStrengthCollectorProvider, latencyMeasurerProvider, networkMetricRepositoryProvider);
  }

  public static NetworkMonitorService newInstance(NetworkTypeDetector networkTypeDetector,
      SignalStrengthCollector signalStrengthCollector, LatencyMeasurer latencyMeasurer,
      NetworkMetricRepository networkMetricRepository) {
    return new NetworkMonitorService(networkTypeDetector, signalStrengthCollector, latencyMeasurer, networkMetricRepository);
  }
}
