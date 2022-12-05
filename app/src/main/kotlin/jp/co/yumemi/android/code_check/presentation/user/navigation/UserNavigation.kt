package jp.co.yumemi.android.code_check.presentation.user.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import jp.co.yumemi.android.code_check.presentation.user.UserView

const val userRoute = "user_route"

fun NavGraphBuilder.userView() {
    composable(route = userRoute) {
        UserView()
    }
}
