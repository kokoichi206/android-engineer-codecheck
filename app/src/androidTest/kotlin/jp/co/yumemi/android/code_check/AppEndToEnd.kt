package jp.co.yumemi.android.code_check

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.yumemi.android.code_check.data.MockGitHubRepositoryImpl
import jp.co.yumemi.android.code_check.di.AppModule
import jp.co.yumemi.android.code_check.presentation.MainActivity
import jp.co.yumemi.android.code_check.presentation.Navigation
import jp.co.yumemi.android.code_check.presentation.util.TestTags
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AppEndToEnd {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        MockGitHubRepositoryImpl.initMock()

        composeRule.activity.setContent {
            Navigation()
        }
    }

    @After
    fun tearDown() {
        MockGitHubRepositoryImpl.initMock()
    }

    @Test
    fun `tap_a_repository_navigate_to_detail_screen`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        // 検索結果一覧を用意
        searchBar.performTextInput("test2")
        searchBar.performImeAction()

        val item = searchResult.onChildren()[0]

        val viewInDetail = composeRule.onNodeWithTag(TestTags.DETAIL_VIEW)
        viewInDetail.assertDoesNotExist()

        // Act
        item.performClick()

        // Assert
        // fragment_detail の一要素から判断する
        viewInDetail.assertExists()
    }

    @Test
    fun `back_key_in_navigated_detail_screen_should_return_main_screen`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        // 検索結果一覧を用意
        searchBar.performTextInput("test3")
        searchBar.performImeAction()
        // 一覧から1つ目の要素をクリックする
        val item = searchResult.onChildren()[0]
        item.performClick()
        // 詳細画面にいることを確認する
        val viewInDetail = composeRule.onNodeWithTag(TestTags.DETAIL_VIEW)
        viewInDetail.assertExists()
        searchBar.assertDoesNotExist()

        // Act
        Espresso.pressBack()

        // Assert
        // 詳細画面が表示されていないこと
        viewInDetail.assertDoesNotExist()
        // メイン画面が表示されていること
        searchBar.assertExists()
        searchResult.assertExists()
        // 検索文字列が消えていないこと
        searchBar.assertTextContains("test3")
        // 検索結果が消えていないこと
        searchResult.onChildren().assertCountEquals(2)
    }
}