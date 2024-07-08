package ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(scrollBehavior: TopAppBarScrollBehavior) {

    var searchState by remember { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = { Text("Sylheti Dictionary") }
//        scrollBehavior = scrollBehavior
    )
//    SearchBar(
//        query = "",
//        onQueryChange = {},
//        onSearch = { searchState = false },
//        active = searchState,
//        onActiveChange = { searchState = it  }
//    ) {
//        Button(onClick = { /*TODO*/ }) {Text("@3uzu")}
//    }
}
