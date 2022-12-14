package jp.co.yumemi.android.code_check.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.models.User
import jp.co.yumemi.android.code_check.util.toRepository
import jp.co.yumemi.android.code_check.util.toUser
import org.json.JSONObject

/**
 * GitHub API に関するクラス。
 */
class GitHubAPI(
    private val client: HttpClient = HttpClient(Android),
) {

    companion object {
        const val BASE_URL = "https://api.github.com"
    }

    suspend fun searchRepositories(query: String, page: Int): List<Repository> {
        val response: HttpResponse = client.get("$BASE_URL/search/repositories") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", query)
            parameter("page", page)
        }

        val jsonBody = JSONObject(response.receive<String>())
        val jsonItems = jsonBody.optJSONArray("items")

        val result = mutableListOf<Repository>()
        jsonItems?.let {
            for (i in 0 until it.length()) {
                val jsonItem = it.optJSONObject(i)

                result.add(jsonItem.toRepository())
            }
        }

        return result
    }

    suspend fun searchUsers(query: String, page: Int): List<User> {
        val response: HttpResponse = client.get("$BASE_URL/search/users") {
            header("Accept", "application/vnd.github.v3+json")
            parameter("q", query)
            parameter("page", page)
        }

        val jsonBody = JSONObject(response.receive<String>())
        val jsonItems = jsonBody.optJSONArray("items")

        val result = mutableListOf<User>()
        jsonItems?.let {
            for (i in 0 until it.length()) {
                val jsonItem = it.optJSONObject(i)

                result.add(jsonItem.toUser())
            }
        }

        return result
    }
}