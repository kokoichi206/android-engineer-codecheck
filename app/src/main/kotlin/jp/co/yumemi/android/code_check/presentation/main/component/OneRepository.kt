package jp.co.yumemi.android.code_check.presentation.main.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.co.yumemi.android.code_check.models.Repository

@Composable
fun OneRepository(
    repository: Repository,
    onRepositoryClick: (Repository) -> Unit = {},
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onRepositoryClick(repository)
            }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        text = repository.name,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.onBackground,
    )
    androidx.compose.material.Divider(
        modifier = Modifier
            .padding(0.dp),
        color = MaterialTheme.colorScheme.onBackground,
    )
}

@Preview
@Composable
fun OneRepositoryPreview() {
    val repository = Repository(
        name = "kokoichi206/kokoichi206",
        ownerIconUrl = "",
        language = "",
        stargazersCount = 0,
        watchersCount = 1,
        forksCount = 2,
        openIssuesCount = 3,
    )
    OneRepository(repository)
}
