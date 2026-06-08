package com.netlinq.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.netlinq.data.preferences.AppPreferences
import com.netlinq.presentation.navigation.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    appPreferences: AppPreferences
) : ViewModel() {

    val startDestination: StateFlow<String> = appPreferences.onboardingComplete
        .map { complete ->
            if (complete) Routes.MAIN else Routes.ONBOARDING
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = Routes.ONBOARDING
        )

    val themeMode: StateFlow<Int> = appPreferences.themeMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppPreferences.THEME_MODE_SYSTEM
        )
}
