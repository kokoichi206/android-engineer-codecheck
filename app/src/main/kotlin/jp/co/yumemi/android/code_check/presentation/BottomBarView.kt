package jp.co.yumemi.android.code_check.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomBarView(
    navController: NavHostController,
) {
    val backStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry.value?.destination?.route
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .padding(horizontal = 16.dp)
            .shadow(16.dp, RoundedCornerShape(32.dp))
            .clip(RoundedCornerShape(32.dp))
            .background(Color.Transparent)
            .height(60.dp),
        backgroundColor = Color.White,
        elevation = 5.dp,
    ) {
        val selectedColor = MaterialTheme.colorScheme.primary
        val unSelectedColor = Color.Gray.copy(alpha = 0.6f)

        bottomNavItems.forEach { item ->
            val selected = item.route == currentRoute
            BottomNavigationItem(
                selected = selected,
                onClick = {
                    // stock には 1 route のみ
                    navController.popBackStack()
                    navController.navigate(item.route)
                },
                selectedContentColor = selectedColor,
                unselectedContentColor = unSelectedColor,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = "Icon of ${item.name}",
                        )
                        Text(
                            text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp,
                        )
                    }
                },
            )
        }
    }
}
