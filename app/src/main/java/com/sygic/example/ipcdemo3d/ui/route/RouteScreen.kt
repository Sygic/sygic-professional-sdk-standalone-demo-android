package com.sygic.example.ipcdemo3d.ui.route

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sygic.example.ipcdemo3d.ui.common.PointInfoDialog
import com.sygic.sdk.remoteapi.model.StopOffPoint

@Composable
fun RouteScreen(viewModel: RouteScreenViewModel = viewModel()) {
    val items = viewModel.routes.value
    val pointInfoState = remember { mutableStateOf<StopOffPoint?>(null) }
    pointInfoState.value?.let {
        PointInfoDialog(it) { pointInfoState.value = null }
    }

    Column(Modifier.padding(vertical = 16.dp)) {
        Text(
            text = if (items.isEmpty()) "No Route" else "Current route",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.height(16.dp))
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = rememberLazyListState(),
            contentPadding = PaddingValues(8.dp)
        ) {
            itemsIndexed(items = items) { indx, item ->
                Column {
                    RoutePointItem(item) { pointInfoState.value = it }
                    if (indx < items.lastIndex) {
                        Spacer(Modifier.height(8.dp))
                        Icon(
                            Icons.Default.ArrowDropDown, contentDescription = "next", modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .fillMaxWidth()
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun RoutePointItem(stopOffPoint: StopOffPoint, onClick: (StopOffPoint) -> Unit) {
    Text(
        "${if (stopOffPoint.isVisited) "✓" else "-"} ${getRouteCaption(stopOffPoint)}",
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke(stopOffPoint) }
    )
}

private fun getRouteCaption(stopOffPoint: StopOffPoint) =
    when {
        !stopOffPoint.caption.isNullOrEmpty() -> stopOffPoint.caption
        !stopOffPoint.address.isNullOrEmpty() -> stopOffPoint.address
        else -> stopOffPoint.location.toString()
    }
