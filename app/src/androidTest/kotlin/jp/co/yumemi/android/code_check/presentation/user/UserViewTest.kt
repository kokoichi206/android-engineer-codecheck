package jp.co.yumemi.android.code_check.presentation.user

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.yumemi.android.code_check.data.MockGitHubRepositoryImpl
import jp.co.yumemi.android.code_check.di.AppModule
import jp.co.yumemi.android.code_check.presentation.MainActivity
import jp.co.yumemi.android.code_check.presentation.util.TestTags
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class UserViewTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        hiltRule.inject()
        MockGitHubRepositoryImpl.initMock()

        composeRule.activity.setContent {
            UserView()
        }
    }

    @After
    fun tearDown() {
        MockGitHubRepositoryImpl.initMock()
    }

    @Test
    fun `UserView_show_correctly`() {
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
    fun `search_users_work_correctly`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()

        // Act
        searchBar.performTextInput("kokoichi206")
        // 対象とする IME Action は勝手に選ばれる
        searchBar.performImeAction()

        // Assert
        searchBar.assertTextEquals("kokoichi206")

        assertEquals(1, MockGitHubRepositoryImpl.counter)
        assertEquals("kokoichi206", MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `search_users_with_empty_text_field_should_not_call_api`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()

        // Act
        searchBar.performImeAction()

        // Assert
        // hint が表示されていること
        searchBar.assertTextContains("GitHub のユーザーを検索できるよー")
        searchResult.onChildren().assertCountEquals(0)

        // API が呼び出されてないこと
        assertEquals(0, MockGitHubRepositoryImpl.counter)
        assertNull(MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `search_users_when_exception_occurs_should_not_crash`() {
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
        // キャンセルボタンが表示されていること
        cancelButton.assertExists()

        // Act
        cancelButton.performClick()

        // Assert
        // 検索文字列・結果が消えてること
        searchBar.assertTextContains("GitHub のユーザーを検索できるよー")
        searchResult.onChildren().assertCountEquals(0)
        // キャンセルボタンが表示されてないこと
        cancelButton.assertDoesNotExist()
    }
}