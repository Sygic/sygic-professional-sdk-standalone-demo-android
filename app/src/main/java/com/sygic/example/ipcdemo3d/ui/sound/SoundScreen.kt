package com.sygic.example.ipcdemo3d.ui.sound

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sygic.example.ipcdemo3d.R

@Composable
@Preview
fun SoundScreen(viewModel: SoundScreenViewModel = viewModel()) {
    val soundText = rememberTextFieldState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                soundText, label = { Text("TTS") }, modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(
                { viewModel.say(soundText.text.toString()) },
                enabled = !soundText.text.isEmpty()
            ) {
                Text("Say")
            }
        }
        Spacer(Modifier.height(16.dp))
        Text(stringResource(R.string.sound_tts_help), Modifier.fillMaxWidth())
    }
}
