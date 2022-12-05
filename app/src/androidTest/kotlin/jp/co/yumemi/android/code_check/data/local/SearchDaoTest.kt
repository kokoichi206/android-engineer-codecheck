package jp.co.yumemi.android.code_check.data.local

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import jp.co.yumemi.android.code_check.data.local.model.SearchEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class SearchDaoTest {

    lateinit var database: SearchDatabase
    private lateinit var dao: SearchDao

    @Before
    fun setUp() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            SearchDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.searchDao
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun topSearched_returns_descending() = runTest {
        // Arrange
        val searched = listOf(
            "golang",
            "golang",
            "python",
            "golang",
            "python",
            "kotlin",
        )
        searched
            .map { SearchEntity(query = it, result = true, 1L) }
            .forEach { dao.insertSearchResult(it) }

        // Act
        val result = dao.topSearched()

        // Assert
        assertEquals(3, result.size)
        assertEquals("golang", result[0])
        assertEquals("python", result[1])
        assertEquals("kotlin", result[2])
    }

    @Test
    fun topSearched_returns_max_10() = runTest {
        // Arrange
        val searched = listOf(
            "golang",
            "c",
            "c++",
            "github",
            "python",
            "gitlab",
            "google",
            "aws",
            "terraform",
            "swiftui",
            "flutter",
            "github-actions",
            "kaggle",
        )
        searched
            .map { SearchEntity(query = it, result = true, 1L) }
            .forEach { dao.insertSearchResult(it) }

        // Act
        val result = dao.topSearched()

        // Assert
        // 10 件のみ取得できていること
        assertEquals(10, result.size)
    }

    @Test
    fun topSearched_does_not_show_failed_search() = runTest {
        // Arrange
        val entities = listOf(
            SearchEntity(query = "kotlin", result = false, 1L),
            SearchEntity(query = "golang", result = true, 1L),
            SearchEntity(query = "flutter", result = false, 1L),
        )
        entities.forEach { dao.insertSearchResult(it) }

        // Act
        val result = dao.topSearched()

        // Assert
        // 成功した1件のみ取得されること
        assertEquals(1, result.size)
        assertEquals("golang", result[0])
    }
}
