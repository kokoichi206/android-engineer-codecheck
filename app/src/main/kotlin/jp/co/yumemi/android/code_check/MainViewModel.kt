/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import jp.co.yumemi.android.code_check.MainActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.util.toRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.util.*

/**
 * MainFragment で使用。
 */
class MainViewModel : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    // 検索結果
    fun searchResults(inputText: String) {

        // API 呼び出し前のバリデーション
        // query パラメータが空の場合 422 が返る
        if (inputText.isBlank() || uiState.value.isLoading) {
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

                        result.add(jsonItem.toRepository())
                    }
                }
                lastSearchDate = Date()
                _uiState.update { it.copy(isLoading = false, repositories = result) }

            } catch (e: Exception) {
                e.localizedMessage?.let { msg ->
                    Log.e(TAG, msg)
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                }
            } finally {
                client.close()
            }
        }
    }
}
