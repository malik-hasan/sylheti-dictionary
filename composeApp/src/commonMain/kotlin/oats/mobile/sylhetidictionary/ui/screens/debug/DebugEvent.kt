package oats.mobile.sylhetidictionary.ui.screens.debug

sealed class DebugEvent {
    data class EnableFeatureBengaliAppLocale(val enable: Boolean) : DebugEvent()
    data class EnableFeatureBengaliDefinitions(val enable: Boolean) : DebugEvent()
    data class EnableFeatureBengaliExamples(val enable: Boolean) : DebugEvent()
}
