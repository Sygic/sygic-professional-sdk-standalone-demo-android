package com.sygic.example.ipcdemo3d.ui.pois

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import com.sygic.sdk.remoteapi.model.Poi
import com.sygic.sdk.remoteapi.model.PoiCategory
import kotlinx.coroutines.launch

class PoisScreenViewModel: ViewModel() {

    private val _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    init {
        viewModelScope.launch {
            val categories = SdkHelper.getPoiCategories()?.sortedBy { it.name.toIntOrNull() ?: -1 }
            _uiState.value = UIState(categories)
            refreshPois(if (categories.isNullOrEmpty()) null else categories[0])
        }
    }

    private fun refreshPois(refreshCategory: PoiCategory?) {
        viewModelScope.launch {
            refreshCategory?.let {
                _uiState.value = _uiState.value.copy(pois = SdkHelper.getPoiList(it.name))
            }
        }
    }

    fun categorySelected(category: PoiCategory) {
        refreshPois(category)
    }

}

data class UIState(
    val categories: List<PoiCategory>? = emptyList(),
    val pois: List<Poi>? = emptyList(),
)
