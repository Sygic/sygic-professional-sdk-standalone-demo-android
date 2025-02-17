package com.sygic.example.ipcdemo3d.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
@Preview
fun HomeScreen(viewModel: HomeScreenViewModel = viewModel()) {
    val state = viewModel.ui.value
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row {
            Text(text = "Status: ")
            Spacer(Modifier.width(8.dp))
            state.status?.let { Text(text = it) }
        }
        Spacer(Modifier.height(8.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { if (state.isConnected) viewModel.disconnect() else viewModel.connect() }) {
            Text(text = if (state.isConnected) "Disconnect from Sygic Service" else "Connect to Sygic Service")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            enabled = state.isConnected,
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.startNaviForeg() }) {
            Text(text = if (!state.isAppRunning && state.isConnected) "Start navi in foreg" else "Bring navi to foreg")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            enabled = state.isConnected && state.isAppRunning,
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.bringForeg5s(context) }) {
            Text(text = "Bring to Foreground for 5s")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            enabled = state.isConnected && state.isAppRunning,
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.endNavigation() }) {
            Text(text = "End Navigation")
        }

        Spacer(Modifier.height(8.dp))

        Row {
            Button(
                enabled = state.isConnected && state.isAppRunning,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = { viewModel.sdkVersion() }) {
                Text(text = "SDK Version")
            }

            Spacer(Modifier.width(8.dp))

            Button(
                enabled = state.isConnected && state.isAppRunning,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = { viewModel.appVersion() }) {
                Text(text = "App Version")
            }
        }

        Spacer(Modifier.height(8.dp))

        Row {
            Button(
                enabled = state.isConnected && state.isAppRunning,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = { viewModel.deviceId() }) {
                Text(text = "Device ID")
            }

            Spacer(Modifier.width(8.dp))

            Button(
                enabled = state.isConnected && state.isAppRunning,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = { viewModel.mapVersion() }) {
                Text(text = "Map Version")
            }
        }

        Spacer(Modifier.height(8.dp))
        Row {
            Button(
                enabled = state.isConnected && state.isAppRunning,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = { viewModel.flashMessage() }) {
                Text(text = "Flash Message")
            }

            Spacer(Modifier.width(8.dp))

            Button(
                enabled = state.isConnected && state.isAppRunning,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                onClick = { viewModel.showMessage() }) {
                Text(text = "Show Message")
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(text = state.message ?: "")
    }

}
