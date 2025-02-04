package com.sygic.example.ipcdemo3d.ui.pois

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sygic.sdk.remoteapi.model.Poi
import com.sygic.sdk.remoteapi.model.PoiCategory

@Composable
fun PoisScreen(navController: NavController, viewModel: PoisScreenViewModel = viewModel()) {

    val state = viewModel.uiState
    val poiEditState = rememberTextFieldState("Poi Example")
    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp, 16.dp)
    ) {
        val categories = state.value.categories
        if (categories.isNullOrEmpty()) {
            Text("No categories")
        } else {
            CategorySelector(categories) {
                viewModel.categorySelected(it)
            }
            PoiList(state.value.pois)
        }
    }

}

@Composable
fun PoiList(pois: List<Poi>?) {
    if (pois.isNullOrEmpty()) {
        Text("No POIs")
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(pois) { indx, poi ->
                Column {
                    Text(poi.name, style = MaterialTheme.typography.bodyLarge)
                    if (indx < pois.lastIndex) {
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(data: List<PoiCategory>, itemSelected: (PoiCategory) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(data[0].name)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field to handle
            // expanding/collapsing the menu on click. A read-only text field has
            // the anchor type `PrimaryNotEditable`.
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            state = textFieldState,
            readOnly = true,
            lineLimits = TextFieldLineLimits.SingleLine,
            label = { Text("Categories") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            data.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd(option.name)
                        expanded = false
                        itemSelected.invoke(option)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}
