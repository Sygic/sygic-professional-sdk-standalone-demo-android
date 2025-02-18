package com.sygic.example.ipcdemo3d.ui.poisupdate

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PoisUpdateScreen(viewModel: PoisUpdateViewModel = viewModel()) {
    Column(Modifier.fillMaxSize()) {
        val commandFieldState = rememberTextFieldState()
        val context = LocalContext.current

        viewModel.uiState.value.updateResult?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        TextField(commandFieldState, label = { Text("Command") }, modifier = Modifier.fillMaxSize().weight(1f))
        Button({viewModel.updatePois(commandFieldState.text.toString())}) {
            Text("Update POIs")
        }
    }
}
