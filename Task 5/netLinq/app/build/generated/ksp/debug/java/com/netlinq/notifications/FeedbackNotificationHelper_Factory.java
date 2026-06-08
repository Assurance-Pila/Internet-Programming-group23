package com.netlinq.notifications;

import android.content.Context;
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
public final class FeedbackNotificationHelper_Factory implements Factory<FeedbackNotificationHelper> {
  private final Provider<Context> contextProvider;

  public FeedbackNotificationHelper_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public FeedbackNotificationHelper get() {
    return newInstance(contextProvider.get());
  }

  public static FeedbackNotificationHelper_Factory create(Provider<Context> contextProvider) {
    return new FeedbackNotificationHelper_Factory(contextProvider);
  }

  public static FeedbackNotificationHelper newInstance(Context context) {
    return new FeedbackNotificationHelper(context);
  }
}
