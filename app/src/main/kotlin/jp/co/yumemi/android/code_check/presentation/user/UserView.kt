package jp.co.yumemi.android.code_check.presentation.user

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.User
import jp.co.yumemi.android.code_check.presentation.MainActivity
import jp.co.yumemi.android.code_check.presentation.util.SearchBar
import jp.co.yumemi.android.code_check.presentation.user.component.OneUser
import jp.co.yumemi.android.code_check.presentation.util.TestTags

@Composable
fun UserView(
    viewModel: UserViewModel = hiltViewModel(),
    onScroll: (Int) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    UserViewMain(
        uiState = uiState,
        onScroll = onScroll,
        onValueChange = {
            viewModel.setSearchInput(it)
        },
        onSearch = {
            viewModel.searchResults(it)
            MainActivity.updateLastSearchDate()
        },
    )
}

@Composable
fun UserViewMain(
    uiState: UserUiState,
    onScroll: (Int) -> Unit = {},
    onValueChange: (TextFieldValue) -> Unit = {},
    onSearch: (String) -> Unit = {},
) {

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
                hint = stringResource(id = R.string.searchInputText_hint_users),
                onValueChange = {
                    onValueChange(it)
                },
                onSearch = {
                    onSearch(it)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                LazyColumn(
                    modifier = Modifier
                        .testTag(TestTags.SEARCH_RESULT),
                    state = scrollState,
                ) {
                    items(uiState.users) { item ->
                        OneUser(user = item)
                    }
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
)
@Composable
fun MainViewPreview() {
    val uiState = UserUiState()
    UserViewMain(uiState)
}

@Preview(
    showBackground = true,
)
@Composable
fun MainViewWithUsersPreview() {
    val uiState = UserUiState(
        users = listOf(
            User(name = "kokoichi206", "", "", ""),
            User(name = "kokoichi2", "", "", ""),
            User(name = "my_name", "", "", ""),
            User(name = "github", "", "", ""),
        ),
    )
    UserViewMain(uiState)
}
