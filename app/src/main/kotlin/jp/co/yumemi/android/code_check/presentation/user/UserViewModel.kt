/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.presentation.user

import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.data.repository.GitHubRepository
import jp.co.yumemi.android.code_check.data.repository.SearchResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainFragment で使用。
 */
@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: GitHubRepository,
    private val searchRepository: SearchResultRepository,
) : ViewModel() {

    companion object {
        private val TAG = UserViewModel::class.java.simpleName
    }

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    // 検索結果
    fun searchResults(inputText: String) {

        // API 呼び出し前のバリデーション
        // query パラメータが空の場合 422 が返る
        if (inputText.isBlank() || uiState.value.isLoading) {
            return
        }

        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true) }

                val result = repository.searchUsers(inputText)
                _uiState.update { it.copy(isLoading = false, users = result) }
            } catch (e: Exception) {
                e.localizedMessage?.let { msg ->
                    Log.e(TAG, msg)
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                }
            }
        }
    }

    fun setSearchInput(inputText: TextFieldValue) {
        if (inputText.text.isEmpty()) {
            _uiState.update { it.copy(users = emptyList()) }
        }
        _uiState.update { it.copy(searchInput = inputText) }
    }
}
