package jp.co.yumemi.android.code_check.presentation.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.Repository

@Composable
fun OneRepository(
    repository: Repository,
    onRepositoryClick: (Repository) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRepositoryClick(repository)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(repository.ownerIconUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.file_not_found),
            contentDescription = "image of ${repository.name}",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart,
            modifier = Modifier
                .size(40.dp),
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = repository.name,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.written_language, repository.language),
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.End,
            )
        }
    }
    androidx.compose.material.Divider(
        modifier = Modifier
            .padding(0.dp),
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Preview(
    showBackground = true,
)
@Composable
fun OneRepositoryPreview() {
    val repository = Repository(
        name = "kokoichi206/kokoichi206",
        ownerIconUrl = "",
        language = "Kotlin",
        stargazersCount = 0,
        watchersCount = 1,
        forksCount = 2,
        openIssuesCount = 3,
    )
    OneRepository(repository)
}
