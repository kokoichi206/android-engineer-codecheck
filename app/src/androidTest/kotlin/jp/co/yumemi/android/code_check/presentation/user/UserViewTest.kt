package jp.co.yumemi.android.code_check.presentation.user

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.yumemi.android.code_check.data.MockGitHubRepositoryImpl
import jp.co.yumemi.android.code_check.di.AppModule
import jp.co.yumemi.android.code_check.models.User
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
        assertEquals(0, MockGitHubRepositoryImpl.searchUsersCounter)

        // Act
        searchBar.performTextInput("kokoichi206")
        // ??????????????? IME Action ????????????????????????
        searchBar.performImeAction()

        // Assert
        searchBar.assertTextEquals("kokoichi206")

        assertEquals(0, MockGitHubRepositoryImpl.searchRepositoriesCounter)
        assertEquals(1, MockGitHubRepositoryImpl.searchUsersCounter)
        assertEquals("kokoichi206", MockGitHubRepositoryImpl.passedQuery)
        assertEquals(1, MockGitHubRepositoryImpl.passedPage)
    }

    @Test
    fun `scroll_to_end_should_call_search_users`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)

        val users = (0..30).map { User("${it}_repository", "", "", "") }
        MockGitHubRepositoryImpl.users = users

        searchBar.performTextInput("test")
        searchBar.performImeAction()

        assertEquals(1, MockGitHubRepositoryImpl.searchUsersCounter)
        searchResult.performScrollToIndex(30)

        // Act
        searchResult.performTouchInput {
            swipeUp(startY = centerY)
        }

        // Assert
        // 2??? API ???????????????????????????
        assertEquals(2, MockGitHubRepositoryImpl.searchUsersCounter)
        assertEquals("test", MockGitHubRepositoryImpl.passedQuery)
        assertEquals(2, MockGitHubRepositoryImpl.passedPage)
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
        // hint ??????????????????????????????
        searchBar.assertTextContains("GitHub ???????????????????????????????????????")
        searchResult.onChildren().assertCountEquals(0)

        // API ?????????????????????????????????
        assertEquals(0, MockGitHubRepositoryImpl.searchUsersCounter)
        assertNull(MockGitHubRepositoryImpl.passedQuery)
        assertEquals(0, MockGitHubRepositoryImpl.passedPage)
    }

    @Test
    fun `search_users_with_no_result_should_show_snackBar`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        val snackBar = composeRule.onNodeWithTag(TestTags.SNACK_BAR)
        snackBar.assertDoesNotExist()
        MockGitHubRepositoryImpl.users = emptyList()
        searchBar.performTextInput("test")

        // Act
        searchBar.performImeAction()

        // Assert
        searchResult.onChildren().assertCountEquals(0)
        // API ?????????????????????????????????
        assertEquals(1, MockGitHubRepositoryImpl.searchUsersCounter)
        assertEquals("test", MockGitHubRepositoryImpl.passedQuery)
        // Snack Bar ??????????????????????????????
        snackBar.assertExists().assertTextContains("?????????1???????????????????????????????????????\n?????????????????????????????????????????????????????????")
    }

    @Test
    fun `search_users_when_exception_occurs`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        // API (???????????? Repository) ???????????????????????????????????????
        MockGitHubRepositoryImpl.error = Exception("My Custom Exception")
        val snackBar = composeRule.onNodeWithTag(TestTags.SNACK_BAR)
        snackBar.assertDoesNotExist()

        // Act
        searchBar.performTextInput("test2")
        searchBar.performImeAction()

        // Assert
        searchBar.assertTextEquals("test2")
        // API ????????????????????????????????????????????????????????????????????????
        searchResult.onChildren().assertCountEquals(0)

        // API ?????????????????????????????????
        assertEquals(1, MockGitHubRepositoryImpl.searchUsersCounter)
        assertEquals("test2", MockGitHubRepositoryImpl.passedQuery)
        // ????????????????????????????????????????????????
        snackBar.assertExists()
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
        // ??????????????????????????????????????????????????????
        cancelButton.assertExists()

        // Act
        cancelButton.performClick()

        // Assert
        // ?????????????????????????????????????????????
        searchBar.assertTextContains("GitHub ???????????????????????????????????????")
        searchResult.onChildren().assertCountEquals(0)
        // ??????????????????????????????????????????????????????
        cancelButton.assertDoesNotExist()
    }
}
