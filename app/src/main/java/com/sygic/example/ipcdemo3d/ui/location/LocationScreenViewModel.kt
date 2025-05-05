package com.sygic.example.ipcdemo3d.ui.location

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import kotlinx.coroutines.launch

class LocationScreenViewModel: ViewModel() {

    private val _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    fun reverseGeo(lat: String, lon: String) {
        val latInt = lat.toIntOrNull()
        val lonInt = lon.toIntOrNull()
        _uiState.value = UIState(lonInt == null, latInt == null)
        if (latInt != null && lonInt != null) {
            viewModelScope.launch {
                _uiState.value = UIState(result = SdkHelper.reverseGeo(latInt, lonInt).toString())
            }
        }
    }

    fun roadInfo(lat: String, lon: String) {
        val latInt = lat.toIntOrNull()
        val lonInt = lon.toIntOrNull()
        _uiState.value = UIState(lonInt == null,  latInt == null)
        if (latInt != null && lonInt != null) {
            viewModelScope.launch {
                _uiState.value = UIState(result = SdkHelper.getRoadInfo(latInt, lonInt).toString())
            }
        }
    }

    fun navigateToAddress(address: String?) {
        address?.let {
            viewModelScope.launch {
                SdkHelper.navigateTo(it)
            }
        }
    }

    fun navigateToPoint(lat: String, lon: String) {
        val latInt = lat.toIntOrNull()
        val lonInt = lon.toIntOrNull()
        _uiState.value = UIState(lonInt == null, latInt == null)
        if (latInt != null && lonInt != null) {
            viewModelScope.launch {
                SdkHelper.navigateToPoint(latInt, lonInt).toString()
            }
        }
    }
}

data class UIState(
    val lonError: Boolean = false,
    val latError: Boolean = false,
    val result: String? = null
)
