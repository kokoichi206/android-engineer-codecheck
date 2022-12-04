package jp.co.yumemi.android.code_check.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun CustomTheme(
    darkMode: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = if (darkMode) {
            myDarkColorScheme
        } else {
            myLightColorScheme
        }
    ) {
        content()
    }
}
