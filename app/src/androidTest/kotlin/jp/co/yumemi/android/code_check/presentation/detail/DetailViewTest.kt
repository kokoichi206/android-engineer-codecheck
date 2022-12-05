package jp.co.yumemi.android.code_check.presentation.detail

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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class DetailViewTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    private val repository = Repository(
        name = "actions-marketplace-validations/kokoichi206_action-URL-watcher",
        repoUrl = "https://github.com/actions-marketplace-validations/kokoichi206_action-URL-watcher",
        ownerIconUrl = "https://avatars.githubusercontent.com/u/112583732?v=4",
        language = "Kotlin",
        stargazersCount = 1,
        watchersCount = 2,
        forksCount = 3,
        openIssuesCount = 4,
    )

    @Before
    fun setUp() {
        hiltRule.inject()

        composeRule.activity.setContent {
            DetailView(repository)
        }
    }

    @After
    fun tearDown() {
        MockGitHubRepositoryImpl.initMock()
    }

    @Test
    fun `DetailView_show_correctly`() {
        // Arrange

        // Act

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
}
