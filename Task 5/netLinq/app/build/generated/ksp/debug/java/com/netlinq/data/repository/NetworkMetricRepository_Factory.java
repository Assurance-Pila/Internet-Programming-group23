package com.netlinq.data.repository;

import com.netlinq.data.local.dao.NetworkMetricDao;
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
public final class NetworkMetricRepository_Factory implements Factory<NetworkMetricRepository> {
  private final Provider<NetworkMetricDao> daoProvider;

  public NetworkMetricRepository_Factory(Provider<NetworkMetricDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public NetworkMetricRepository get() {
    return newInstance(daoProvider.get());
  }

  public static NetworkMetricRepository_Factory create(Provider<NetworkMetricDao> daoProvider) {
    return new NetworkMetricRepository_Factory(daoProvider);
  }

  public static NetworkMetricRepository newInstance(NetworkMetricDao dao) {
    return new NetworkMetricRepository(dao);
  }
}
