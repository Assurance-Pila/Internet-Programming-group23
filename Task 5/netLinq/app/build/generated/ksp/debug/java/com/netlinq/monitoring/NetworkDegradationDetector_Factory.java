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
public final class NetworkDegradationDetector_Factory implements Factory<NetworkDegradationDetector> {
  @Override
  public NetworkDegradationDetector get() {
    return newInstance();
  }

  public static NetworkDegradationDetector_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NetworkDegradationDetector newInstance() {
    return new NetworkDegradationDetector();
  }

  private static final class InstanceHolder {
    private static final NetworkDegradationDetector_Factory INSTANCE = new NetworkDegradationDetector_Factory();
  }
}
