package com.netlinq.presentation.feedback;

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
public final class FeedbackViewModel_Factory implements Factory<FeedbackViewModel> {
  private final Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider;

  private final Provider<NetworkMetricRepository> networkMetricRepositoryProvider;

  public FeedbackViewModel_Factory(Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider,
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider) {
    this.qoeFeedbackRepositoryProvider = qoeFeedbackRepositoryProvider;
    this.networkMetricRepositoryProvider = networkMetricRepositoryProvider;
  }

  @Override
  public FeedbackViewModel get() {
    return newInstance(qoeFeedbackRepositoryProvider.get(), networkMetricRepositoryProvider.get());
  }

  public static FeedbackViewModel_Factory create(
      Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider,
      Provider<NetworkMetricRepository> networkMetricRepositoryProvider) {
    return new FeedbackViewModel_Factory(qoeFeedbackRepositoryProvider, networkMetricRepositoryProvider);
  }

  public static FeedbackViewModel newInstance(QoeFeedbackRepository qoeFeedbackRepository,
      NetworkMetricRepository networkMetricRepository) {
    return new FeedbackViewModel(qoeFeedbackRepository, networkMetricRepository);
  }
}
