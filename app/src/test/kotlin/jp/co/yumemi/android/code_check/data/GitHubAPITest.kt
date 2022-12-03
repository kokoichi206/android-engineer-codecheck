package jp.co.yumemi.android.code_check.data

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.ktor.utils.io.*
import jp.co.yumemi.android.code_check.models.Repository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Test

/**
 * Ktor を使っている GitHub の単体テスト。
 * レスポンスには MockEngine を使用している。
 */
class GitHubAPITest {

    @Test
    fun `searchRepositories_correctly`() {
        // Arrange
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"items":[{"full_name":"kokoichi206/routines", "language":"Golang", "stargazers_count": 3}, {}]}"""),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val query = "my_query"

        // Act
        var result: List<Repository>
        runBlocking {
            val client = HttpClient(mockEngine)
            val api = GitHubAPI(client)

            result = api.searchRepositories(query)
        }

        // Assert
        // リクエストの確認
        assertEquals(1, mockEngine.requestHistory.size)
        val request = mockEngine.requestHistory[0]
        assertEquals("https://api.github.com/search/repositories?q=$query", request.url.toString())
        // accept に application/vnd.github.v3+json を設定することが、ドキュメントで推奨されている
        // https://docs.github.com/ja/rest/search?apiVersion=2022-11-28#search-repositories--parameters
        val headers = request.headers
        assertEquals("application/vnd.github.v3+json", headers["Accept"])

        // レスポンスの確認
        assertEquals(2, result.size)
        // 中身が正しいかのテスト（詳細は [JSONExtentionKtTest] で確認している）
        val repository = result[0]
        assertEquals("kokoichi206/routines", repository.name)
        assertEquals("Golang", repository.language)
    }

    @Test(expected = io.ktor.client.features.ServerResponseException::class)
    fun `searchRepositories_can_throw_exception`() {
        // Arrange
        val mockEngine = MockEngine { request ->
            respond(
                content = ByteReadChannel("""{"items":[{"full_name":"kokoichi206/routines", "language":"Golang", "stargazers_count": 3}, {}]}"""),
                // ドキュメントに記載の、発生しうるエラー（サーバーエラー）
                status = HttpStatusCode.ServiceUnavailable,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val query = "my_query"

        // Act
        var result: List<Repository>
        runBlocking {
            val client = HttpClient(mockEngine)
            val api = GitHubAPI(client)

            // Test アノテーションで、例外が投げられているかを確認している
            result = api.searchRepositories(query)
        }

        // Assert
        // リクエストの確認
        assertEquals(1, mockEngine.requestHistory.size)
        val request = mockEngine.requestHistory[0]
        assertEquals("https://api.github.com/search/repositories?q=$query", request.url.toString())
        // accept に application/vnd.github.v3+json を設定することが、ドキュメントで推奨されている
        // https://docs.github.com/ja/rest/search?apiVersion=2022-11-28#search-repositories--parameters
        val headers = request.headers
        assertEquals("application/vnd.github.v3+json", headers["Accept"])

        // レスポンスの確認
        assertEquals(0, result.size)
    }
}