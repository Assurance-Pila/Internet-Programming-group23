package com.netlinq.di;

import com.netlinq.data.local.NetLinqDatabase;
import com.netlinq.data.local.dao.NetworkMetricDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideNetworkMetricDaoFactory implements Factory<NetworkMetricDao> {
  private final Provider<NetLinqDatabase> databaseProvider;

  public DatabaseModule_ProvideNetworkMetricDaoFactory(Provider<NetLinqDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public NetworkMetricDao get() {
    return provideNetworkMetricDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideNetworkMetricDaoFactory create(
      Provider<NetLinqDatabase> databaseProvider) {
    return new DatabaseModule_ProvideNetworkMetricDaoFactory(databaseProvider);
  }

  public static NetworkMetricDao provideNetworkMetricDao(NetLinqDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideNetworkMetricDao(database));
  }
}
