package oats.mobile.sylhetidictionary.ui.utils

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBarState
import androidx.compose.material3.SearchBarValue

@OptIn(ExperimentalMaterial3Api::class)
val SearchBarState.isExpanded
    get() = currentValue == SearchBarValue.Expanded
