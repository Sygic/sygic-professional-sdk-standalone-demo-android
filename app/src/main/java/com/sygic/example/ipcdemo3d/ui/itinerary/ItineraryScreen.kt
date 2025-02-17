package com.sygic.example.ipcdemo3d.ui.itinerary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sygic.example.ipcdemo3d.ui.common.PointInfoDialog
import com.sygic.sdk.remoteapi.model.StopOffPoint

@Composable
@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
fun ItineraryScreen(viewModel: ItineraryScreenViewModel = viewModel()) {

    val state = viewModel.uiState.value
    val addEntryAction = remember { mutableStateOf(false) }
    val addItineraryAction = remember { mutableStateOf(false) }
    val pointInfoState = remember { mutableStateOf<StopOffPoint?>(null) }

    if (addEntryAction.value) {
        AddStopoffDialog({ addEntryAction.value = false }) {
            viewModel.addEntry(it.first, it.second)
            addEntryAction.value = false
        }
    }

    if (addItineraryAction.value) {
        AddItineraryDialog({ addItineraryAction.value = false }) {
            viewModel.addItinerary(it)
            addItineraryAction.value = false
        }
    }

    pointInfoState.value?.let {
        PointInfoDialog(it) { pointInfoState.value = null }
    }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            "Test1 itinerary",
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            addEntryAction.value = true
        }) { Text("Add Entry") }
        Text("Waypoints: ")

        Spacer(Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            state.waypoints?.let {
                itemsIndexed(it) { index, entry ->
                    Column {
                        WaipointItem(entry, index != 0 && index != it.lastIndex, index, {pointInfoState.value = it}) {
                            viewModel.deleteWaypoint(it)
                        }
                        if (index < it.lastIndex) {
                            Spacer(Modifier.height(4.dp))
                            HorizontalDivider(Modifier.fillMaxWidth())
                            Spacer(Modifier.height(4.dp))
                        }
                    }
                }
            }
        }

        Button(modifier = Modifier.fillMaxWidth(), onClick = { viewModel.calculateRoute() }) { Text("Calculate route") }
        Row {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = { addItineraryAction.value = true }) { Text("Add itinerary") }
            Spacer(Modifier.width(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                onClick = { viewModel.deleteItinerary() }) { Text("Delete itinerary") }
        }
    }
}

@Composable
fun WaipointItem(
    entry: StopOffPoint,
    isDeletable: Boolean,
    index: Int,
    onClickAction: (StopOffPoint) -> Unit,
    deleteAction: (Int) -> Unit
) {
    Row(verticalAlignment = CenterVertically, modifier = Modifier.clickable { onClickAction.invoke(entry) }) {
        Text(
            text = getStopOffTitle(entry),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )
        Spacer(Modifier.width(8.dp))
        if (isDeletable) {
            Button({ deleteAction.invoke(index) }) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}

private fun getStopOffTitle(entry: StopOffPoint): String =
    when {
        !entry.caption.isNullOrEmpty() -> entry.caption
        !entry.address.isNullOrEmpty() -> entry.address
        entry.location != null -> entry.location.toString()
        else -> "No title"
    }
