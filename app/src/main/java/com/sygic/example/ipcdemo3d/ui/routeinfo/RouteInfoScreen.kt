package com.sygic.example.ipcdemo3d.ui.routeinfo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sygic.example.ipcdemo3d.utils.formatTime
import com.sygic.sdk.remoteapi.model.RouteInfo

@Composable
fun RouteInfoScreen(viewModel: RouteInfoScreenViewModel = viewModel()) {

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        val state = viewModel.uiState.value
        if (state.routeInfo == null) {
            EmptyRoute()
        } else {
            RouteInfo(state.routeInfo)
            Spacer(Modifier.height(8.dp))
            HorizontalDivider()
            Spacer(Modifier.height(8.dp))
            RouteState(state.routeStatus)
        }
    }
}

@Composable
fun RouteInfo(routeInfo: RouteInfo) {
    RouteInfoRow("Total distance", "${routeInfo.totalDistance} m")
    RouteInfoRow("Total time", routeInfo.totalTime.formatTime())
    RouteInfoRow("Remaining distance", "${routeInfo.remainingDistance} m")
    RouteInfoRow("Remaining time", routeInfo.remainingTime.formatTime())
    RouteInfoRow("Status", routeInfo.status.toString())
    val eta = routeInfo.estimatedTimeArrival
    RouteInfoRow("ETA", "${eta.hour}:${eta.minute}:${eta.second} ${eta.day}.${eta.month}.${eta.year}")
}

@Composable
fun RouteState(routeState: String?) {
    routeState?.let {
        Text(it)
    }
}

@Composable
fun RouteInfoRow(label: String, value: String?) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        Text(label)
        Text(value ?: "")
    }
}

@Composable
fun EmptyRoute() {
    Text(
        "No route selected",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.fillMaxSize(),
        textAlign = TextAlign.Center,
    )
}
