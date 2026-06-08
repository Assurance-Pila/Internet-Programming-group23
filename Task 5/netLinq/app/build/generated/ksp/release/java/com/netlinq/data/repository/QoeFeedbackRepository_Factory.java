package com.netlinq.data.repository;

import com.netlinq.data.local.dao.QoeFeedbackDao;
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
public final class QoeFeedbackRepository_Factory implements Factory<QoeFeedbackRepository> {
  private final Provider<QoeFeedbackDao> daoProvider;

  public QoeFeedbackRepository_Factory(Provider<QoeFeedbackDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public QoeFeedbackRepository get() {
    return newInstance(daoProvider.get());
  }

  public static QoeFeedbackRepository_Factory create(Provider<QoeFeedbackDao> daoProvider) {
    return new QoeFeedbackRepository_Factory(daoProvider);
  }

  public static QoeFeedbackRepository newInstance(QoeFeedbackDao dao) {
    return new QoeFeedbackRepository(dao);
  }
}
