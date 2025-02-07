package com.sygic.example.ipcdemo3d.ui.itinerary

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import com.sygic.example.ipcdemo3d.domain.entity.StartStopRoute
import com.sygic.sdk.remoteapi.model.Position
import com.sygic.sdk.remoteapi.model.StopOffPoint
import kotlinx.coroutines.launch

class ItineraryScreenViewModel : ViewModel() {

    private val _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState


    init {
        refreshItinerary()
    }

    fun addEntry(position: Position, isVisible: Boolean) {
        viewModelScope.launch {
            try {
                SdkHelper.addStopoffPoint(position, isVisible)
                refreshItinerary()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UIState(message = "Failed to add entry")
            }
        }

    }

    fun calculateRoute() {
        viewModelScope.launch {
            _uiState.value = UIState(
                try {
                    SdkHelper.setRoute()
                    "Route calculated"
                } catch (e: Exception) {
                    "No itinerary added"
                },
                uiState.value.waypoints
            )
        }
    }

    private fun refreshItinerary() {
        viewModelScope.launch {
            try {
                val list = SdkHelper.getItineraryList("test1")
                _uiState.value = UIState(waypoints = list)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UIState(message = "No itinerary fetched")
            }
        }
    }

    fun addItinerary(route: StartStopRoute) {
        viewModelScope.launch {
            try {
                SdkHelper.addItinerary(route, "test1")
                refreshItinerary()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UIState(message = "Failed to add itinerary")
            }
        }
    }

    fun deleteItinerary() {
        viewModelScope.launch {
            try {
                SdkHelper.deleteItinerary("test1")
                refreshItinerary()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UIState(message = "Failed to delete itinerary")
            }
        }
    }

    fun deleteWaypoint(index: Int) {
        viewModelScope.launch {
            try {
                SdkHelper.deleteStopOff(index, "test1")
                refreshItinerary()
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = UIState(message = "Failed to delete entry")
            }
        }
    }
}

data class UIState(
    val message: String? = null,
    val waypoints: List<StopOffPoint>? = null
)
