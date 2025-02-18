package com.sygic.example.ipcdemo3d

import android.app.Application
import android.os.Environment


/**
 * Used to save global state and to provide application general constants
 */
class SdkApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val prefix = Environment.getExternalStorageDirectory()
            .toString() + "/Android/data/" + BuildConfig.NAVI_PACKAGE + "/files/SygicTruck/"
        PATH_GPS_LOG = prefix + "Res/gpslogs"
        PATH_ROUTE = prefix + "Res/routes"
        PATH_ITINERARY = prefix + "Res/itinerary"
        PATH_BTIMAP = prefix + "Res/icons/rupi"
        PATH_GF = prefix + "Res/geofiles"
        PATH_VOICES_2D = prefix + "Res/voices"
        PATH_VOICES_PERSON_2D = prefix + "LoquendoTTS/modules"
        PATH_LANGS = prefix + "Res/skin/langs"
    }

    companion object {
        var PATH_GPS_LOG: String? = null
        var PATH_ROUTE: String? = null
        var PATH_ITINERARY: String? = null
        var PATH_BTIMAP: String? = null
        var PATH_GF: String? = null
        var PATH_VOICES_2D: String? = null
        var PATH_VOICES_PERSON_2D: String? = null
        var PATH_LANGS: String? = null

        const val MAX: Int = 20000

        private const val PACKAGE_NAME = "com.sygic"
        const val INTENT_ACTION_APP_STARTED = "$PACKAGE_NAME.intent.action.app_started"
        const val INTENT_ACTION_APP_STARTED_LOCAL = "$PACKAGE_NAME.intent.action.app_started_local"
        const val INTENT_ACTION_AM_WAKEUP = "$PACKAGE_NAME.intent.action.am_wakeup"
    }
}
