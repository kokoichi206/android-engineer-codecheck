/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.presentation.user

import android.app.Application
import android.util.Log
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jp.co.yumemi.android.code_check.R
import jp.co.yumemi.android.code_check.data.repository.GitHubRepository
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
class UserViewModel @Inject constructor(
    private val repository: GitHubRepository,
    private val application: Application,
) : ViewModel() {

    companion object {
        private val TAG = UserViewModel::class.java.simpleName
    }

    private val _uiState = MutableStateFlow(UserUiState())
    val uiState = _uiState.asStateFlow()

    private var lastApiCallSucceeded = true

    // 検索結果
    fun searchResults(inputText: String) {

        _uiState.update { it.copy(page = Constants.START_PAGINATION) }
        fetchUsers(inputText, _uiState.value.page)
    }

    fun onScrollEnd() {
        if (lastApiCallSucceeded) {
            fetchUsers(_uiState.value.searchInput.text, _uiState.value.page)
        }
    }

    private fun fetchUsers(query: String, page: Int) {
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
                        users = if (page == 1) emptyList() else it.users,
                    )
                }

                val result = repository.searchUsers(query, page)
                if (result.isEmpty()) {
                    val noRecordMsg = application.getString(R.string.noRecordFound)
                    _uiState.update { it.copy(isLoading = false, error = noRecordMsg) }
                    lastApiCallSucceeded = false
                    return@launch
                }
                val newResult = _uiState.value.users + result

                Log.d(TAG, "newResult contains: ${newResult.size}")
                _uiState.update { it.copy(isLoading = false, users = newResult, page = it.page + 1) }

                lastApiCallSucceeded = true
            } catch (e: Exception) {
                e.localizedMessage?.let { msg ->
                    Log.e(TAG, msg)
                    val errorMsg = application.getString(R.string.apiErrorMessage)
                    _uiState.update { it.copy(isLoading = false, error = errorMsg) }
                }
                lastApiCallSucceeded = false
            }
        }
    }

    fun setSearchInput(inputText: TextFieldValue) {
        if (inputText.text.isEmpty()) {
            _uiState.update { it.copy(users = emptyList()) }
        }
        _uiState.update { it.copy(searchInput = inputText) }
    }

    fun resetError() {
        _uiState.update { it.copy(error = "") }
    }
}
