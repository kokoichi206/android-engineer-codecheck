package jp.co.yumemi.android.code_check.presentation.util

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SnackbarSetting(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        SnackbarHost(
            modifier = Modifier.align(Alignment.BottomCenter),
            hostState = snackbarHostState,
            snackbar = { snackbarData: SnackbarData ->
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(4.dp)
                        .wrapContentSize()
                        .border(
                            width = 2.dp,
                            color = androidx.compose.material3.MaterialTheme.colorScheme.outline,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .align(Alignment.Center)
                ) {
                    Text(
                        modifier = Modifier
                            .background(androidx.compose.material3.MaterialTheme.colorScheme.secondary)
                            .padding(8.dp),
                        text = snackbarData.message,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSecondary,
                    )
                }
            }
        )
    }
}
