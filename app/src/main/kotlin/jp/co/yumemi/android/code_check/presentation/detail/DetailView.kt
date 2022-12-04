package jp.co.yumemi.android.code_check.presentation.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import jp.co.yumemi.android.code_check.models.Repository

@Composable
fun DetailView(repository: Repository) {
    Text(text = repository.toString())
}
