package jp.co.yumemi.android.code_check.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.code_check.presentation.theme.Colors
import jp.co.yumemi.android.code_check.presentation.util.TestTags

@Composable
fun RecentSearched(
    searched: List<String>,
    onCloseClick: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onItemReflectClick: (String) -> Unit = {},
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        item {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .testTag(TestTags.RECENT_SEARCHED),
                    text = "Recent",
                    fontSize = 16.sp,
                    style = TextStyle(
                        color = Colors.TextGray,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Box(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Close,
                    modifier = Modifier
                        .size(16.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.4f))
                        .clickable {
                            onCloseClick()
                        }
                        .testTag(TestTags.RECENT_SEARCHED_CLOSE),
                    contentDescription = "close recent",
                    tint = Color.White,
                )
            }
        }

        items(searched) { str ->
            Row(
                modifier = Modifier
                    .clickable {
                        onItemClick(str)
                    }
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .testTag("${TestTags.RECENT_SEARCH_STR_ROW_PREFIX}_$str"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .testTag("${TestTags.SEARCH_RECENT_RESULT_PREFIX}_$str"),
                    text = str,
                    fontSize = 16.sp,
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Box(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    modifier = Modifier
                        .size(20.dp)
                        .rotate(45f)
                        .clickable(
                            onClick = {
                                onItemReflectClick(str)
                            },
                            indication = rememberRipple(bounded = false),
                            interactionSource = remember { MutableInteractionSource() }
                        )
                        .testTag("${TestTags.REFLECT_SEARCH_BAR_PREFIX}_$str"),
                    contentDescription = "close recent",
                    tint = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun RecentSearchedPreview() {
    val recent = listOf(
        "????????????",
        "??????46",
        "Twitter",
        "Android",
        "Flutter",
        "???????????????",
        "GitHub API",
    )
    RecentSearched(recent)
}
