package com.sygic.example.ipcdemo3d.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.runIO(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch(Dispatchers.IO, block= block)
