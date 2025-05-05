package com.sygic.example.ipcdemo3d.ui.itinerary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sygic.example.ipcdemo3d.domain.entity.StartStopRoute

@Composable
fun AddItineraryDialog(onDismiss: () -> Unit, actionAdd:(StartStopRoute) -> Unit) {
    val startLatState = rememberTextFieldState()
    val startLonState = rememberTextFieldState()
    val stopLatState = rememberTextFieldState()
    val stopLonState = rememberTextFieldState()

    val startLatError = remember { mutableStateOf(false) }
    val startLonError = remember { mutableStateOf(false) }
    val stopLatError = remember { mutableStateOf(false) }
    val stopLonError = remember { mutableStateOf(false) }

    Dialog(onDismiss) {
        Surface(
            Modifier
                .fillMaxWidth()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text("Start", style = MaterialTheme.typography.titleMedium)
                TextField(
                    startLatState, label = { Text("Latitude") }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                TextField(
                    startLonState, label = { Text("Longitude") }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )

                Spacer(Modifier.height(16.dp))

                Text("Stop", style = MaterialTheme.typography.titleMedium)
                TextField(
                    stopLatState, label = { Text("Latitude") }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                TextField(
                    stopLonState, label = { Text("Longitude") }, modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
                Row(Modifier.fillMaxWidth()) {
                    Button(modifier = Modifier.fillMaxWidth().weight(1f), onClick = {
                        val startLat = startLatState.text.toString().toIntOrNull()
                        if (startLat == null) {
                            startLatError.value = true
                            return@Button
                        }
                        val startLon = startLonState.text.toString().toIntOrNull()
                        if (startLon == null) {
                            startLonError.value = true
                            return@Button
                        }
                        val stopLat = stopLatState.text.toString().toIntOrNull()
                        if (stopLat == null) {
                            stopLatError.value = true
                            return@Button
                        }
                        val stopLon = stopLonState.text.toString().toIntOrNull()
                        if (stopLon == null) {
                            stopLonError.value = true
                            return@Button
                        }
                        actionAdd.invoke(StartStopRoute(startLat, startLon, stopLat, stopLon))
                    }) { Text("Set") }
                    Spacer(Modifier.width(8.dp))
                    Button(modifier = Modifier.fillMaxWidth().weight(1f), onClick = onDismiss) { Text("Cancel") }
                }
            }
        }
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun AddItineraryDialogPreview() {
    AddItineraryDialog({}, {})
}
