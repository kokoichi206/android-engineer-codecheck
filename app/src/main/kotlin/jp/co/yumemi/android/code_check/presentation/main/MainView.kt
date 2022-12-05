package jp.co.yumemi.android.code_check.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.presentation.MainActivity.Companion.updateLastSearchDate
import jp.co.yumemi.android.code_check.presentation.main.component.OneRepository
import jp.co.yumemi.android.code_check.presentation.main.component.RecentSearched
import jp.co.yumemi.android.code_check.presentation.main.component.SearchBar
import jp.co.yumemi.android.code_check.presentation.util.TestTags

@Composable
fun MainView(
    viewModel: MainViewModel = hiltViewModel(),
    onRepositoryClick: (Repository) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchSearchRecent()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchBar(
                text = uiState.searchInput,
                onValueChange = {
                    viewModel.setSearchInput(it)
                    viewModel.setShowRecent(true)
                },
                onSearch = {
                    viewModel.searchResults(it)
                    updateLastSearchDate()
                    viewModel.setShowRecent(true)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                // 最近の検索結果を表示する条件
                if (uiState.repositories.isEmpty() && uiState.showRecent) {
                    RecentSearched(
                        searched = uiState.recent,
                        onCloseClick = {
                            viewModel.setShowRecent(false)
                        },
                        onItemClick = {
                            viewModel.setSearchInput(
                                TextFieldValue(
                                    text = it,
                                    selection = TextRange(it.length),
                                )
                            )
                        },
                    )
                }

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
}

@Preview
@Composable
fun MainViewPreview() {
    MainView()
}
