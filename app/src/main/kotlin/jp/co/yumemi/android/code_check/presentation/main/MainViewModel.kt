/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.presentation.main

import android.app.Application
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.data.repository.GitHubRepository
import jp.co.yumemi.android.code_check.data.repository.SearchResultRepository
import jp.co.yumemi.android.code_check.presentation.util.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainFragment で使用。
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: GitHubRepository,
    private val searchRepository: SearchResultRepository,
    private val application: Application,
) : ViewModel() {

    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    var lastApiCallSucceeded = true

    fun searchResults(inputText: String) {
        // pagination を初期化する
        _uiState.update { it.copy(page = Constants.START_PAGINATION) }

        fetchRepositories(inputText, _uiState.value.page)
    }

    fun onScrollEnd() {
        if (lastApiCallSucceeded) {
            fetchRepositories(_uiState.value.searchInput.text, _uiState.value.page)
        }
    }

    private fun fetchRepositories(query: String, page: Int) {

        // API 呼び出し前のバリデーション
        // query パラメータが空の場合 422 が返る
        if (query.isBlank() || uiState.value.isLoading) {
            return
        }

        Log.d(TAG, "API call start with: query=$query and page=$page")
        viewModelScope.launch {
            try {
                _uiState.update {
                    it.copy(
                        isLoading = true,
                        repositories = if (page == 1) emptyList() else it.repositories,
                    )
                }

                val result = repository.searchRepositories(query = query, page = page)

                if (result.isEmpty()) {
                    val noRecordMsg = application.getString(R.string.noRecordFound)
                    _uiState.update {
                        it.copy(isLoading = false, error = noRecordMsg)
                    }
                    // 失敗扱いとして、次回の実行をさせない
                    lastApiCallSucceeded = false
                    return@launch
                }
                val newResult = _uiState.value.repositories + result

                Log.d(TAG, "newResult contains: ${newResult.size}")
                _uiState.update { it.copy(isLoading = false, repositories = newResult, page = it.page + 1) }

                lastApiCallSucceeded = true
                searchRepository.insertRecord(query, true)
                fetchSearchRecent()
            } catch (e: Exception) {
                lastApiCallSucceeded = false
                e.localizedMessage?.let { msg ->
                    Log.e(TAG, msg)

                    val errorMsg = application.getString(R.string.apiErrorMessage)
                    _uiState.update {
                        it.copy(isLoading = false, error = errorMsg)
                    }
                }
                searchRepository.insertRecord(query, false)
            }
        }
    }

    fun fetchSearchRecent() {
        viewModelScope.launch {
            val recent = searchRepository.getTopSearched()
            _uiState.update { it.copy(recent = recent) }
        }
    }

    fun setSearchInput(inputText: TextFieldValue) {
        if (inputText.text.isEmpty()) {
            _uiState.update { it.copy(repositories = emptyList()) }
        }
        _uiState.update { it.copy(searchInput = inputText) }
    }

    fun setShowRecent(showRecent: Boolean) {
        _uiState.update { it.copy(showRecent = showRecent) }
    }

    fun resetError() {
        _uiState.update { it.copy(error = "") }
    }
}
