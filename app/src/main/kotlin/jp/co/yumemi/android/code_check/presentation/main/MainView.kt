package jp.co.yumemi.android.code_check.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.presentation.MainActivity.Companion.updateLastSearchDate
import jp.co.yumemi.android.code_check.presentation.main.component.OneRepository
import jp.co.yumemi.android.code_check.presentation.main.component.SearchBar
import jp.co.yumemi.android.code_check.presentation.util.TestTags

@Composable
fun MainView(
    viewModel: MainViewModel = hiltViewModel(),
    onRepositoryClick: (Repository) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchBar(
                onSearch = {
                    viewModel.searchResults(it)
                    updateLastSearchDate()
                }
            )

            LazyColumn(
                modifier = Modifier
                    .testTag(TestTags.SEARCH_RESULT)
            ) {
                items(uiState.repositories) { item ->
                    OneRepository(repository = item, onRepositoryClick = onRepositoryClick)
                }
            }
        }
    }
}

@Preview
@Composable
fun MainViewPreview() {
    MainView()
}
