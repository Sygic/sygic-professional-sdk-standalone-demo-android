package com.sygic.example.ipcdemo3d.ui.routeinfo

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import com.sygic.sdk.remoteapi.model.RouteInfo
import kotlinx.coroutines.launch

class RouteInfoScreenViewModel: ViewModel() {

    private val _uiState = mutableStateOf(RouteInfoScreenUiState())
    val uiState: State<RouteInfoScreenUiState> = _uiState

    init {
        refresh()
    }

    private fun refresh() {
        viewModelScope.launch {
            val routeInfo = SdkHelper.getRouteInfo()
            val routeStatus = SdkHelper.getRouteStatus()
            _uiState.value = RouteInfoScreenUiState(routeInfo, routeStatus)
        }
    }

}

data class RouteInfoScreenUiState(
    val routeInfo: RouteInfo? = null,
    val routeStatus: String? = null
)
