package com.sygic.example.ipcdemo3d.ui.poisupdate

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import kotlinx.coroutines.launch

class PoisUpdateViewModel: ViewModel() {
    private val _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    fun updatePois(command: String) {
        viewModelScope.launch {
            _uiState.value = UIState()
            _uiState.value = UIState(SdkHelper.updatePois(command))
        }
    }
}

data class UIState(
    val updateResult: String? = null,
)
