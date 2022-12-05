package jp.co.yumemi.android.code_check.presentation.user

import androidx.compose.ui.text.input.TextFieldValue
import jp.co.yumemi.android.code_check.models.User

/**
 * UserView 表示用の状態。
 */
data class UserUiState(
    var searchInput: TextFieldValue = TextFieldValue(),
    var isLoading: Boolean = false,
    var error: String = "",
    var users: List<User> = emptyList(),
)
