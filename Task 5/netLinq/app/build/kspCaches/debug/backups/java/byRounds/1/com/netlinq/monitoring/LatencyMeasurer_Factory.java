package com.netlinq.monitoring;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class LatencyMeasurer_Factory implements Factory<LatencyMeasurer> {
  @Override
  public LatencyMeasurer get() {
    return newInstance();
  }

  public static LatencyMeasurer_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static LatencyMeasurer newInstance() {
    return new LatencyMeasurer();
  }

  private static final class InstanceHolder {
    private static final LatencyMeasurer_Factory INSTANCE = new LatencyMeasurer_Factory();
  }
}
