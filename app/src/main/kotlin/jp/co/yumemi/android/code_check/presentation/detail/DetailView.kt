package jp.co.yumemi.android.code_check.presentation.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.testTag
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
import jp.co.yumemi.android.code_check.presentation.detail.component.NumberInfo
import jp.co.yumemi.android.code_check.presentation.theme.Colors
import jp.co.yumemi.android.code_check.presentation.util.TestTags

@Composable
fun DetailView(repository: Repository) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.DETAIL_VIEW),
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
            modifier = Modifier
                .testTag(TestTags.DETAIL_NAME),
            text = repository.name,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 24.sp,
        )

        Column(
            modifier = Modifier
                .padding(horizontal = 32.dp)
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.DETAIL_LANGUAGE),
                text = stringResource(id = R.string.written_language, repository.language),
                color = Colors.TextGray,
                fontSize = 16.sp,
                textAlign = TextAlign.End,
            )

            NumberInfo(
                key = stringResource(id = R.string.detail_stars),
                value = repository.watchersCount,
                tag = TestTags.DETAIL_STARS,
                iconId = R.drawable.star,
            )
            Divider(color = Colors.Divider)

            NumberInfo(
                key = stringResource(id = R.string.detail_watchers),
                value = repository.watchersCount,
                tag = TestTags.DETAIL_WATCHERS,
                iconId = R.drawable.watch,
            )
            Divider(color = Colors.Divider)

            NumberInfo(
                key = stringResource(id = R.string.detail_forks),
                value = repository.forksCount,
                tag = TestTags.DETAIL_FORKS,
                iconId = R.drawable.fork,
            )
            Divider(color = Colors.Divider)

            NumberInfo(
                key = stringResource(id = R.string.detail_open_issues),
                value = repository.openIssuesCount,
                tag = TestTags.DETAIL_ISSUES,
                iconId = R.drawable.issue,
            )
            Divider(color = Colors.Divider)

            val uriHandler = LocalUriHandler.current

            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .clickable {
                            uriHandler.openUri(repository.repoUrl)
                        },
                    painter = painterResource(id = R.drawable.github),
                    contentDescription = "github icon",
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun DetailViewPreview() {
    val repository = Repository(
        name = "kokoichi206/kokoichi206",
        repoUrl = "",
        ownerIconUrl = "https://avatars.githubusercontent.com/u/52474650?v=4",
        language = "Go",
        stargazersCount = 0,
        watchersCount = 1,
        forksCount = 2,
        openIssuesCount = 3,
    )
    DetailView(repository = repository)
}
