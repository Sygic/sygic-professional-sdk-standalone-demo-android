package com.sygic.example.ipcdemo3d

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sygic.sdk.remoteapi.Api

class StateChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            SdkApplication.INTENT_ACTION_APP_STARTED -> {
                val i = Intent().apply {
                    action = SdkApplication.INTENT_ACTION_APP_STARTED_LOCAL
                }
                context.sendBroadcast(i)
            }

            SdkApplication.INTENT_ACTION_AM_WAKEUP -> {
                Api.getInstance().bringApplicationToBackground()
            }
        }
    }

}
