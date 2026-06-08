package com.netlinq;

import android.app.Activity;
import android.app.Service;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import com.netlinq.data.local.NetLinqDatabase;
import com.netlinq.data.local.dao.NetworkMetricDao;
import com.netlinq.data.local.dao.QoeFeedbackDao;
import com.netlinq.data.preferences.AppPreferences;
import com.netlinq.data.remote.SupabaseApi;
import com.netlinq.data.repository.DeviceRepository;
import com.netlinq.data.repository.NetworkMetricRepository;
import com.netlinq.data.repository.QoeFeedbackRepository;
import com.netlinq.data.repository.SyncRepository;
import com.netlinq.di.DatabaseModule_ProvideDatabaseFactory;
import com.netlinq.di.DatabaseModule_ProvideNetworkMetricDaoFactory;
import com.netlinq.di.DatabaseModule_ProvideQoeFeedbackDaoFactory;
import com.netlinq.di.NetworkModule_ProvideOkHttpClientFactory;
import com.netlinq.di.NetworkModule_ProvideRetrofitFactory;
import com.netlinq.di.NetworkModule_ProvideSupabaseApiFactory;
import com.netlinq.monitoring.LatencyMeasurer;
import com.netlinq.monitoring.NetworkDegradationDetector;
import com.netlinq.monitoring.NetworkMonitorService;
import com.netlinq.monitoring.NetworkMonitoringManager;
import com.netlinq.monitoring.NetworkTypeDetector;
import com.netlinq.monitoring.SignalStrengthCollector;
import com.netlinq.notifications.FeedbackNotificationHelper;
import com.netlinq.presentation.MainViewModel;
import com.netlinq.presentation.MainViewModel_HiltModules;
import com.netlinq.presentation.feedback.FeedbackViewModel;
import com.netlinq.presentation.feedback.FeedbackViewModel_HiltModules;
import com.netlinq.presentation.history.HistoryViewModel;
import com.netlinq.presentation.history.HistoryViewModel_HiltModules;
import com.netlinq.presentation.home.HomeViewModel;
import com.netlinq.presentation.home.HomeViewModel_HiltModules;
import com.netlinq.presentation.monitoring.PromptEventViewModel;
import com.netlinq.presentation.monitoring.PromptEventViewModel_HiltModules;
import com.netlinq.presentation.onboarding.OnboardingViewModel;
import com.netlinq.presentation.onboarding.OnboardingViewModel_HiltModules;
import com.netlinq.presentation.settings.SettingsViewModel;
import com.netlinq.presentation.settings.SettingsViewModel_HiltModules;
import com.netlinq.sync.SyncScheduler;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

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
public final class DaggerNetLinqApplication_HiltComponents_SingletonC {
  private DaggerNetLinqApplication_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public NetLinqApplication_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements NetLinqApplication_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public NetLinqApplication_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements NetLinqApplication_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public NetLinqApplication_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements NetLinqApplication_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public NetLinqApplication_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements NetLinqApplication_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public NetLinqApplication_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements NetLinqApplication_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public NetLinqApplication_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements NetLinqApplication_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public NetLinqApplication_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements NetLinqApplication_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public NetLinqApplication_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends NetLinqApplication_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends NetLinqApplication_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends NetLinqApplication_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends NetLinqApplication_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(7).put(LazyClassKeyProvider.com_netlinq_presentation_feedback_FeedbackViewModel, FeedbackViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_netlinq_presentation_history_HistoryViewModel, HistoryViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_netlinq_presentation_home_HomeViewModel, HomeViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_netlinq_presentation_MainViewModel, MainViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_netlinq_presentation_onboarding_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_netlinq_presentation_monitoring_PromptEventViewModel, PromptEventViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_netlinq_presentation_settings_SettingsViewModel, SettingsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_netlinq_presentation_monitoring_PromptEventViewModel = "com.netlinq.presentation.monitoring.PromptEventViewModel";

      static String com_netlinq_presentation_feedback_FeedbackViewModel = "com.netlinq.presentation.feedback.FeedbackViewModel";

      static String com_netlinq_presentation_home_HomeViewModel = "com.netlinq.presentation.home.HomeViewModel";

      static String com_netlinq_presentation_MainViewModel = "com.netlinq.presentation.MainViewModel";

      static String com_netlinq_presentation_history_HistoryViewModel = "com.netlinq.presentation.history.HistoryViewModel";

      static String com_netlinq_presentation_settings_SettingsViewModel = "com.netlinq.presentation.settings.SettingsViewModel";

      static String com_netlinq_presentation_onboarding_OnboardingViewModel = "com.netlinq.presentation.onboarding.OnboardingViewModel";

      @KeepFieldType
      PromptEventViewModel com_netlinq_presentation_monitoring_PromptEventViewModel2;

      @KeepFieldType
      FeedbackViewModel com_netlinq_presentation_feedback_FeedbackViewModel2;

      @KeepFieldType
      HomeViewModel com_netlinq_presentation_home_HomeViewModel2;

      @KeepFieldType
      MainViewModel com_netlinq_presentation_MainViewModel2;

      @KeepFieldType
      HistoryViewModel com_netlinq_presentation_history_HistoryViewModel2;

      @KeepFieldType
      SettingsViewModel com_netlinq_presentation_settings_SettingsViewModel2;

      @KeepFieldType
      OnboardingViewModel com_netlinq_presentation_onboarding_OnboardingViewModel2;
    }
  }

  private static final class ViewModelCImpl extends NetLinqApplication_HiltComponents.ViewModelC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<FeedbackViewModel> feedbackViewModelProvider;

    private Provider<HistoryViewModel> historyViewModelProvider;

    private Provider<HomeViewModel> homeViewModelProvider;

    private Provider<MainViewModel> mainViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<PromptEventViewModel> promptEventViewModelProvider;

    private Provider<SettingsViewModel> settingsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;

      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.feedbackViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.historyViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.homeViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.mainViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.promptEventViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.settingsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(7).put(LazyClassKeyProvider.com_netlinq_presentation_feedback_FeedbackViewModel, ((Provider) feedbackViewModelProvider)).put(LazyClassKeyProvider.com_netlinq_presentation_history_HistoryViewModel, ((Provider) historyViewModelProvider)).put(LazyClassKeyProvider.com_netlinq_presentation_home_HomeViewModel, ((Provider) homeViewModelProvider)).put(LazyClassKeyProvider.com_netlinq_presentation_MainViewModel, ((Provider) mainViewModelProvider)).put(LazyClassKeyProvider.com_netlinq_presentation_onboarding_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_netlinq_presentation_monitoring_PromptEventViewModel, ((Provider) promptEventViewModelProvider)).put(LazyClassKeyProvider.com_netlinq_presentation_settings_SettingsViewModel, ((Provider) settingsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_netlinq_presentation_onboarding_OnboardingViewModel = "com.netlinq.presentation.onboarding.OnboardingViewModel";

      static String com_netlinq_presentation_home_HomeViewModel = "com.netlinq.presentation.home.HomeViewModel";

      static String com_netlinq_presentation_monitoring_PromptEventViewModel = "com.netlinq.presentation.monitoring.PromptEventViewModel";

      static String com_netlinq_presentation_history_HistoryViewModel = "com.netlinq.presentation.history.HistoryViewModel";

      static String com_netlinq_presentation_MainViewModel = "com.netlinq.presentation.MainViewModel";

      static String com_netlinq_presentation_feedback_FeedbackViewModel = "com.netlinq.presentation.feedback.FeedbackViewModel";

      static String com_netlinq_presentation_settings_SettingsViewModel = "com.netlinq.presentation.settings.SettingsViewModel";

      @KeepFieldType
      OnboardingViewModel com_netlinq_presentation_onboarding_OnboardingViewModel2;

      @KeepFieldType
      HomeViewModel com_netlinq_presentation_home_HomeViewModel2;

      @KeepFieldType
      PromptEventViewModel com_netlinq_presentation_monitoring_PromptEventViewModel2;

      @KeepFieldType
      HistoryViewModel com_netlinq_presentation_history_HistoryViewModel2;

      @KeepFieldType
      MainViewModel com_netlinq_presentation_MainViewModel2;

      @KeepFieldType
      FeedbackViewModel com_netlinq_presentation_feedback_FeedbackViewModel2;

      @KeepFieldType
      SettingsViewModel com_netlinq_presentation_settings_SettingsViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.netlinq.presentation.feedback.FeedbackViewModel 
          return (T) new FeedbackViewModel(singletonCImpl.qoeFeedbackRepositoryProvider.get(), singletonCImpl.networkMetricRepositoryProvider.get());

          case 1: // com.netlinq.presentation.history.HistoryViewModel 
          return (T) new HistoryViewModel(singletonCImpl.networkMetricRepositoryProvider.get(), singletonCImpl.qoeFeedbackRepositoryProvider.get());

          case 2: // com.netlinq.presentation.home.HomeViewModel 
          return (T) new HomeViewModel(singletonCImpl.networkMonitorServiceProvider.get(), singletonCImpl.networkMetricRepositoryProvider.get(), singletonCImpl.syncRepositoryProvider.get());

          case 3: // com.netlinq.presentation.MainViewModel 
          return (T) new MainViewModel(singletonCImpl.appPreferencesProvider.get());

          case 4: // com.netlinq.presentation.onboarding.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.deviceRepositoryProvider.get(), singletonCImpl.appPreferencesProvider.get(), singletonCImpl.syncSchedulerProvider.get());

          case 5: // com.netlinq.presentation.monitoring.PromptEventViewModel 
          return (T) new PromptEventViewModel(singletonCImpl.networkMonitoringManagerProvider.get(), singletonCImpl.feedbackNotificationHelperProvider.get());

          case 6: // com.netlinq.presentation.settings.SettingsViewModel 
          return (T) new SettingsViewModel(singletonCImpl.appPreferencesProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends NetLinqApplication_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends NetLinqApplication_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }
  }

  private static final class SingletonCImpl extends NetLinqApplication_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<NetLinqDatabase> provideDatabaseProvider;

    private Provider<QoeFeedbackRepository> qoeFeedbackRepositoryProvider;

    private Provider<NetworkMetricRepository> networkMetricRepositoryProvider;

    private Provider<NetworkTypeDetector> networkTypeDetectorProvider;

    private Provider<SignalStrengthCollector> signalStrengthCollectorProvider;

    private Provider<LatencyMeasurer> latencyMeasurerProvider;

    private Provider<NetworkMonitorService> networkMonitorServiceProvider;

    private Provider<OkHttpClient> provideOkHttpClientProvider;

    private Provider<Retrofit> provideRetrofitProvider;

    private Provider<SupabaseApi> provideSupabaseApiProvider;

    private Provider<AppPreferences> appPreferencesProvider;

    private Provider<DeviceRepository> deviceRepositoryProvider;

    private Provider<SyncRepository> syncRepositoryProvider;

    private Provider<SyncScheduler> syncSchedulerProvider;

    private Provider<NetworkDegradationDetector> networkDegradationDetectorProvider;

    private Provider<NetworkMonitoringManager> networkMonitoringManagerProvider;

    private Provider<FeedbackNotificationHelper> feedbackNotificationHelperProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(Collections.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>emptyMap());
    }

    private QoeFeedbackDao qoeFeedbackDao() {
      return DatabaseModule_ProvideQoeFeedbackDaoFactory.provideQoeFeedbackDao(provideDatabaseProvider.get());
    }

    private NetworkMetricDao networkMetricDao() {
      return DatabaseModule_ProvideNetworkMetricDaoFactory.provideNetworkMetricDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<NetLinqDatabase>(singletonCImpl, 1));
      this.qoeFeedbackRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<QoeFeedbackRepository>(singletonCImpl, 0));
      this.networkMetricRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<NetworkMetricRepository>(singletonCImpl, 2));
      this.networkTypeDetectorProvider = DoubleCheck.provider(new SwitchingProvider<NetworkTypeDetector>(singletonCImpl, 4));
      this.signalStrengthCollectorProvider = DoubleCheck.provider(new SwitchingProvider<SignalStrengthCollector>(singletonCImpl, 5));
      this.latencyMeasurerProvider = DoubleCheck.provider(new SwitchingProvider<LatencyMeasurer>(singletonCImpl, 6));
      this.networkMonitorServiceProvider = DoubleCheck.provider(new SwitchingProvider<NetworkMonitorService>(singletonCImpl, 3));
      this.provideOkHttpClientProvider = DoubleCheck.provider(new SwitchingProvider<OkHttpClient>(singletonCImpl, 10));
      this.provideRetrofitProvider = DoubleCheck.provider(new SwitchingProvider<Retrofit>(singletonCImpl, 9));
      this.provideSupabaseApiProvider = DoubleCheck.provider(new SwitchingProvider<SupabaseApi>(singletonCImpl, 8));
      this.appPreferencesProvider = DoubleCheck.provider(new SwitchingProvider<AppPreferences>(singletonCImpl, 12));
      this.deviceRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<DeviceRepository>(singletonCImpl, 11));
      this.syncRepositoryProvider = DoubleCheck.provider(new SwitchingProvider<SyncRepository>(singletonCImpl, 7));
      this.syncSchedulerProvider = DoubleCheck.provider(new SwitchingProvider<SyncScheduler>(singletonCImpl, 13));
      this.networkDegradationDetectorProvider = DoubleCheck.provider(new SwitchingProvider<NetworkDegradationDetector>(singletonCImpl, 15));
      this.networkMonitoringManagerProvider = DoubleCheck.provider(new SwitchingProvider<NetworkMonitoringManager>(singletonCImpl, 14));
      this.feedbackNotificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<FeedbackNotificationHelper>(singletonCImpl, 16));
    }

    @Override
    public void injectNetLinqApplication(NetLinqApplication netLinqApplication) {
      injectNetLinqApplication2(netLinqApplication);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private NetLinqApplication injectNetLinqApplication2(NetLinqApplication instance) {
      NetLinqApplication_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.netlinq.data.repository.QoeFeedbackRepository 
          return (T) new QoeFeedbackRepository(singletonCImpl.qoeFeedbackDao());

          case 1: // com.netlinq.data.local.NetLinqDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 2: // com.netlinq.data.repository.NetworkMetricRepository 
          return (T) new NetworkMetricRepository(singletonCImpl.networkMetricDao());

          case 3: // com.netlinq.monitoring.NetworkMonitorService 
          return (T) new NetworkMonitorService(singletonCImpl.networkTypeDetectorProvider.get(), singletonCImpl.signalStrengthCollectorProvider.get(), singletonCImpl.latencyMeasurerProvider.get(), singletonCImpl.networkMetricRepositoryProvider.get());

          case 4: // com.netlinq.monitoring.NetworkTypeDetector 
          return (T) new NetworkTypeDetector(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.netlinq.monitoring.SignalStrengthCollector 
          return (T) new SignalStrengthCollector(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 6: // com.netlinq.monitoring.LatencyMeasurer 
          return (T) new LatencyMeasurer();

          case 7: // com.netlinq.data.repository.SyncRepository 
          return (T) new SyncRepository(singletonCImpl.provideSupabaseApiProvider.get(), singletonCImpl.deviceRepositoryProvider.get(), singletonCImpl.networkMetricRepositoryProvider.get(), singletonCImpl.qoeFeedbackRepositoryProvider.get());

          case 8: // com.netlinq.data.remote.SupabaseApi 
          return (T) NetworkModule_ProvideSupabaseApiFactory.provideSupabaseApi(singletonCImpl.provideRetrofitProvider.get());

          case 9: // retrofit2.Retrofit 
          return (T) NetworkModule_ProvideRetrofitFactory.provideRetrofit(singletonCImpl.provideOkHttpClientProvider.get());

          case 10: // okhttp3.OkHttpClient 
          return (T) NetworkModule_ProvideOkHttpClientFactory.provideOkHttpClient();

          case 11: // com.netlinq.data.repository.DeviceRepository 
          return (T) new DeviceRepository(singletonCImpl.appPreferencesProvider.get());

          case 12: // com.netlinq.data.preferences.AppPreferences 
          return (T) new AppPreferences(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 13: // com.netlinq.sync.SyncScheduler 
          return (T) new SyncScheduler(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 14: // com.netlinq.monitoring.NetworkMonitoringManager 
          return (T) new NetworkMonitoringManager(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.networkMonitorServiceProvider.get(), singletonCImpl.networkTypeDetectorProvider.get(), singletonCImpl.networkDegradationDetectorProvider.get(), singletonCImpl.appPreferencesProvider.get());

          case 15: // com.netlinq.monitoring.NetworkDegradationDetector 
          return (T) new NetworkDegradationDetector();

          case 16: // com.netlinq.notifications.FeedbackNotificationHelper 
          return (T) new FeedbackNotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
