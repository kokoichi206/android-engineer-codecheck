package jp.co.yumemi.android.code_check.presentation.main.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.code_check.presentation.theme.Colors

@Composable
fun RecentSearched(
    searched: List<String>,
    onCloseClick: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
) {
    val iconColor = Color.Gray.copy(alpha = 0.4f)

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
                        .background(iconColor)
                        .clickable {
                            onCloseClick()
                        },
                    contentDescription = "close recent",
                    tint = Color.White,
                )
            }
        }

        items(searched) { str ->
            Row(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = str,
                    fontSize = 16.sp,
                    style = TextStyle(
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Box(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    modifier = Modifier
                        .size(16.dp)
                        .rotate(45f)
                        .clickable {
                            onItemClick(str)
                        },
                    contentDescription = "close recent",
                    tint = iconColor,
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
        "サッカー",
        "櫻坂46",
        "Twitter",
        "Android",
        "Flutter",
        "弱虫ペダル",
        "GitHub API",
    )
    RecentSearched(recent)
}
