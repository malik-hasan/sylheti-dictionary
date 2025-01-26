package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import oats.mobile.sylhetidictionary.ui.app.LocalNavController

@Composable
fun UpIconButton(
    modifier: Modifier = Modifier,
    navController: NavController = LocalNavController.current
) {
    IconButton(
        modifier = modifier,
        onClick = { navController.navigateUp() }
    ) { Icon(Icons.AutoMirrored.Default.ArrowBack, "back") }
}
