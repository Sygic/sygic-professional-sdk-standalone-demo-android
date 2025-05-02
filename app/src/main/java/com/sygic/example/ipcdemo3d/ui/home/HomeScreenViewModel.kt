package com.sygic.example.ipcdemo3d.ui.home

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.RemoteException
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sygic.example.ipcdemo3d.SdkApplication
import com.sygic.example.ipcdemo3d.StateChangeReceiver
import com.sygic.example.ipcdemo3d.domain.DialogResult
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import com.sygic.example.ipcdemo3d.utils.runIO
import com.sygic.sdk.remoteapi.Api
import com.sygic.sdk.remoteapi.exception.GeneralException

class HomeScreenViewModel() : ViewModel() {

    private val _ui = mutableStateOf(UIState())
    val ui: State<UIState> get() = _ui

    init {
        runIO {
            SdkHelper.isServiceConnected.collect {
                _ui.value = _ui.value.copy(isConnected = it)
            }
        }

        runIO {
            SdkHelper.isAppRunning.collect {
                _ui.value = _ui.value.copy(isAppRunning = it)
            }
        }
    }

    fun connect() {
        SdkHelper.connect()
    }

    fun startNaviForeg() {
        runIO {
            SdkHelper.startNaviInForeground()
        }
    }

    fun bringForeg5s(context: Context) {

        runIO {
            try {
                SdkHelper.flashMessage5s()
                Api.getInstance().show(false)
            } catch (e: RemoteException) {
                e.printStackTrace()
            } catch (e: GeneralException) {
                e.printStackTrace()
            }
        }

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val amIntent = Intent(context, StateChangeReceiver::class.java).apply {
            action = SdkApplication.INTENT_ACTION_AM_WAKEUP
        }

        val pi = PendingIntent.getBroadcast(context, 0, amIntent, PendingIntent.FLAG_IMMUTABLE)
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pi)
    }

    fun endNavigation() {
        runIO {
            SdkHelper.endNavigation()
        }
    }

    fun appVersion() {
        runIO {
            _ui.value = ui.value.copy(message = SdkHelper.appVersion()?.toString())
        }
    }

    fun deviceId() {
        runIO {
            _ui.value = ui.value.copy(message = SdkHelper.getDeviceId())
        }
    }

    fun mapVersion() {
        runIO {
            _ui.value = ui.value.copy(message = SdkHelper.getMapVersion())
        }
    }

    fun flashMessage() {
        runIO {
            SdkHelper.flashMessage()
        }
    }

    fun showMessage() {
        runIO {
            _ui.value = ui.value.copy(
                message =
                    when (SdkHelper.showMessage()) {
                        DialogResult.NEGATIVE -> "Dialog result: Negative"
                        DialogResult.POSITIVE -> "Dialog result: Positive"
                        null -> "Dialog result: None"
                    }
            )
        }
    }

    fun sdkVersion() {
        runIO {
            _ui.value = ui.value.copy(message = SdkHelper.getLibVersion())
        }
    }

    fun disconnect() {
        SdkHelper.disconnect()
    }

}

data class UIState(
    val status: String? = null,
    val isConnected: Boolean = false,
    val isAppRunning: Boolean = false,
    val message: String? = null
)
