package com.sygic.example.ipcdemo3d.ui.main

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import com.sygic.example.ipcdemo3d.utils.getOriginalFileName
import com.sygic.sdk.remoteapi.Api
import kotlinx.coroutines.launch

class MainActivityViewModel(val app: Application): AndroidViewModel(app) {

    val isAppRunning = SdkHelper.isAppRunning
    val isServiceConnected = SdkHelper.isServiceConnected

    fun refreshApplicationState() {
        viewModelScope.launch {
            SdkHelper.isApplicationRunning()
        }
    }

    fun initSdk(context: Context) {
        SdkHelper.init(context)
    }

    fun importFile(uri: Uri) {
        app.contentResolver.openInputStream(uri)?.use {
            val data = it.readBytes()
            if (isAppRunning.value && isServiceConnected.value) {
                Api.importFile(data, uri.getOriginalFileName(app), false)?.let { result ->
                    Log.v("Api.importFile", "Import relative path: $result")
                }
            }
        }
    }

}
