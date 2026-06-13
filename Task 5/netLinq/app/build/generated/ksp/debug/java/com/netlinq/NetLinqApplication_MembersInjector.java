package com.netlinq;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class NetLinqApplication_MembersInjector implements MembersInjector<NetLinqApplication> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  private final Provider<AppInitializer> appInitializerProvider;

  public NetLinqApplication_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<AppInitializer> appInitializerProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
    this.appInitializerProvider = appInitializerProvider;
  }

  public static MembersInjector<NetLinqApplication> create(
      Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<AppInitializer> appInitializerProvider) {
    return new NetLinqApplication_MembersInjector(workerFactoryProvider, appInitializerProvider);
  }

  @Override
  public void injectMembers(NetLinqApplication instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
    injectAppInitializer(instance, appInitializerProvider.get());
  }

  @InjectedFieldSignature("com.netlinq.NetLinqApplication.workerFactory")
  public static void injectWorkerFactory(NetLinqApplication instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }

  @InjectedFieldSignature("com.netlinq.NetLinqApplication.appInitializer")
  public static void injectAppInitializer(NetLinqApplication instance,
      AppInitializer appInitializer) {
    instance.appInitializer = appInitializer;
  }
}
