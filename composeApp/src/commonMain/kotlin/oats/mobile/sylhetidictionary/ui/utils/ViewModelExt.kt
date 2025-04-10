package oats.mobile.sylhetidictionary.ui.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.runBlocking

inline fun <reified T> ViewModel.stateFlowOf(initialValue: T, flow: Flow<T>) =
    flow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), initialValue)

inline fun <reified T> ViewModel.blockingStateFlowOf(flow: Flow<T>) =
    flow.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), runBlocking { flow.first() })
