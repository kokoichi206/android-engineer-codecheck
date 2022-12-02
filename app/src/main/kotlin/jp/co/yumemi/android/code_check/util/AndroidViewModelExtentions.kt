package jp.co.yumemi.android.code_check.util

import android.content.Context
import androidx.lifecycle.AndroidViewModel

val AndroidViewModel.context: Context
    get() = getApplication()
