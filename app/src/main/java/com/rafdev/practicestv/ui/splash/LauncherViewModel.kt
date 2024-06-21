package com.rafdev.practicestv.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor() : ViewModel() {


    private val _showSplash = MutableStateFlow(false)
    val showSplash: StateFlow<Boolean> get() = _showSplash

    init {
        viewModelScope.launch {
            delay(1000L)
            _showSplash.value = true
        }
    }
}