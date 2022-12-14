package jp.co.yumemi.android.code_check.presentation.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.presentation.main.component.OneRepository
import jp.co.yumemi.android.code_check.presentation.main.component.RecentSearched
import jp.co.yumemi.android.code_check.presentation.util.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
fun MainView(
    viewModel: MainViewModel = hiltViewModel(),
    onRepositoryClick: (Repository) -> Unit = {},
    onScroll: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
                    // Scroll ????????? Index ??????????????????????????????????????????
                    val diff = lastIndex - info.index
                    onScroll(diff)
                    lastIndex = info.index
                }
            }
            // ????????? ?????? ????????????????????????
            if (scrollState.isScrolledToEnd() && lastIndex != 0) {
                viewModel.onScrollEnd()
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
                    viewModel.setShowRecent(true)
                    // ?????????????????????????????????????????????
                    focusManager.clearFocus()
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                // ??????????????????????????????????????????
                if (uiState.repositories.isEmpty() && uiState.showRecent) {
                    RecentSearched(
                        searched = uiState.recent,
                        onCloseClick = {
                            viewModel.setShowRecent(false)
                        },
                        onItemClick = {
                            // ?????????????????????
                            viewModel.setSearchInput(
                                TextFieldValue(
                                    text = it,
                                    selection = TextRange(it.length),
                                )
                            )
                            // ??????????????????
                            viewModel.searchResults(it)
                            // ?????????????????????????????????????????????
                            focusManager.clearFocus()
                        },
                        onItemReflectClick = {
                            // ?????????????????????????????????
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

        // snackBar ??????????????????
        val snackBarCoroutineScope = rememberCoroutineScope()
        val snackBarHostState = remember { SnackbarHostState() }
        var snackBarJob: Job? by remember { mutableStateOf(null) }

        LaunchedEffect(uiState.error) {
            if (uiState.error.isNotEmpty()) {
                snackBarJob?.cancel()
                snackBarJob = snackBarCoroutineScope.launch {
                    // uiState ???????????????????????????????????????
                    snackBarHostState.showSnackbar(uiState.error)
                }
                viewModel.resetError()
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            SnackbarSetting(
                snackbarHostState = snackBarHostState,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
fun MainViewPreview() {
    MainView()
}
