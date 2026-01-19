package oats.mobile.sylhetidictionary.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
fun LoadingScreen(appDataLoaded: Boolean, clearLoadingScreen: () -> Unit) {
    LaunchedEffect(appDataLoaded) {
        if (appDataLoaded) clearLoadingScreen()
    }

    Box(Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.primaryFixed)
    )
}
