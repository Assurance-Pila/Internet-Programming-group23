package com.netlinq.di;

import com.netlinq.data.local.NetLinqDatabase;
import com.netlinq.data.local.dao.QoeFeedbackDao;
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
public final class DatabaseModule_ProvideQoeFeedbackDaoFactory implements Factory<QoeFeedbackDao> {
  private final Provider<NetLinqDatabase> databaseProvider;

  public DatabaseModule_ProvideQoeFeedbackDaoFactory(Provider<NetLinqDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public QoeFeedbackDao get() {
    return provideQoeFeedbackDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideQoeFeedbackDaoFactory create(
      Provider<NetLinqDatabase> databaseProvider) {
    return new DatabaseModule_ProvideQoeFeedbackDaoFactory(databaseProvider);
  }

  public static QoeFeedbackDao provideQoeFeedbackDao(NetLinqDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideQoeFeedbackDao(database));
  }
}
