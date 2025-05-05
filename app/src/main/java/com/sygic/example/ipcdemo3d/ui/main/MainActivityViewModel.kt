package com.sygic.example.ipcdemo3d.ui.main

import android.app.Application
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import com.sygic.example.ipcdemo3d.utils.getOriginalFileName
import com.sygic.example.ipcdemo3d.utils.runIO
import com.sygic.sdk.remoteapi.Api

class MainActivityViewModel(val app: Application) : AndroidViewModel(app) {

    private val _uiState = mutableStateOf(UIState())
    val uiState: State<UIState> = _uiState

    val isAppRunning = SdkHelper.isAppRunning
    val isServiceConnected = SdkHelper.isServiceConnected

    fun refreshApplicationState() {
        runIO {
            SdkHelper.isApplicationRunning()
        }
    }

    fun initSdk(context: Context) {
        SdkHelper.init(context)
    }

    fun importFile(uri: Uri) {
        runIO {
            app.contentResolver.openInputStream(uri)?.use {
                _uiState.value = UIState(uploading = true, uploadingProgress = 0f)

                val size = it.available()

                val buf = ByteArray(512 * 1024)
                var readBytes: Int
                var totalBytes = 0
                var filename = ""
                var append = false
                val originalFileName = uri.getOriginalFileName(app)

                do {
                    readBytes = it.read(buf)
                    totalBytes += readBytes

                    if (readBytes > 0) {

                        val uploadResult = Api.importFile(buf.copyOfRange(0, readBytes), originalFileName, append)
                        if (uploadResult != null) {
                            filename = uploadResult
                        } else {
                            _uiState.value = UIState(uploading = false, uploadingResult = "Failed to upload file")
                            break
                        }
                    }

                    if (!append) append = true

                    _uiState.value = UIState(uploading = true, uploadingProgress = totalBytes / size.toFloat())

                } while (readBytes > 0)

                _uiState.value = UIState(uploading = false, uploadingResult = filename)
                Log.v("Api.importFile", "Import result: $filename")
            }
        }
    }

    fun uploadingResultShown() {
        _uiState.value = UIState(uploading = false, uploadingResult = null)
    }
}

data class UIState(
    val uploading: Boolean = false,
    val uploadingProgress: Float = 0.0f,
    val uploadingResult: String? = null
)
