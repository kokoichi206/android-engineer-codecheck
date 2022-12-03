package jp.co.yumemi.android.code_check

import android.text.Editable
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import jp.co.yumemi.android.code_check.data.MockGitHubRepositoryImpl
import jp.co.yumemi.android.code_check.di.AppModule
import jp.co.yumemi.android.code_check.presentation.MainActivity
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
class AppEndToEnd {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val activityScenarioRule: ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    private lateinit var activity: MainActivity

    @Before
    fun setUp() {
        hiltRule.inject()
        MockGitHubRepositoryImpl.initMock()

        activityScenarioRule.scenario.onActivity {
            activity = it
        }
    }

    @After
    fun tearDown() {
        MockGitHubRepositoryImpl.initMock()
    }

    @Test
    fun `main_fragment_show_correctly`() {
        // Arrange
        val searchInput = activity.findViewById<TextInputEditText>(R.id.searchInputText)
        assertNotNull(searchInput)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertNotNull(recyclerView)

        // Act
        activityScenarioRule.scenario.onActivity {
            searchInput.text = Editable.Factory.getInstance().newEditable("test")
        }

        // Assert
        assertEquals("test", searchInput.text.toString())
        assertEquals(0, recyclerView.size)
    }

    @Test
    fun `search_repositories_work_correctly`() {
        // Arrange
        val searchInput = activity.findViewById<TextInputEditText>(R.id.searchInputText)
        assertNotNull(searchInput)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertNotNull(recyclerView)

        // Act
        activityScenarioRule.scenario.onActivity {
            searchInput.text = Editable.Factory.getInstance().newEditable("test")
            searchInput.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()

        // Assert
        assertEquals("test", searchInput.text.toString())
        // Mock で用意した数表示されていること
        assertEquals(2, recyclerView.size)

        assertEquals(1, MockGitHubRepositoryImpl.counter)
        assertEquals("test", MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `search_repositories_with_empty_text_field_should_not_call_api`() {
        // Arrange
        val searchInput = activity.findViewById<TextInputEditText>(R.id.searchInputText)
        assertNotNull(searchInput)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertNotNull(recyclerView)

        // Act
        activityScenarioRule.scenario.onActivity {
            searchInput.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()

        // Assert
        assertEquals("", searchInput.text.toString())
        assertEquals(0, recyclerView.size)

        // API が呼び出されてないこと
        assertEquals(0, MockGitHubRepositoryImpl.counter)
        assertNull(MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `search_repositories_when_exception_occurs_should_not_crash`() {
        // Arrange
        val searchInput = activity.findViewById<TextInputEditText>(R.id.searchInputText)
        assertNotNull(searchInput)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertNotNull(recyclerView)
        // API (正確には Repository) で擬似的にエラーを吐かせる
        MockGitHubRepositoryImpl.error = Exception("My Custom Exception")

        // Act
        activityScenarioRule.scenario.onActivity {
            searchInput.text = Editable.Factory.getInstance().newEditable("test2")
            searchInput.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()

        // Assert
        assertEquals("test2", searchInput.text.toString())
        // API のレスポンスがないので、リストは更新されていない
        assertEquals(0, recyclerView.size)

        // API が呼び出されていること
        assertEquals(1, MockGitHubRepositoryImpl.counter)
        assertEquals("test2", MockGitHubRepositoryImpl.passedQuery)
    }

    @Test
    fun `tap_a_repository_navigate_to_detail_screen`() {
        // Arrange
        val searchInput = activity.findViewById<TextInputEditText>(R.id.searchInputText)
        assertNotNull(searchInput)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertNotNull(recyclerView)
        // 検索結果一覧を用意
        activityScenarioRule.scenario.onActivity {
            searchInput.text = Editable.Factory.getInstance().newEditable("test2")
            searchInput.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()

        val item = recyclerView[0]

        var viewInDetail = activity.findViewById<TextView>(R.id.languageView)
        assertNull(viewInDetail)

        // Act
        activityScenarioRule.scenario.onActivity {
            item.performClick()
        }
        instrumentation.waitForIdleSync()

        // Assert
        // fragment_detail の一要素から判断する
        viewInDetail = activity.findViewById<TextView>(R.id.languageView)
        assertNotNull(viewInDetail)
    }

    @Test
    fun `navigated_detail_screen_show_correctly`() {
        // Arrange
        val searchInput = activity.findViewById<TextInputEditText>(R.id.searchInputText)
        assertNotNull(searchInput)
        val recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertNotNull(recyclerView)
        // 検索結果一覧を用意
        activityScenarioRule.scenario.onActivity {
            searchInput.text = Editable.Factory.getInstance().newEditable("test2")
            searchInput.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()
        val item = recyclerView[0]

        // Act
        activityScenarioRule.scenario.onActivity {
            item.performClick()
        }
        instrumentation.waitForIdleSync()

        // Assert
        val nameView = activity.findViewById<TextView>(R.id.nameView)
        assertEquals("actions-marketplace-validations/kokoichi206_action-URL-watcher", nameView.text)
        val languageView = activity.findViewById<TextView>(R.id.languageView)
        assertEquals("Written in Kotlin", languageView.text)
        val starsView = activity.findViewById<TextView>(R.id.starsView)
        assertEquals("1 stars", starsView.text)
        val watchersView = activity.findViewById<TextView>(R.id.watchersView)
        assertEquals("2 watchers", watchersView.text)
        val forksView = activity.findViewById<TextView>(R.id.forksView)
        assertEquals("3 forks", forksView.text)
        val openIssuesView = activity.findViewById<TextView>(R.id.openIssuesView)
        assertEquals("4 open issues", openIssuesView.text)
    }

    @Test
    fun `back_key_in_navigated_detail_screen_should_return_main_screen`() {
        // Arrange
        var searchInput = activity.findViewById<TextInputEditText>(R.id.searchInputText)
        var recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        // 検索結果一覧を用意
        activityScenarioRule.scenario.onActivity {
            searchInput.text = Editable.Factory.getInstance().newEditable("test3")
            searchInput.onEditorAction(EditorInfo.IME_ACTION_SEARCH)
        }
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        instrumentation.waitForIdleSync()
        // 一覧から1つ目の要素をクリックする
        activityScenarioRule.scenario.onActivity {
            val item = recyclerView[0]
            item.performClick()
        }
        instrumentation.waitForIdleSync()
        // 詳細画面にいることを確認する
        var viewInDetail = activity.findViewById<TextView>(R.id.languageView)
        assertNotNull(viewInDetail)
        recyclerView = activity.findViewById<RecyclerView>(R.id.recyclerView)
        assertNull(recyclerView)

        // Act
        activityScenarioRule.scenario.onActivity {
            // バックキーが押されたと仮定
            activity.onBackPressed()
        }
        instrumentation.waitForIdleSync()

        // Assert
        // 詳細画面が表示されていないこと
        viewInDetail = activity.findViewById(R.id.languageView)
        assertNull(viewInDetail)
        // メイン画面が表示されていること
        searchInput = activity.findViewById<TextInputEditText>(R.id.searchInputText)
        assertNotNull(searchInput)
        recyclerView = activity.findViewById(R.id.recyclerView)
        assertNotNull(recyclerView)
        // 検索文字列・結果が消えていないこと
        assertEquals("test3", searchInput.text.toString())
        assertEquals(2, recyclerView.size)
    }
}