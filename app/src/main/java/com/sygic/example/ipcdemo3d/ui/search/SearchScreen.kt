package com.sygic.example.ipcdemo3d.ui.search

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
@Preview
fun SearchScreen(viewModel: SearchScreenViewModel = viewModel()) {
    val searchTextState = rememberTextFieldState()
    Column(Modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            TextField(
                state = searchTextState,
                label = { Text("Enter text") },
                modifier = Modifier.fillMaxWidth().weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = { viewModel.search(searchTextState.text.toString()) }) {
                Text("Search")
            }
        }
        Spacer(Modifier.height(16.dp))
        viewModel.uiState.value.searchResult?.let {
            Text(text = it, modifier = Modifier.fillMaxSize())
        }
    }
}
