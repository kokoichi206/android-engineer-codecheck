package jp.co.yumemi.android.code_check.presentation.theme

import android.annotation.SuppressLint
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

private val PrimaryBlue = Color(0xFF1BA6E1)

object Colors {
    val TextGray = Color.Gray
    val Divider = Color.Gray
    val Icon = Color.Gray
}

@SuppressLint("ConflictingOnColor")
val myLightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
)

@SuppressLint("ConflictingOnColor")
val myDarkColorScheme = darkColorScheme(
    primary = Color(0xFF454845),
    onPrimary = PrimaryBlue,
    background = Color(0xFF1A1C19),
    onBackground = Color(0xFFDAE1FD),
)
