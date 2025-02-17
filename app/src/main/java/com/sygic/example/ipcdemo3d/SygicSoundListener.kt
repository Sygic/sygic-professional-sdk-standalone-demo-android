package com.sygic.example.ipcdemo3d

import android.util.Log
import com.sygic.sdk.remoteapi.OnSoundListener
import com.sygic.sdk.remoteapi.OnTtsListener

class SygicSoundListener : OnSoundListener, OnTtsListener {
    override fun onSound(arg0: Boolean) {
        Log.d(LOG_TAG, "onSound $arg0")
    }

    override fun onTts(arg0: String?) {
        Log.d(LOG_TAG, "onTts $arg0")
    }

    companion object {
        private val LOG_TAG = SygicSoundListener::class.java.simpleName
    }
}
