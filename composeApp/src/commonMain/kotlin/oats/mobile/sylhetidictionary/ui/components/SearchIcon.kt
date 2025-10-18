package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.painterResource
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.search

@Composable
fun SearchIcon() = Icon(
    painter = painterResource(Res.drawable.search),
    contentDescription = "Search"
)
