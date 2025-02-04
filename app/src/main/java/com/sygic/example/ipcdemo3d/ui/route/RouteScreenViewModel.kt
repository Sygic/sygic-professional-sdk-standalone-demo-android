package com.sygic.example.ipcdemo3d.ui.route

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class RouteScreenViewModel : ViewModel() {
    val isApiEnabled =
        SdkHelper.isAppRunning.combine(SdkHelper.isServiceConnected) { isAppRunning, isServiceConnected ->
            isAppRunning && isServiceConnected
        }

    private val _routes = mutableStateOf(emptyList<String>())
    val routes: State<List<String>> = _routes

    init {
        viewModelScope.launch {
            _routes.value = SdkHelper.getItineraryList().map {
                "${if (it.isVisited) "✓" else "-"} ${it.caption ?: it.address}"
            }
        }
    }

}
