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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    fun `main_fragment_show_correctly`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()

        // Act
        searchBar.performTextInput("test")

        // Assert
        searchBar.assertTextEquals("test")
        searchResult.onChildren().assertCountEquals(0)
    }

    @Test
    fun `search_repositories_work_correctly`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()

        // Act
        searchBar.performTextInput("test")
        // 対象とする IME Action は勝手に選ばれる
        searchBar.performImeAction()

        // Assert
        searchBar.assertTextEquals("test")
        // Mock で用意した数表示されていること
        searchResult.onChildren().assertCountEquals(2)

        assertEquals(1, MockGitHubRepositoryImpl.counter)
        assertEquals("test", MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `search_repositories_with_empty_text_field_should_not_call_api`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()

        // Act
        searchBar.performImeAction()

        // Assert
        // hint が表示されていること
        searchBar.assertTextContains("GitHub のリポジトリを検索できるよー")
        searchResult.onChildren().assertCountEquals(0)

        // API が呼び出されてないこと
        assertEquals(0, MockGitHubRepositoryImpl.counter)
        assertNull(MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `search_repositories_when_exception_occurs_should_not_crash`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        // API (正確には Repository) で擬似的にエラーを吐かせる
        MockGitHubRepositoryImpl.error = Exception("My Custom Exception")

        // Act
        searchBar.performTextInput("test2")
        searchBar.performImeAction()

        // Assert
        searchBar.assertTextEquals("test2")
        // API のレスポンスがないので、リストは更新されていない
        searchResult.onChildren().assertCountEquals(0)

        // API が呼び出されていること
        assertEquals(1, MockGitHubRepositoryImpl.counter)
        assertEquals("test2", MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `cancel_button_work_correctly`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        val cancelButton = composeRule.onNodeWithTag(TestTags.CANCEL_BUTTON)
        cancelButton.assertDoesNotExist()

        searchBar.performTextInput("test2")
        searchBar.performImeAction()
        searchBar.assertTextEquals("test2")
        searchResult.onChildren().assertCountEquals(2)
        // キャンセルボタンが表示されていること
        cancelButton.assertExists()

        // Act
        cancelButton.performClick()

        // Assert
        // 検索文字列・結果が消えてること
        searchBar.assertTextContains("GitHub のリポジトリを検索できるよー")
        searchResult.onChildren().assertCountEquals(0)
        // キャンセルボタンが表示されてないこと
        cancelButton.assertDoesNotExist()
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
    fun `navigated_detail_screen_show_correctly`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        // 検索結果一覧を用意
        searchBar.performTextInput("test2")
        searchBar.performImeAction()
        val item = searchResult.onChildren()[0]

        // Act
        item.performClick()

        // Assert
        composeRule.onNodeWithTag(TestTags.DETAIL_NAME)
            .assertTextEquals("actions-marketplace-validations/kokoichi206_action-URL-watcher")
        composeRule.onNodeWithTag(TestTags.DETAIL_LANGUAGE)
            .assertTextEquals("Written in Kotlin")
        composeRule.onNodeWithTag(TestTags.DETAIL_STARS)
            .assertTextEquals("Stars")
        composeRule.onNodeWithTag(TestTags.DETAIL_WATCHERS)
            .assertTextEquals("Watchers")
        composeRule.onNodeWithTag(TestTags.DETAIL_FORKS)
            .assertTextEquals("Forks")
        composeRule.onNodeWithTag(TestTags.DETAIL_ISSUES)
            .assertTextEquals("Open issues")
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