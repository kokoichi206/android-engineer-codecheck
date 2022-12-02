/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.util.context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

/**
 * MainFragment で使用。
 */
class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    // 検索結果
    fun searchResults(inputText: String) {

        // API 呼び出し前のバリデーション
        // query パラメータが空の場合 422 が返る
        if (inputText.isBlank()) {
            return
        }

        val client = HttpClient(Android)

        val result = mutableListOf<Repository>()
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }
                val response: HttpResponse = client.get("https://api.github.com/search/repositories") {
                    header("Accept", "application/vnd.github.v3+json")
                    parameter("q", inputText)
                }
                val jsonBody = JSONObject(response.receive<String>())
                val jsonItems = jsonBody.optJSONArray("items")

                jsonItems?.let {
                    for (i in 0 until it.length()) {
                        val jsonItem = it.optJSONObject(i)

                        val name = jsonItem.optString("full_name")
                        val ownerIconUrl = jsonItem.optJSONObject("owner")?.optString("avatar_url") ?: "empty_url"
                        val language = jsonItem.optString("language")
                        val stargazersCount = jsonItem.optLong("stargazers_count")
                        val watchersCount = jsonItem.optLong("watchers_count")
                        val forksCount = jsonItem.optLong("forks_count")
                        val openIssuesCount = jsonItem.optLong("open_issues_count")

                        result.add(
                            Repository(
                                name = name,
                                ownerIconUrl = ownerIconUrl,
                                language = context.getString(R.string.written_language, language),
                                stargazersCount = stargazersCount,
                                watchersCount = watchersCount,
                                forksCount = forksCount,
                                openIssuesCount = openIssuesCount
                            )
                        )
                    }
                }
                lastSearchDate = Date()
                _uiState.update { it.copy(isLoading = false, repositories = result) }

            } catch (e: Exception) {
                e.localizedMessage?.let { msg ->
                    Log.e(TAG, msg)
                    _uiState.update { it.copy(error = msg) }
                }
            } finally {
                client.close()
            }
        }
    }
}
