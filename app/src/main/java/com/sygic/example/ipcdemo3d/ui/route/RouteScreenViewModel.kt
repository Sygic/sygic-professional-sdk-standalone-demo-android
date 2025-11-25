package com.sygic.example.ipcdemo3d.ui.route

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import com.sygic.sdk.remoteapi.model.StopOffPoint
import kotlinx.coroutines.launch

class RouteScreenViewModel : ViewModel() {

    private val _routes = mutableStateOf<List<StopOffPoint>>(emptyList())
    val routes: State<List<StopOffPoint>> = _routes

    init {
        viewModelScope.launch {
            _routes.value = SdkHelper.getItineraryList()
        }
    }

}
