package com.netlinq.presentation.history;

import com.netlinq.data.repository.NetworkMetricRepository;
import com.netlinq.data.repository.QoeFeedbackRepository;
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
public final class HistoryViewModel_Factory implements Factory<HistoryViewModel> {
  private final Provider<NetworkMetricRepository> networkMetricRepositoryProvider;

  private final Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider;

  public HistoryViewModel_Factory(Provider<NetworkMetricRepository> networkMetricRepositoryProvider,
      Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider) {
    this.networkMetricRepositoryProvider = networkMetricRepositoryProvider;
    this.qoeFeedbackRepositoryProvider = qoeFeedbackRepositoryProvider;
  }

  @Override
  public HistoryViewModel get() {
    return newInstance(networkMetricRepositoryProvider.get(), qoeFeedbackRepositoryProvider.get());
  }

  public static HistoryViewModel_Factory create(
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider,
      Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider) {
    return new HistoryViewModel_Factory(networkMetricRepositoryProvider, qoeFeedbackRepositoryProvider);
  }

  public static HistoryViewModel newInstance(NetworkMetricRepository networkMetricRepository,
      QoeFeedbackRepository qoeFeedbackRepository) {
    return new HistoryViewModel(networkMetricRepository, qoeFeedbackRepository);
  }
}
