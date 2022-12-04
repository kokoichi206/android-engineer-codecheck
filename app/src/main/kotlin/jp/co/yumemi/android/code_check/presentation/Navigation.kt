package jp.co.yumemi.android.code_check.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import jp.co.yumemi.android.code_check.presentation.detail.navigation.detailView
import jp.co.yumemi.android.code_check.presentation.detail.navigation.navigateToDetailView
import jp.co.yumemi.android.code_check.presentation.main.navigation.mainRoute
import jp.co.yumemi.android.code_check.presentation.main.navigation.mainView

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = mainRoute,
    ) {
        mainView {
            navController.navigateToDetailView(it)
        }

        detailView()
    }
}
