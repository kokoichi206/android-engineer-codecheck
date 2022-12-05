package jp.co.yumemi.android.code_check.presentation.main

import androidx.compose.ui.text.input.TextFieldValue
import jp.co.yumemi.android.code_check.models.Repository

/**
 * MainView 表示用の状態。
 */
data class MainUiState(
    var searchInput: TextFieldValue = TextFieldValue(),
    var isLoading: Boolean = false,
    var error: String = "",
    var repositories: List<Repository> = emptyList(),
    var showRecent: Boolean = true,
    var recent: List<String> = emptyList(),
)
