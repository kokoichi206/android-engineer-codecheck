package jp.co.yumemi.android.code_check.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.presentation.MainActivity.Companion.updateLastSearchDate
import jp.co.yumemi.android.code_check.presentation.main.component.OneRepository
import jp.co.yumemi.android.code_check.presentation.main.component.RecentSearched
import jp.co.yumemi.android.code_check.presentation.util.CustomCircularProgressIndicator
import jp.co.yumemi.android.code_check.presentation.util.SearchBar
import jp.co.yumemi.android.code_check.presentation.util.TestTags

@Composable
fun MainView(
    viewModel: MainViewModel = hiltViewModel(),
    onRepositoryClick: (Repository) -> Unit = {},
    onScroll: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(true) {
        viewModel.fetchSearchRecent()
    }

    val scrollState = rememberLazyListState()
    var lastIndex by remember { mutableStateOf(0) }
    LaunchedEffect(scrollState) {

        snapshotFlow {
            scrollState.layoutInfo
        }.collect {
            if (it.visibleItemsInfo.isNotEmpty()) {
                val info = it.visibleItemsInfo[0]
                if (lastIndex != info.index) {
                    // Scroll された Index 分、呼び出し元に返してあげる
                    val diff = lastIndex - info.index
                    onScroll(diff)
                    lastIndex = info.index
                }
            }
        }
    }

    val focusManager = LocalFocusManager.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.MAIN_VIEW)
    ) {
        CustomCircularProgressIndicator(visible = uiState.isLoading)

        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            SearchBar(
                text = uiState.searchInput,
                hint = stringResource(R.string.searchInputText_hint),
                onValueChange = {
                    viewModel.setSearchInput(it)
                    viewModel.setShowRecent(true)
                },
                onSearch = {
                    viewModel.searchResults(it)
                    updateLastSearchDate()
                    viewModel.setShowRecent(true)
                    // 検索実行時にキーボードを閉じる
                    focusManager.clearFocus()
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
                            // 検索バーに表示
                            viewModel.setSearchInput(
                                TextFieldValue(
                                    text = it,
                                    selection = TextRange(it.length),
                                )
                            )
                            // 検索まで行う
                            viewModel.searchResults(it)
                            // 検索実行時にキーボードを閉じる
                            focusManager.clearFocus()
                        },
                        onItemReflectClick = {
                            // 検索バーに表示するだけ
                            viewModel.setSearchInput(
                                TextFieldValue(
                                    text = it,
                                    selection = TextRange(it.length),
                                )
                            )
                        }
                    )
                }

                LazyColumn(
                    modifier = Modifier
                        .testTag(TestTags.SEARCH_RESULT),
                    state = scrollState,
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
