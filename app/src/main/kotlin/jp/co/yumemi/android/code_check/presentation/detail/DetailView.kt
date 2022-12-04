package jp.co.yumemi.android.code_check.presentation.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.presentation.detail.component.RightNumberInfo

@Composable
fun DetailView(repository: Repository) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(repository.ownerIconUrl)
                .crossfade(true)
                .build(),
            placeholder = painterResource(id = R.drawable.jetbrains),
            contentDescription = "image of ${repository.name}",
            contentScale = ContentScale.Crop,
            alignment = Alignment.TopStart,
            modifier = Modifier
                .padding(12.dp),
        )

        Text(
            modifier = Modifier,
            text = repository.name,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
        )

        Row(
            modifier = Modifier
                .padding(4.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f),
                text = stringResource(id = R.string.written_language, repository.language),
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
            )

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = stringResource(id = R.string.detail_stars, repository.stargazersCount),
                style = TextStyle(
                    textAlign = TextAlign.End,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                ),
            )
        }

        RightNumberInfo(
            text = stringResource(id = R.string.detail_watchers, repository.watchersCount)
        )

        RightNumberInfo(
            text = stringResource(id = R.string.detail_forks, repository.forksCount)
        )

        RightNumberInfo(
            text = stringResource(id = R.string.detail_open_issues, repository.openIssuesCount)
        )
    }
}

@Preview
@Composable
fun DetailViewPreview() {
    val repository = Repository(
        name = "kokoichi206/kokoichi206",
        ownerIconUrl = "https://avatars.githubusercontent.com/u/52474650?v=4",
        language = "Go",
        stargazersCount = 0,
        watchersCount = 1,
        forksCount = 2,
        openIssuesCount = 3,
    )
    DetailView(repository = repository)
}
