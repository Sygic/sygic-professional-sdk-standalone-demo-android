package com.sygic.example.ipcdemo3d.domain

import android.R.attr.x
import android.R.attr.y
import android.content.Context
import android.os.RemoteException
import android.util.Log
import com.sygic.example.ipcdemo3d.BuildConfig
import com.sygic.example.ipcdemo3d.SdkApplication
import com.sygic.example.ipcdemo3d.SygicSoundListener
import com.sygic.example.ipcdemo3d.domain.SdkHelper.isServiceConnected
import com.sygic.sdk.remoteapi.Api
import com.sygic.sdk.remoteapi.Api.isApplicationRunning
import com.sygic.sdk.remoteapi.ApiCallback
import com.sygic.sdk.remoteapi.ApiDialog
import com.sygic.sdk.remoteapi.ApiItinerary
import com.sygic.sdk.remoteapi.ApiLocation
import com.sygic.sdk.remoteapi.ApiNavigation
import com.sygic.sdk.remoteapi.ApiPoi
import com.sygic.sdk.remoteapi.ApiTts
import com.sygic.sdk.remoteapi.callback.OnSearchListener
import com.sygic.sdk.remoteapi.events.ApiEvents
import com.sygic.sdk.remoteapi.exception.GeneralException
import com.sygic.sdk.remoteapi.model.Poi
import com.sygic.sdk.remoteapi.model.PoiCategory
import com.sygic.sdk.remoteapi.model.Position
import com.sygic.sdk.remoteapi.model.StopOffPoint
import com.sygic.sdk.remoteapi.model.WayPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.ArrayList

const val SDK_TIMEOUT = 10000

object SdkHelper {

    private val _isAppRunning = MutableStateFlow(false)
    val isAppRunning: StateFlow<Boolean> get() = _isAppRunning

    private val _isServiceConnected = MutableStateFlow(false)
    val isServiceConnected: StateFlow<Boolean> get() = _isServiceConnected

    private val _events = MutableSharedFlow<Int>()
    val events: Flow<Int> = _events

    val soundListener = SygicSoundListener()

    private val apiCallback = object : ApiCallback {
        override fun onEvent(event: Int, data: String?) {
            GlobalScope.launch {
                _events.emit(event)
            }
            when (event) {
                ApiEvents.EVENT_APP_EXIT -> {
                    Api.getInstance().disconnect()
                    _isServiceConnected.value = false
                    GlobalScope.launch {
                        isApplicationRunning()
                    }
                }
            }
        }

        override fun onServiceConnected() {
            _isServiceConnected.value = true
            Api.getInstance().setOnSoundListener(soundListener);
            Api.getInstance().setOnTtsListener(soundListener);
            GlobalScope.launch {
                isApplicationRunning()
            }
        }

        override fun onServiceDisconnected() {
            _isServiceConnected.value = false
            GlobalScope.launch {
                isApplicationRunning()
            }
        }
    }

