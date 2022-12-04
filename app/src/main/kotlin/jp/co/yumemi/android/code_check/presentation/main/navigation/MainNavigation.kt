package jp.co.yumemi.android.code_check.presentation.main.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.presentation.main.MainView

const val mainRoute = "main_route"

fun NavGraphBuilder.mainView(
    onRepositoryClick: (Repository) -> Unit,
) {
    composable(route = mainRoute) {
        MainView(onRepositoryClick = onRepositoryClick)
    }
}
