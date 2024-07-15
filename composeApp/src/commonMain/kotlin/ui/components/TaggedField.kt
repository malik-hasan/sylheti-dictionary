package ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import ui.theme.bodyFontFamily

@Composable
fun taggedField(tag: String, body: CharSequence): AnnotatedString {
    val bodySmall = MaterialTheme.typography.bodySmall.fontSize
    val bodyFontFamily = bodyFontFamily
    return buildAnnotatedString {
        withStyle(ParagraphStyle(textIndent = TextIndent(restLine = 30.sp))) {
            withStyle(
                SpanStyle(
                    fontStyle = FontStyle.Italic,
                    color = Color.Gray,
                    fontFamily = bodyFontFamily,
                    fontSize = bodySmall
                )
            ) { append(tag) }
            append(' ')
            append(body)
        }
    }
}
