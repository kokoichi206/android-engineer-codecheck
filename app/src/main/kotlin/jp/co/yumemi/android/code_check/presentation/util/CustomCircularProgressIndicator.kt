package jp.co.yumemi.android.code_check.presentation.util

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jp.co.yumemi.android.code_check.presentation.theme.CustomTheme


@Composable
fun CustomCircularProgressIndicator(
    size: Dp = 24.dp,
    visible: Boolean = true,
    durationMillis: Int = 200,
) {
    AnimatedVisibility(
        modifier = Modifier
            .fillMaxSize(),
        visible = visible,
        enter = fadeIn(tween(durationMillis)),
        exit = fadeOut(tween(durationMillis)),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {

            CircularProgressIndicator(
                modifier = Modifier
                    .shadow(
                        elevation = size / 3,
                        shape = CircleShape,
                        ambientColor = MaterialTheme.colorScheme.outline,
                    )
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.secondary)
                    .padding(size / 2)
                    .size(size),
                color = MaterialTheme.colorScheme.onSecondary,
                strokeWidth = 4.dp,
            )
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun CustomCircularProgressIndicatorPreview() {
    CustomTheme {
        CustomCircularProgressIndicator()
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
fun CustomCircularProgressIndicatorWithNightModePreview() {
    CustomTheme {
        CustomCircularProgressIndicator()
    }
}
