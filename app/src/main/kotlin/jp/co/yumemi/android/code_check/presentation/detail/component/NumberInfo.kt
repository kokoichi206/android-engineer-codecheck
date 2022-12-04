package jp.co.yumemi.android.code_check.presentation.detail.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.presentation.theme.Colors

@Composable
fun NumberInfo(
    modifier: Modifier = Modifier,
    key: String,
    value: Long,
    iconId: Int,
    tag: String = "",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = "Icon of $key",
            tint = Colors.Icon,
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .weight(2f)
                .fillMaxWidth()
                .testTag(tag),
            text = key,
            style = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.secondary,
            )
        )

        Text(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            text = "$value",
            style = TextStyle(
                textAlign = TextAlign.Start,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun RightNumberInfoPreview() {
    NumberInfo(
        key = "Stars",
        value = 3,
        iconId = R.drawable.star,
    )
}
