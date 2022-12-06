package jp.co.yumemi.android.code_check.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.presentation.detail.navigation.detailView
import jp.co.yumemi.android.code_check.presentation.detail.navigation.navigateToDetailView
import jp.co.yumemi.android.code_check.presentation.main.navigation.mainRoute
import jp.co.yumemi.android.code_check.presentation.main.navigation.mainView
import jp.co.yumemi.android.code_check.presentation.user.navigation.userView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation() {
    SetupUIBar()

    val navController = rememberNavController()

    /**
     * BottomBar を表示するかのフラグ。
     * メイン画面のスクロール状態からのコールバックで変更される。
     */
    var showBottomBar by remember { mutableStateOf(true) }

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
        bottomBar = {
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = EaseIn,
                    ),
                    initialOffsetY = { it },
                ),
                exit = slideOutVertically(
                    animationSpec = tween(
                        durationMillis = 500,
                        easing = EaseOut,
                    ),
                    targetOffsetY = { it },
                ),
            ) {
                BottomBarView(navController = navController)
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
    ) {
        BottomNavigation(
            navController = navController,
            paddingValues = it,
            onScroll = { scrolled ->
                showBottomBar = (scrolled > 0)
            },
        )
    }
}

@Composable
fun BottomNavigation(
    navController: NavHostController,
    paddingValues: PaddingValues,
    onScroll: (Int) -> Unit = {},
) {
    NavHost(
        modifier = Modifier
            // Bottom に関しては BottomBar をスクロールで表示を切り替える関係で padding させない
            .padding(top = paddingValues.calculateTopPadding()),
        navController = navController,
        startDestination = mainRoute,
    ) {

        mainView(
            onRepositoryClick = {
                navController.navigateToDetailView(it)
            },
            onScroll = {
                onScroll(it)
            },
        )

        detailView()

        userView(
            onScroll = {
                onScroll(it)
            },
        )
    }
}

@Composable
fun SetupUIBar() {
    val systemUiController = rememberSystemUiController()

    systemUiController.setStatusBarColor(
        color = MaterialTheme.colorScheme.background,
        darkIcons = !isSystemInDarkTheme(),
    )
    systemUiController.setNavigationBarColor(
        color = Color.Gray.copy(alpha = 0.1f),
        darkIcons = !isSystemInDarkTheme(),
    )
}
