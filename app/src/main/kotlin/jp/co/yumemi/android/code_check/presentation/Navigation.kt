package jp.co.yumemi.android.code_check.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.presentation.detail.navigation.detailView
import jp.co.yumemi.android.code_check.presentation.detail.navigation.navigateToDetailView
import jp.co.yumemi.android.code_check.presentation.main.navigation.mainRoute
import jp.co.yumemi.android.code_check.presentation.main.navigation.mainView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    val navController = rememberNavController()

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
        NavHost(
            modifier = Modifier
                .padding(paddingValues = it),
            navController = navController,
            startDestination = mainRoute,
        ) {
            mainView {
                navController.navigateToDetailView(it)
            }

            detailView()
        }
    }
}