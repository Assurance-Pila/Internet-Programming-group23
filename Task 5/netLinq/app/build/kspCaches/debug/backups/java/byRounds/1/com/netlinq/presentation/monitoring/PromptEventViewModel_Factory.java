package com.netlinq.presentation.monitoring;

import com.netlinq.monitoring.NetworkMonitoringManager;
import com.netlinq.notifications.FeedbackNotificationHelper;
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
public final class PromptEventViewModel_Factory implements Factory<PromptEventViewModel> {
  private final Provider<NetworkMonitoringManager> monitoringManagerProvider;

  private final Provider<FeedbackNotificationHelper> notificationHelperProvider;

  public PromptEventViewModel_Factory(Provider<NetworkMonitoringManager> monitoringManagerProvider,
      Provider<FeedbackNotificationHelper> notificationHelperProvider) {
    this.monitoringManagerProvider = monitoringManagerProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  @Override
  public PromptEventViewModel get() {
    return newInstance(monitoringManagerProvider.get(), notificationHelperProvider.get());
  }

  public static PromptEventViewModel_Factory create(
      Provider<NetworkMonitoringManager> monitoringManagerProvider,
      Provider<FeedbackNotificationHelper> notificationHelperProvider) {
    return new PromptEventViewModel_Factory(monitoringManagerProvider, notificationHelperProvider);
  }

  public static PromptEventViewModel newInstance(NetworkMonitoringManager monitoringManager,
      FeedbackNotificationHelper notificationHelper) {
    return new PromptEventViewModel(monitoringManager, notificationHelper);
  }
}
