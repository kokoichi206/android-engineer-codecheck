package jp.co.yumemi.android.code_check.presentation

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.NavHostController
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.yumemi.android.code_check.di.AppModule
import jp.co.yumemi.android.code_check.presentation.util.TestTags
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class BottomBarViewKtTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()


    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {

            BottomBarView(NavHostController(composeRule.activity))
        }
    }

    @Test
    fun `BottomBarView_show_correctly`() {
        // Arrange
        val repoTag = "${TestTags.BOTTOM_ITEM_PREFIX}_${BottomNavItem.Home.name}"
        val repo = composeRule.onNodeWithTag(repoTag)
        val userTag = "${TestTags.BOTTOM_ITEM_PREFIX}_${BottomNavItem.User.name}"
        val user = composeRule.onNodeWithTag(userTag)

        // Act

        // Assert
        repo.assertExists().assertTextContains("Repository")
        user.assertExists().assertTextContains("User")
    }
}
