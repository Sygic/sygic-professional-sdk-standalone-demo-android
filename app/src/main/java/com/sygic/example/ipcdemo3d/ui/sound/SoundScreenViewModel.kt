package com.sygic.example.ipcdemo3d.ui.sound

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import kotlinx.coroutines.launch

class SoundScreenViewModel: ViewModel() {

    fun say(tts: String) {
        viewModelScope.launch {
            SdkHelper.ttsSpeak(tts)
        }
    }

}
