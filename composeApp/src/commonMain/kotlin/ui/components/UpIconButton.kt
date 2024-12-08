package ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import ui.app.LocalNavController

@Composable
fun UpIconButton(navController: NavController = LocalNavController.current) {
    IconButton(
        onClick = { navController.navigateUp() }
    ) { Icon(Icons.AutoMirrored.Default.ArrowBack, "back") }
}
