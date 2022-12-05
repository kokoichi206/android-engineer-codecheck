package jp.co.yumemi.android.code_check.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.Group
import androidx.compose.ui.graphics.vector.ImageVector
import jp.co.yumemi.android.code_check.presentation.main.navigation.mainRoute
import jp.co.yumemi.android.code_check.presentation.user.navigation.userRoute

val bottomNavItems = listOf(
    BottomNavItem.Home,
    BottomNavItem.User,
)

sealed class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
) {
    object Home : BottomNavItem(
        name = "Repository",
        route = mainRoute,
        icon = Icons.Default.AccountTree,
    )

    object User : BottomNavItem(
        name = "User",
        route = userRoute,
        icon = Icons.Default.Group,
    )
}
