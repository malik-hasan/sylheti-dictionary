package oats.mobile.sylhetidictionary.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import oats.mobile.sylhetidictionary.ui.components.NavigationRailIconButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBar
import oats.mobile.sylhetidictionary.ui.theme.latinBodyFontFamily
import oats.mobile.sylhetidictionary.ui.theme.textLinkStyle
import oats.mobile.sylhetidictionary.ui.utils.AudioPlayer
import oats.mobile.sylhetidictionary.ui.utils.drawVoweChartDot
import oats.mobile.sylhetidictionary.ui.utils.drawVowelButton
import oats.mobile.sylhetidictionary.ui.utils.drawVowelChartLine
import oats.mobile.sylhetidictionary.ui.utils.rotate
import oats.mobile.sylhetidictionary.ui.utils.space
import oats.mobile.sylhetidictionary.utility.UnicodeUtility
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf
import sylhetidictionary.composeapp.generated.resources.Res
import sylhetidictionary.composeapp.generated.resources.how_does_ipa_rep_sylheti
import sylhetidictionary.composeapp.generated.resources.how_to_search_ipa
import sylhetidictionary.composeapp.generated.resources.ipa_equivalences
import sylhetidictionary.composeapp.generated.resources.ipa_help
import sylhetidictionary.composeapp.generated.resources.ipa_is_an_alphabet
import sylhetidictionary.composeapp.generated.resources.ipa_url
import sylhetidictionary.composeapp.generated.resources.learn_more
import sylhetidictionary.composeapp.generated.resources.sylheti_ipa_chart_with_audio
import sylhetidictionary.composeapp.generated.resources.sylheti_ipa_url
import sylhetidictionary.composeapp.generated.resources.use_qwerty
import sylhetidictionary.composeapp.generated.resources.what_is_ipa

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun IpaHelpScreen(
    logger: Logger = koinInject { parametersOf("IpaHelp") }
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    SDScreen(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            SDTopAppBar(
                scrollBehavior = scrollBehavior,
                navigationIcon = { NavigationRailIconButton() },
                title = { Text(stringResource(Res.string.ipa_help)) }
            )
        }
    ) { scaffoldPadding ->
        SelectionContainer {
            val audioPlayer = remember { AudioPlayer() }
            DisposableEffect(Unit) {
                onDispose { audioPlayer.release() }
            }

            LazyColumn(
                contentPadding = scaffoldPadding + PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text(
                        text = stringResource(Res.string.what_is_ipa),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                item {
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(Res.string.ipa_is_an_alphabet))
                            space()
                            withLink(
                                LinkAnnotation.Url(
                                    url = stringResource(Res.string.ipa_url),
                                    styles = textLinkStyle
                                )
                            ) { append(stringResource(Res.string.learn_more)) }
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item {
                    Spacer(Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = stringResource(Res.string.how_does_ipa_rep_sylheti),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                item {
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(Res.string.sylheti_ipa_chart_with_audio))
                            space()
                            withLink(LinkAnnotation.Url(
                                url = stringResource(Res.string.sylheti_ipa_url),
                                styles = textLinkStyle
                            )) { append(stringResource(Res.string.learn_more)) }
                        },
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                item { 
                    Text("Vowel Chart", style = MaterialTheme.typography.titleMedium)
                }

                item {
                    ConstraintLayout(Modifier.horizontalScroll(rememberScrollState())) {
                        val (backnessLabels, heightLabels, chart) = createRefs()

                        Row(
                            modifier = Modifier.constrainAs(backnessLabels) {
                                absoluteLeft.linkTo(chart.absoluteLeft)
                                absoluteRight.linkTo(chart.absoluteRight)
                                width = Dimension.fillToConstraints
                            }.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Front", style = MaterialTheme.typography.labelLarge)
                            Text("Back", style = MaterialTheme.typography.labelLarge)
                        }

                        Column(
                            modifier = Modifier.constrainAs(heightLabels) {
                                top.linkTo(chart.top)
                                bottom.linkTo(chart.bottom)
                                height = Dimension.fillToConstraints
                            },
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("High", style = MaterialTheme.typography.labelLarge)
                            Text("Low", style = MaterialTheme.typography.labelLarge)
                        }

                        val textMeasurer = TextMeasurer(
                            LocalFontFamilyResolver.current,
                            LocalDensity.current,
                            LocalLayoutDirection.current
                        )
                        val textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSecondary)
                        val chartColor = MaterialTheme.colorScheme.onBackground
                        val buttonColor = MaterialTheme.colorScheme.secondary
                        val tappedButtonColor = MaterialTheme.colorScheme.secondaryContainer

                        val vowelButtons = remember { mutableMapOf<Rect, String>() }
                        var tappedVowel by remember { mutableStateOf<Rect?>(null) }

                        LaunchedEffect(tappedVowel) {
                            tappedVowel?.let {
                                delay(500)
                                tappedVowel = null
                            }
                        }

                        Canvas(Modifier
                            .constrainAs(chart) {
                                top.linkTo(backnessLabels.bottom, 4.dp)
                                absoluteLeft.linkTo(heightLabels.absoluteRight, 4.dp)
                            }.size(with(LocalDensity.current) { 300.sp.toDp() })
                            .pointerInput(Unit) {
                                detectTapGestures { offset ->
                                    vowelButtons.keys.firstOrNull { buttonRect ->
                                        buttonRect.contains(offset)
                                    }?.let {
                                        tappedVowel = it
                                        val path = vowelButtons[it]
                                        logger.d("IPA_HELP: playing audio for $path")
                                        audioPlayer.play("https://upload.wikimedia.org/wikipedia/commons/$path.ogg")
                                    }
                                }
                            }
                        ) {
                            val iText = textMeasurer.measure("i", textStyle)
                            val canvasDimension = size.width
                            val chartStart = (iText.size.height) / 2f
                            val chartEnd = canvasDimension - chartStart
                            val halfChart = canvasDimension / 2
                            val chartDimension = chartEnd - chartStart
                            val oneSixthChart = chartDimension / 6 + chartStart
                            val oneThirdChart = chartDimension / 3 + chartStart
                            val twoThirdsChart = chartDimension * 2 / 3 + chartStart
                            val i = drawVowelButton(
                                vowel = iText,
                                center = Offset(chartStart, chartStart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[i] = "9/91/Close_front_unrounded_vowel"

                            val u = drawVowelButton(
                                vowel = textMeasurer.measure("u", textStyle),
                                center = Offset(chartEnd, chartStart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[u] = "5/5d/Close_back_rounded_vowel"

                            val laxI = drawVowelButton(
                                vowel = textMeasurer.measure("ɪ", textStyle),
                                center = Offset(oneThirdChart, oneSixthChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[laxI] = "4/4c/Near-close_near-front_unrounded_vowel"

                            val laxU = drawVowelButton(
                                vowel = textMeasurer.measure("ʊ", textStyle),
                                center = Offset(chartDimension * 5 / 6 + chartStart, oneSixthChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[laxU] = "d/d5/Near-close_near-back_rounded_vowel"

                            val e = drawVowelButton(
                                vowel = textMeasurer.measure("e", textStyle),
                                center = Offset(oneSixthChart, oneThirdChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[e] = "6/6c/Close-mid_front_unrounded_vowel"

                            val o = drawVowelButton(
                                vowel = textMeasurer.measure("o", textStyle),
                                center = Offset(chartEnd, oneThirdChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[o] = "8/84/Close-mid_back_rounded_vowel"

                            val lowE = drawVowelButton(
                                vowel = textMeasurer.measure("ɛ", textStyle),
                                center = Offset(oneThirdChart, twoThirdsChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[lowE] = "7/71/Open-mid_front_unrounded_vowel"

                            val lowO = drawVowelButton(
                                vowel = textMeasurer.measure("ɔ", textStyle),
                                center = Offset(chartEnd, twoThirdsChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[lowO] = "d/d0/PR-open-mid_back_rounded_vowel"

                            val a = drawVowelButton(
                                vowel = textMeasurer.measure("a", textStyle),
                                center = Offset(halfChart, chartEnd),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[a] = "6/65/Open_front_unrounded_vowel"

                            drawVowelChartLine(i.centerRight, u.centerLeft, chartColor)
                            drawVowelChartLine(e.centerRight, o.centerLeft, chartColor)
                            drawVowelChartLine(lowE.centerRight, lowO.centerLeft, chartColor)
                            val lowBackVertex = Offset(lowO.center.x, a.center.y, chartColor)
                            drawVowelChartLine(a.centerRight, lowBackVertex, chartColor)

                            val angle = -30f
                            drawVowelChartLine(
                                start = i.bottomCenter.rotate(angle, i.center),
                                end = e.topCenter.rotate(angle, e.center),
                                color = chartColor
                            )
                            drawVowelChartLine(
                                start = e.bottomCenter.rotate(angle, e.center),
                                end = lowE.topCenter.rotate(angle, lowE.center),
                                color = chartColor
                            )
                            drawVowelChartLine(
                                start = lowE.bottomCenter.rotate(angle, lowE.center),
                                end = a.topCenter.rotate(angle, a.center),
                                color = chartColor
                            )

                            drawVowelChartLine(
                                start = Offset(halfChart, i.center.y),
                                end = Offset(chartEnd * 3 / 4, a.center.y),
                                color = chartColor
                            )

                            drawVowelChartLine(u.bottomCenter, o.topCenter, chartColor)
                            drawVowelChartLine(o.bottomCenter, lowO.topCenter, chartColor)
                            drawVowelChartLine(lowO.bottomCenter, lowBackVertex, chartColor)
                        }
                    }
                }

                item {
                    Spacer(Modifier.height(8.dp))
                }

                item {
                    Text(
                        text = stringResource(Res.string.how_to_search_ipa),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                item {
                    Text(
                        text = stringResource(Res.string.use_qwerty),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                item {
                    Image(
                        painter = painterResource(Res.drawable.ipa_equivalences),
                        contentDescription = "searching 'suron' matches 'sʊɾɔn' as well as 'ʃʊɾɔŋ'",
                    )
                }

                item {
                    Text(
                        text = stringResource(Res.string.ipa_equivalences),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                items(UnicodeUtility.LATIN_IPA_CHAR_MAP.toList()) { (char, charSet) ->
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Black)) {
                                append("$char — ")
                            }
                            append(charSet.joinToString(", "))
                        },
                        fontFamily = latinBodyFontFamily
                    )
                }
            }
        }
    }
}
