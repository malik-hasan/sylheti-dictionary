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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.plus
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.retain.RetainedEffect
import androidx.compose.runtime.retain.retain
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
import oats.mobile.sylhetidictionary.ui.components.ConsonantButton
import oats.mobile.sylhetidictionary.ui.components.ConsonantButtonTableCell
import oats.mobile.sylhetidictionary.ui.components.LeftHeaderTableCell
import oats.mobile.sylhetidictionary.ui.components.NavigationRailIconButton
import oats.mobile.sylhetidictionary.ui.components.SDScreen
import oats.mobile.sylhetidictionary.ui.components.SDTopAppBar
import oats.mobile.sylhetidictionary.ui.components.TableCell
import oats.mobile.sylhetidictionary.ui.components.TableColumn
import oats.mobile.sylhetidictionary.ui.components.TopHeaderTableCell
import oats.mobile.sylhetidictionary.ui.components.VoicedCellBlocker
import oats.mobile.sylhetidictionary.ui.components.VowelChartHeader
import oats.mobile.sylhetidictionary.ui.theme.latinBodyFontFamily
import oats.mobile.sylhetidictionary.ui.theme.textLinkStyle
import oats.mobile.sylhetidictionary.ui.utils.AudioPlayer
import oats.mobile.sylhetidictionary.ui.utils.drawVowelButton
import oats.mobile.sylhetidictionary.ui.utils.drawVowelChartDot
import oats.mobile.sylhetidictionary.ui.utils.drawVowelChartLine
import oats.mobile.sylhetidictionary.ui.utils.playPhone
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
            val audioPlayer = retain { AudioPlayer() }
            RetainedEffect(Unit) {
                onRetire { audioPlayer.release() }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
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
                    Text("Vowels", style = MaterialTheme.typography.titleMedium)
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
                            VowelChartHeader("Front")
                            VowelChartHeader("Back")
                        }

                        Column(
                            modifier = Modifier.constrainAs(heightLabels) {
                                top.linkTo(chart.top)
                                bottom.linkTo(chart.bottom)
                                height = Dimension.fillToConstraints
                            },
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            VowelChartHeader("Close")
                            VowelChartHeader("Open")
                        }

                        val textMeasurer = TextMeasurer(
                            LocalFontFamilyResolver.current,
                            LocalDensity.current,
                            LocalLayoutDirection.current
                        )
                        val textStyle = MaterialTheme.typography.bodyLarge.copy(color = ButtonDefaults.buttonColors().contentColor)
                        val chartColor = MaterialTheme.colorScheme.onBackground
                        val buttonColor = ButtonDefaults.buttonColors().containerColor
                        val tappedButtonColor = MaterialTheme.colorScheme.secondary

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
                                    }?.let { vowelButton ->
                                        tappedVowel = vowelButton
                                        vowelButtons[vowelButton]?.let { path ->
                                            logger.d("IPA_HELP: playing audio for $path")
                                            audioPlayer.playPhone(path)
                                        }
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
                            vowelButtons[i] = "91/Close_front_unrounded_vowel.ogg"
                            val vowelDotSpacer = Offset(8.sp.toPx(), 0f)
                            val iDot = drawVowelChartDot(i.centerRight + vowelDotSpacer, chartColor)

                            val u = drawVowelButton(
                                vowel = textMeasurer.measure("u", textStyle),
                                center = Offset(chartEnd, chartStart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[u] = "5d/Close_back_rounded_vowel.ogg"
                            val uDot = drawVowelChartDot(u.centerLeft - vowelDotSpacer, chartColor)

                            val laxIDot = drawVowelChartDot(Offset(oneThirdChart, oneSixthChart), chartColor)
                            val laxI = drawVowelButton(
                                vowel = textMeasurer.measure("ɪ", textStyle),
                                center = laxIDot.center - Offset(8.sp.toPx() + chartStart, 0f),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[laxI] = "4c/Near-close_near-front_unrounded_vowel.ogg"

                            val laxU = drawVowelButton(
                                vowel = textMeasurer.measure("ʊ", textStyle),
                                center = Offset(chartDimension * 5 / 6 + chartStart, oneSixthChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[laxU] = "d5/Near-close_near-back_rounded_vowel.ogg"
                            drawVowelChartDot(laxU.centerLeft - vowelDotSpacer, chartColor)

                            val e = drawVowelButton(
                                vowel = textMeasurer.measure("e", textStyle),
                                center = Offset(oneSixthChart, oneThirdChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[e] = "6c/Close-mid_front_unrounded_vowel.ogg"
                            val eDot = drawVowelChartDot(e.centerRight + vowelDotSpacer, chartColor)

                            val o = drawVowelButton(
                                vowel = textMeasurer.measure("o", textStyle),
                                center = Offset(chartEnd, oneThirdChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[o] = "84/Close-mid_back_rounded_vowel.ogg"
                            val oDot = drawVowelChartDot(o.centerLeft - vowelDotSpacer, chartColor)

                            val lowE = drawVowelButton(
                                vowel = textMeasurer.measure("ɛ", textStyle),
                                center = Offset(oneThirdChart, twoThirdsChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[lowE] = "71/Open-mid_front_unrounded_vowel.ogg"
                            val lowEDot = drawVowelChartDot(lowE.centerRight + vowelDotSpacer, chartColor)

                            val lowO = drawVowelButton(
                                vowel = textMeasurer.measure("ɔ", textStyle),
                                center = Offset(chartEnd, twoThirdsChart),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[lowO] = "d0/PR-open-mid_back_rounded_vowel.ogg"
                            val lowODot = drawVowelChartDot(lowO.centerLeft - vowelDotSpacer, chartColor)

                            val a = drawVowelButton(
                                vowel = textMeasurer.measure("a", textStyle),
                                center = Offset(halfChart, chartEnd),
                                buttonColor = buttonColor,
                                tappedColor = tappedButtonColor,
                                tappedVowel = tappedVowel
                            )
                            vowelButtons[a] = "65/Open_front_unrounded_vowel.ogg"
                            val aDot = drawVowelChartDot(a.centerRight + vowelDotSpacer, chartColor)

                            drawVowelChartLine(iDot.centerRight, uDot.centerLeft, chartColor)
                            drawVowelChartLine(eDot.centerRight, oDot.centerLeft, chartColor)
                            drawVowelChartLine(lowEDot.centerRight, lowODot.centerLeft, chartColor)
                            val lowBackVertex = Offset(lowODot.center.x, aDot.center.y)
                            drawVowelChartLine(aDot.centerRight, lowBackVertex, chartColor)

                            val angle = -30f
                            drawVowelChartLine(
                                start = iDot.bottomCenter.rotate(angle, iDot.center),
                                end = eDot.topCenter.rotate(angle, eDot.center),
                                color = chartColor
                            )
                            drawVowelChartLine(
                                start = eDot.bottomCenter.rotate(angle, eDot.center),
                                end = lowEDot.topCenter.rotate(angle, lowEDot.center),
                                color = chartColor
                            )
                            drawVowelChartLine(
                                start = lowEDot.bottomCenter.rotate(angle, lowEDot.center),
                                end = aDot.topCenter.rotate(angle, aDot.center),
                                color = chartColor
                            )

                            drawVowelChartLine(
                                start = Offset(halfChart, iDot.center.y),
                                end = Offset(chartEnd * 3 / 4, aDot.center.y),
                                color = chartColor
                            )

                            drawVowelChartLine(uDot.bottomCenter, oDot.topCenter, chartColor)
                            drawVowelChartLine(oDot.bottomCenter, lowODot.topCenter, chartColor)
                            drawVowelChartLine(lowODot.bottomCenter, lowBackVertex, chartColor)
                        }
                    }
                }

                item {
                    Text("Consonants", style = MaterialTheme.typography.titleMedium)
                }

                item {
                    LazyRow {
                        item {
                            TableColumn {
                                TableCell(
                                    topBorderThickness = null,
                                    leftBorderThickness = null,
                                    bottomBorderThickness = 1.dp,
                                    rightBorderThickness = 1.dp
                                )

                                LeftHeaderTableCell("Plosive")
                                LeftHeaderTableCell("Affricate")
                                LeftHeaderTableCell("Nasal")
                                LeftHeaderTableCell("Trill")
                                LeftHeaderTableCell("Tap/Flap")
                                LeftHeaderTableCell("Fricative")
                                LeftHeaderTableCell(
                                    label = "Lateral Approximant",
                                    bottomBorderThickness = 2.dp
                                )
                            }
                        }

                        item {
                            TableColumn {
                                TopHeaderTableCell("Bilabial")

                                // Plosive
                                ConsonantButtonTableCell {
                                    ConsonantButton("p", "51/Voiceless_bilabial_plosive.ogg", audioPlayer)
                                    ConsonantButton("b", "2c/Voiced_bilabial_plosive.ogg", audioPlayer)
                                }

                                // Affricate
                                TableCell()

                                // Nasal
                                ConsonantButtonTableCell(true) {
                                    ConsonantButton("m", "a9/Bilabial_nasal.ogg", audioPlayer)
                                }

                                // Trill
                                TableCell()
                                // Tap/Flap
                                TableCell()
                                // Fricative
                                TableCell()
                                // Lateral Approximant
                                TableCell(
                                    backgroundColor = MaterialTheme.colorScheme.onBackground,
                                    bottomBorderThickness = 2.dp
                                )
                            }
                        }

                        item {
                            TableColumn {
                                TopHeaderTableCell("Labiodental")

                                // Plosive
                                TableCell()
                                // Affricate
                                TableCell()

                                // Nasal
                                ConsonantButtonTableCell(true) {
                                    ConsonantButton("ɱ", "18/Labiodental_nasal.ogg", audioPlayer)
                                }

                                // Trill
                                TableCell()
                                // Tap/Flap
                                TableCell()

                                // Fricative
                                ConsonantButtonTableCell(voicelessOnly = true) {
                                    ConsonantButton("f", "c7/Voiceless_labio-dental_fricative.ogg", audioPlayer)
                                }

                                // Lateral Approximant
                                TableCell(
                                    backgroundColor = MaterialTheme.colorScheme.onBackground,
                                    bottomBorderThickness = 2.dp
                                )
                            }
                        }

                        item {
                            TableColumn {
                                TopHeaderTableCell("Dental")

                                // Plosive
                                ConsonantButtonTableCell {
                                    ConsonantButton("t", "dc/Voiceless_dental_stop.ogg", audioPlayer)
                                    ConsonantButton("d", "1c/Voiced_dental_stop.ogg", audioPlayer)
                                }

                                // Affricate
                                TableCell()
                                // Nasal
                                TableCell()
                                // Trill
                                TableCell()
                                // Tap/Flap
                                TableCell()
                                // Fricative
                                TableCell()
                                // Lateral Approximant
                                TableCell(bottomBorderThickness = 2.dp)
                            }
                        }

                        item {
                            TableColumn {
                                TopHeaderTableCell("Alveolar")

                                // Plosive
                                TableCell()
                                // Affricate
                                TableCell()

                                // Nasal
                                ConsonantButtonTableCell(
                                    voicedOnly = true,
                                    leftBorderThickness = null
                                ) {
                                    ConsonantButton("n", "29/Alveolar_nasal.ogg", audioPlayer)
                                }

                                // Trill
                                ConsonantButtonTableCell(
                                    voicedOnly = true,
                                    leftBorderThickness = null
                                ) {
                                    ConsonantButton("r", "ce/Alveolar_trill.ogg", audioPlayer)
                                }

                                // Tap/Flap
                                ConsonantButtonTableCell(
                                    voicedOnly = true,
                                    leftBorderThickness = null
                                ) {
                                    ConsonantButton("ɾ", "a0/Alveolar_tap.ogg", audioPlayer)
                                }

                                // Fricative
                                ConsonantButtonTableCell {
                                    ConsonantButton("s", "ac/Voiceless_alveolar_sibilant.ogg", audioPlayer)
                                    ConsonantButton("z", "c0/Voiced_alveolar_sibilant.ogg", audioPlayer)
                                }

                                // Lateral Approximant
                                ConsonantButtonTableCell(
                                    voicedOnly = true,
                                    leftBorderThickness = null,
                                    bottomBorderThickness = 2.dp
                                ) {
                                    ConsonantButton("l", "bc/Alveolar_lateral_approximant.ogg", audioPlayer)
                                }
                            }
                        }

                        item {
                            TableColumn {
                                TopHeaderTableCell("Postalveolar")

                                // Plosive
                                TableCell()

                                // Affricate
                                ConsonantButtonTableCell {
                                    ConsonantButton("ʧ", "97/Voiceless_palato-alveolar_affricate.ogg", audioPlayer)
                                    ConsonantButton("ʤ", "e6/Voiced_palato-alveolar_affricate.ogg", audioPlayer)
                                }

                                // Nasal
                                TableCell(leftBorderThickness = null)
                                // Trill
                                TableCell(leftBorderThickness = null)
                                // Tap/Flap
                                TableCell(leftBorderThickness = null)

                                // Fricative
                                ConsonantButtonTableCell {
                                    ConsonantButton("ʃ", "cc/Voiceless_palato-alveolar_sibilant.ogg", audioPlayer)
                                    ConsonantButton("ʒ", "30/Voiced_palato-alveolar_sibilant.ogg", audioPlayer)
                                }

                                // Lateral Approximant
                                TableCell(
                                    leftBorderThickness = null,
                                    bottomBorderThickness = 2.dp
                                )
                            }
                        }

                        item {
                            TableColumn {
                                TopHeaderTableCell("Retroflex")

                                // Plosive
                                ConsonantButtonTableCell {
                                    ConsonantButton("ʈ", "b0/Voiceless_retroflex_stop.oga", audioPlayer)
                                    ConsonantButton("ɖ", "27/Voiced_retroflex_stop.oga", audioPlayer)
                                }

                                // Affricate
                                TableCell()

                                // Nasal
                                ConsonantButtonTableCell(true) {
                                    ConsonantButton("ɳ", "af/Retroflex_nasal.ogg", audioPlayer)
                                }

                                // Trill
                                TableCell()

                                // Tap/Flap
                                ConsonantButtonTableCell(true) {
                                    ConsonantButton("ɽ", "87/Retroflex_flap.ogg", audioPlayer)
                                }

                                // Fricative
                                ConsonantButtonTableCell(voicelessOnly = true) {
                                    ConsonantButton("ʂ", "b1/Voiceless_retroflex_sibilant.ogg", audioPlayer)
                                }

                                // Lateral Approximant
                                TableCell(bottomBorderThickness = 2.dp)
                            }
                        }

                        item {
                            TableColumn {
                                TopHeaderTableCell("Velar")

                                // Plosive
                                ConsonantButtonTableCell {
                                    ConsonantButton("k", "e3/Voiceless_velar_plosive.ogg", audioPlayer)
                                    ConsonantButton("g", "12/Voiced_velar_plosive_02.ogg", audioPlayer)
                                }

                                // Affricate
                                TableCell()

                                // Nasal
                                ConsonantButtonTableCell(true) {
                                    ConsonantButton("ŋ", "39/Velar_nasal.ogg", audioPlayer)
                                }

                                // Trill
                                TableCell(backgroundColor = MaterialTheme.colorScheme.onBackground)
                                // Tap/Flap
                                TableCell(backgroundColor = MaterialTheme.colorScheme.onBackground)

                                // Fricative
                                ConsonantButtonTableCell(voicelessOnly = true) {
                                    ConsonantButton("x", "0f/Voiceless_velar_fricative.ogg", audioPlayer)
                                }

                                // Lateral Approximant
                                TableCell(bottomBorderThickness = 2.dp)
                            }
                        }

                        item {
                            TableColumn {
                                TopHeaderTableCell(
                                    label = "Glottal",
                                    rightBorderThickness = 2.dp
                                )

                                // Plosive
                                TableCell { VoicedCellBlocker() }
                                // Affricate
                                TableCell { VoicedCellBlocker() }
                                // Nasal
                                TableCell(backgroundColor = MaterialTheme.colorScheme.onBackground)
                                // Trill
                                TableCell(backgroundColor = MaterialTheme.colorScheme.onBackground)
                                // Tap/Flap
                                TableCell(backgroundColor = MaterialTheme.colorScheme.onBackground)

                                // Fricative
                                ConsonantButtonTableCell(
                                    voicelessOnly = true,
                                    rightBorderThickness = 2.dp
                                ) {
                                    ConsonantButton("h", "da/Voiceless_glottal_fricative.ogg", audioPlayer)
                                }

                                // Lateral Approximant
                                TableCell(backgroundColor = MaterialTheme.colorScheme.onBackground)
                            }
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
