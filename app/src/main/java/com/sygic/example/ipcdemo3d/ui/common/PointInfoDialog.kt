package com.sygic.example.ipcdemo3d.ui.common

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.sygic.sdk.remoteapi.model.StopOffPoint

@Composable
fun PointInfoDialog(item: StopOffPoint, onDismissAction: () -> Unit) {
    Dialog(onDismissAction) {
        Surface(
            Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                PointInfoItem("Caption", item.caption ?: "No caption")
                PointInfoItem("Address", item.address ?: "No address")
                PointInfoItem("ISO", item.iso ?: "No iso")
                PointInfoItem("Location", item.location.toString())
                PointInfoItem("Offset", item.offset.toString())
                PointInfoItem("Point type", item.pointType.toString())
                Spacer(Modifier.height(16.dp))
                Button(onClick = onDismissAction, modifier = Modifier.fillMaxWidth()) {
                    Text("Dismiss")
                }
            }
        }
    }
}

@Composable
fun PointInfoItem(name: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary
        )
        Text(
            value,
            Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
        )
    }
}

@Preview(backgroundColor = 0xFFFFFFFF, showBackground = true)
@Composable
fun PointInfoItemPreview() {
    PointInfoItem("Name", "Value")
}
