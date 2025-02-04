package com.sygic.example.ipcdemo3d.ui.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import kotlinx.coroutines.launch

class MainActivityViewModel: ViewModel() {

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

}
