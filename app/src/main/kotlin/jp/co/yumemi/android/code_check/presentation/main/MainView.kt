package jp.co.yumemi.android.code_check.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.presentation.MainActivity.Companion.updateLastSearchDate
import jp.co.yumemi.android.code_check.presentation.main.component.OneRepository
import jp.co.yumemi.android.code_check.presentation.main.component.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView(
    onRepositoryClick: (Repository) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        SearchView(paddingValues = it, onRepositoryClick = onRepositoryClick)
    }
}

@Composable
fun SearchView(
    viewModel: MainViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(0.dp),
    onRepositoryClick: (Repository) -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
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

            LazyColumn {
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
