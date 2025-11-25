package com.sygic.example.ipcdemo3d.ui.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import kotlinx.coroutines.launch

class SearchScreenViewModel: ViewModel() {

    private val _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    fun search(string: String) {
        viewModelScope.launch {
            _uiState.value = UIState()
            _uiState.value = UIState(SdkHelper.search(string))
        }
    }
}

data class UIState(
    val searchResult: String? = null,
)