    suspend fun getRoadInfo(lat: Int, lon: Int) =
        runIO {
            try {
                ApiLocation.getLocationRoadInfo(Position(lon, lat), SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    suspend fun reverseGeo(lat: Int, lon: Int) =
        runIO {
            try {
                ApiLocation.getLocationAddressInfo(Position(lon, lat), SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    suspend fun navigateToPoint(lat: Int, lon: Int) {
        runIO {
            try {
                val address = ApiLocation.getLocationAddressInfo(Position(lon, lat), SdkApplication.MAX)
                ApiNavigation.startNavigation(WayPoint(address, lon, lat), 0, false, SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getRouteInfo() =
        runIO {
            try {
                ApiNavigation.getRouteInfo(true, SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    suspend fun getRouteStatus() =
        runIO {
            try {
                ApiNavigation.getRouteStatus(SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun search(value: String): String? = suspendCancellableCoroutine { continuation ->
        val listener = object : OnSearchListener() {
            override fun onResult(input: String?, waypoints: ArrayList<WayPoint?>?, resultCode: Int) {
                    val result: String = if (resultCode != RC_OK) {
                      "Search failed"
                    } else {
                        waypoints
                            ?.filterNotNull()
                            ?.joinToString(";") {
                                "address: ${it.strAddress}\n${it.location.x}, ${it.location.y}\n\n"
                            } ?: "Search is empty"
                    }
                continuation.resume(result, null)
            }
        }
        try {
            ApiLocation.searchLocation(value, listener, SdkApplication.MAX)
        } catch (e: Exception) {
            e.printStackTrace()
            continuation.resume("Search failed", null)
        }
    }

    suspend fun ttsSpeak(text: String) {
        runIO {
            try {
                ApiTts.playSound(text, SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getLibVersion() =
        try {
            runIO {
                Api.getLibVersion()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }

    suspend fun getMapVersion() =
        try {
            runIO {
                Api.getMapVersion("SVK", SdkApplication.MAX)
            }
        } catch (e: Exception) {
            null
        }

    suspend fun getDeviceId() = try {
        runIO {
            Api.getUniqueDeviceId(SdkApplication.MAX)
        }
    } catch (e: Exception) {
        null
    }

    suspend fun appVersion() = runIO {
        try {
            Api.getApplicationVersion(SdkApplication.MAX);
        } catch (e: GeneralException) {
            e.printStackTrace();
            null
        }
    }

    suspend fun startNaviInForeground() {
        try {
            runIO {
                Api.getInstance().show(false)
            }
        } catch (e: RemoteException) {
            e.printStackTrace();
        }
        isApplicationRunning()
    }

    suspend fun updatePois(command: String) =
        runIO {
            try {
                ApiPoi.updatePois(command, ApiPoi.FORMAT_TEXT, SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }



    fun connect() {
        try {
            Api.getInstance().connect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        try {
            Api.getInstance().disconnect()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _isServiceConnected.value = false
    }

    suspend fun isApplicationRunning(timeout: Int = SDK_TIMEOUT) {
        _isAppRunning.value = try {
            if (isServiceConnected.value) {
                runIO {
                    Api.isApplicationRunning(timeout)
                }
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun init(context: Context) {
        val apiClass = if (BuildConfig.FLAVOR == "fleet") Api.CLASS_FLEET else Api.CLASS_TRUCK
        Api.init(context, BuildConfig.NAVI_PACKAGE, apiClass, apiCallback)
    }

    suspend fun flashMessage5s() {
        runIO {
            ApiDialog.flashMessage("Bring to background from demo after 5 sec...", SdkApplication.MAX)
        }
    }

    suspend fun showMessage() =
        try {
            runIO {
                val result = ApiDialog.showMessage("This is sample message", DialogType.YES_NO.value, true, 0)
                DialogResult.entries.firstOrNull { it.value == result }
            }
        } catch (e: RemoteException) {
            e.printStackTrace();
            null
        }

    suspend fun flashMessage() {
        try {
            runIO {
                ApiDialog.flashMessage("This is sample message", SdkApplication.MAX);
                Api.getInstance().show(false);
            }
        } catch (e: RemoteException) {
            e.printStackTrace();
        }
    }

    private suspend fun <T> runIO(block: suspend () -> T): T {
        return withContext(Dispatchers.IO) {
            block()
        }
    }

    suspend fun endNavigation() {
        runIO {
            try {
                Api.endApplication(SdkApplication.MAX)
                isApplicationRunning()
            } catch (e: GeneralException) {
                e.printStackTrace();
            }
        }
    }

    suspend fun getItineraryList(): List<StopOffPoint> =
        runIO {
            try {
                ApiItinerary.getItineraryList("default", SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

    suspend fun getPoiCategories(): List<PoiCategory>? =
        runIO {
            try {
                ApiPoi.getPoiCategoryList(SdkApplication.MAX)
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

    suspend fun getPoiList(category: String, searchAddress: Boolean = true): List<Poi>? =
        runIO {
            try {
                ApiPoi.getPoiList(category, searchAddress, SdkApplication.MAX);
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }

    suspend fun navigateTo(address: String) {
        runIO {
            try {
                val pos = ApiLocation.locationFromAddress(address, false, true, 0);
                ApiNavigation.startNavigation(WayPoint(address, pos.x, pos.y), 0, false, 0)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


enum class DialogResult(val value: Int) {
    NEGATIVE(101), POSITIVE(201)
}

enum class DialogType(val value: Int) {
    OK(1), OK_CANCEL(2), YES_NO(3)
}
