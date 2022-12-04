package jp.co.yumemi.android.code_check.presentation.detail.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RightNumberInfo(
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Box(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            text = text,
            style = TextStyle(
                textAlign = TextAlign.End,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
    }
}

@Preview
@Composable
fun RightNumberInfoPreview() {
    RightNumberInfo(text = "Right")
}
