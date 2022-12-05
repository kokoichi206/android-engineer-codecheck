package jp.co.yumemi.android.code_check.presentation.user.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.User
import jp.co.yumemi.android.code_check.presentation.theme.Colors

@Composable
fun OneUser(
    user: User,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(user.avatarUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.file_not_found),
            contentDescription = "image of ${user.name}",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart,
            modifier = Modifier
                .size(40.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = user.name,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onBackground,
        )
    }
    Divider(color = Colors.Divider)
}

@Preview(
    showBackground = true,
)
@Composable
fun OneUser() {
    val user = User(
        name = "kokoichi206",
        avatarUrl = "",
        htmlUrl = "",
        type = "User",
    )
    OneUser(user)
}
