package com.sygic.example.ipcdemo3d.ui.itinerary

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sygic.sdk.remoteapi.model.Position

@Composable
fun AddStopoffDialog(dialogDismissed: () -> Unit, addStopoff: (Pair<Position, Boolean>) -> Unit) {
    val latFieldState = rememberTextFieldState()
    val lonFieldState = rememberTextFieldState()
    val latError = remember { mutableStateOf(false) }
    val lonError = remember { mutableStateOf(false) }
    val isVisibleState = remember { mutableStateOf(false) }

    Dialog(dialogDismissed) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .padding(8.dp)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                TextField(latFieldState, label = { Text("Latitude") }, modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.height(8.dp))
                TextField(lonFieldState, label = { Text("Longitude") }, modifier = Modifier.fillMaxWidth())
                Row(verticalAlignment = CenterVertically) {
                    Checkbox(isVisibleState.value, onCheckedChange = { isVisibleState.value = it })
                    Text("Visible")
                }
                Spacer(Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            val (y, x) = getCoordinates(latFieldState.text.toString(), lonFieldState.text.toString())
                            if (x != null && y != null) {
                                addStopoff.invoke(Pair(Position(x, y), isVisibleState.value))
                            } else {
                                latError.value = y == null
                                lonError.value = x == null
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Add")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { dialogDismissed() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

fun getCoordinates(lat: String, lon: String): Pair<Int?, Int?> {
    val x = lat.toIntOrNull()
    val y = lon.toIntOrNull()
    return Pair(y, x)
}

@Composable
@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
fun AddStopoffDialogPreview() {
    AddStopoffDialog({}, {})
}
