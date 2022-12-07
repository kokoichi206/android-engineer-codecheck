package jp.co.yumemi.android.code_check.presentation.main

import androidx.activity.compose.setContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.yumemi.android.code_check.data.MockGitHubRepositoryImpl
import jp.co.yumemi.android.code_check.di.AppModule
import jp.co.yumemi.android.code_check.models.Repository
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
class MainViewTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    companion object {
        private var repositoryClickedCounter = 0
        private var passedRepository: Repository? = null
    }

    @Before
    fun setUp() {
        hiltRule.inject()
        MockGitHubRepositoryImpl.initMock()

        composeRule.activity.setContent {
            MainView(
                onRepositoryClick = {
                    repositoryClickedCounter++
                    passedRepository = it
                },
            )
        }

        // Initialize mock
        repositoryClickedCounter = 0
        passedRepository = null
    }

    @After
    fun tearDown() {
        MockGitHubRepositoryImpl.initMock()
    }

    @Test
    fun `MainView_show_correctly`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        val recent = composeRule.onNodeWithTag(TestTags.RECENT_SEARCHED)
        recent.assertExists()

        // Act
        searchBar.performTextInput("test")

        // Assert
        searchBar.assertTextEquals("test")
        searchResult.onChildren().assertCountEquals(0)
        recent.assertExists()
    }

    @Test
    fun `search_repositories_work_correctly`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        val recent = composeRule.onNodeWithTag(TestTags.RECENT_SEARCHED)
        recent.assertExists()

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
        // 直近の検索結果が消えていること
        recent.assertDoesNotExist()
    }

    @Test
    fun `search_repositories_with_empty_text_field_should_not_call_api`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        val recent = composeRule.onNodeWithTag(TestTags.RECENT_SEARCHED)
        recent.assertExists()

        // Act
        searchBar.performImeAction()

        // Assert
        // hint が表示されていること
        searchBar.assertTextContains("GitHub のリポジトリを検索できるよー")
        searchResult.onChildren().assertCountEquals(0)

        // API が呼び出されてないこと
        assertEquals(0, MockGitHubRepositoryImpl.counter)
        assertNull(MockGitHubRepositoryImpl.passedQuery)
        // 直近の検索結果が消えていないこと
        recent.assertExists()
    }

    @Test
    fun `search_repositories_with_no_result_should_show_snackBar`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        val snackBar = composeRule.onNodeWithTag(TestTags.SNACK_BAR)
        snackBar.assertDoesNotExist()
        MockGitHubRepositoryImpl.repositories = emptyList()
        searchBar.performTextInput("test")

        // Act
        searchBar.performImeAction()

        // Assert
        searchResult.onChildren().assertCountEquals(0)
        // API が呼び出されていること
        assertEquals(1, MockGitHubRepositoryImpl.counter)
        assertEquals("test", MockGitHubRepositoryImpl.passedQuery)
        // Snack Bar が表示されていること
        snackBar.assertExists().assertTextContains("結果が1件も見つかりませんでした。\n検索ワードを変えて再度お試しください。")
    }

    @Test
    fun `search_repositories_when_exception`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        // API (正確には Repository) で擬似的にエラーを吐かせる
        MockGitHubRepositoryImpl.error = Exception("My Custom Exception")
        val recent = composeRule.onNodeWithTag(TestTags.RECENT_SEARCHED)
        recent.assertExists()
        val snackBar = composeRule.onNodeWithTag(TestTags.SNACK_BAR)
        snackBar.assertDoesNotExist()

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
        // 直近の検索結果が消えていないこと
        recent.assertExists()
        // スナックバーが表示されていること
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
    fun `show_recent_work_correctly`() {
        // Arrange
        val query = "test_show_recent"
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        val recent = composeRule.onNodeWithTag(TestTags.RECENT_SEARCHED)
        recent.assertExists()
        val searchedReflect = composeRule.onNodeWithTag("${TestTags.REFLECT_SEARCH_BAR_PREFIX}_$query")
        searchedReflect.assertDoesNotExist()

        // Act
        // 検索を行う
        searchBar.performTextInput(query)
        searchBar.performImeAction()
        // 検索文字列削除
        composeRule.onNodeWithTag(TestTags.CANCEL_BUTTON).performClick()

        // Assert
        recent.assertExists()
        // 直近の検索結果に、履歴が表示されていること
        searchedReflect.assertExists()
        composeRule.onNodeWithTag(
            testTag = "${TestTags.SEARCH_RECENT_RESULT_PREFIX}_$query",
            useUnmergedTree = true,
        ).assertTextEquals(query)
    }

    @Test
    fun `close_show_recent_work_correctly`() {
        // Arrange
        val recent = composeRule.onNodeWithTag(TestTags.RECENT_SEARCHED)
        val closeRecent = composeRule.onNodeWithTag(TestTags.RECENT_SEARCHED_CLOSE)
        recent.assertExists()

        // Act
        // Recent の close ボタンを押す
        closeRecent.performClick()

        // Assert
        // Recent が表示されていないこと
        recent.assertDoesNotExist()
    }

    @Test
    fun `show_recent_reflect_correctly`() {
        // Arrange
        val query = "show_recent_reflect_correctly_query"
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        val searchedReflect = composeRule.onNodeWithTag("${TestTags.REFLECT_SEARCH_BAR_PREFIX}_$query")
        searchBar.performTextInput(query)
        searchBar.performImeAction()
        // 検索文字列削除
        composeRule.onNodeWithTag(TestTags.CANCEL_BUTTON).performClick()
        searchBar.assertTextContains("GitHub のリポジトリを検索できるよー")

        // Act
        // 反映ボタン (↖︎) を押す
        searchedReflect.performClick()

        // Assert
        // 検索バーに、期待値通り文言が表示されていること
        searchBar.assertTextEquals(query)
    }

    @Test
    fun `show_recent_searched_name_work_correctly`() {
        // Arrange
        val query = "show_recent_reflect_correctly_query"
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        val searchedRow = composeRule.onNodeWithTag("${TestTags.RECENT_SEARCH_STR_ROW_PREFIX}_$query")
        // 検索履歴用意
        searchBar.performTextInput(query)
        searchBar.performImeAction()
        MockGitHubRepositoryImpl.initMock()
        // 検索文字列削除
        composeRule.onNodeWithTag(TestTags.CANCEL_BUTTON).performClick()

        // Act
        // 対象の行を押す
        searchedRow.performClick()

        // Assert
        // 検索バーに、期待値通り文言が表示されていること
        searchBar.assertTextEquals(query)
        // API のレスポンスにより、リストが更新されていること
        searchResult.onChildren().assertCountEquals(2)

        // API が呼び出されていること
        assertEquals(1, MockGitHubRepositoryImpl.counter)
        assertEquals(query, MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `tap_a_repository`() {
        // Arrange
        val searchBar = composeRule.onNodeWithTag(TestTags.SEARCH_BAR)
        searchBar.assertExists()
        val searchResult = composeRule.onNodeWithTag(TestTags.SEARCH_RESULT)
        searchResult.assertExists()
        // 検索結果一覧を用意
        searchBar.performTextInput("test2")
        searchBar.performImeAction()
        val expectedRepository = Repository(
            name = "actions-marketplace-validations/kokoichi206_action-URL-watcher",
            repoUrl = "https://github.com/actions-marketplace-validations/kokoichi206_action-URL-watcher",
            ownerIconUrl = "https://avatars.githubusercontent.com/u/112583732?v=4",
            language = "Kotlin",
            stargazersCount = 1,
            watchersCount = 2,
            forksCount = 3,
            openIssuesCount = 4,
        )

        val item = searchResult.onChildren()[0]

        val viewInDetail = composeRule.onNodeWithTag(TestTags.DETAIL_VIEW)
        viewInDetail.assertDoesNotExist()

        // Act
        item.performClick()

        // Assert
        assertEquals(1, repositoryClickedCounter)
        assertEquals(expectedRepository, passedRepository)
    }
}
