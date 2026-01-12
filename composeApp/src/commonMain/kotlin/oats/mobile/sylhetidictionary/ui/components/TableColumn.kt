package oats.mobile.sylhetidictionary.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TableColumn(content: @Composable ColumnScope.() -> Unit) = Column(
    modifier = Modifier.width(IntrinsicSize.Max),
    content = content
)
