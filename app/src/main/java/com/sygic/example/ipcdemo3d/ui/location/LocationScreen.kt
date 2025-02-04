package com.sygic.example.ipcdemo3d.ui.location

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun LocationScreen(navigation: NavController, viewModel: LocationScreenViewModel = viewModel()) {
    val longFieldState = rememberTextFieldState()
    val latFieldState = rememberTextFieldState()
    val addressFieldState = rememberTextFieldState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val state = viewModel.uiState.value

    Column(
        Modifier
            .fillMaxSize()
            .padding(9.dp)
    ) {
        Row {
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.reverseGeo(latFieldState.text.toString(), longFieldState.text.toString())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { Text("Reverse Geocoding") }
            Spacer(Modifier.width(8.dp))
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.roadInfo(latFieldState.text.toString(), longFieldState.text.toString())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { Text("Road Info") }
        }
        Spacer(Modifier.height(8.dp))
        Row {
            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.navigateToPoint(latFieldState.text.toString(), longFieldState.text.toString())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { Text("Navigate to Point") }
            Spacer(Modifier.width(8.dp))

            Button(
                onClick = {
                    keyboardController?.hide()
                    viewModel.navigateToAddress(addressFieldState.text.toString())
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { Text("Navigate to Address") }
        }
        Spacer(Modifier.height(16.dp))
        Row {
            TextField(
                latFieldState,
                label = { Text("Latitude") },
                placeholder = { Text("4815559") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                isError = state.latError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                longFieldState,
                label = { Text("Longitude") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                placeholder = { Text("1710568") },
                isError = state.lonError,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
        Spacer(Modifier.height(8.dp))
        TextField(
            addressFieldState,
            label = { Text("ISO, City, Street, number") },
            modifier = Modifier.fillMaxWidth()
        )
        state.result?.let {
            Spacer(Modifier.height(16.dp))
            Text(text = it)
        }
    }

}
