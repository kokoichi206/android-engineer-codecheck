/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import dagger.hilt.android.AndroidEntryPoint
import jp.co.yumemi.android.code_check.presentation.main.MainView
import java.util.*

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    companion object {
        // 最終検索日時
        var lastSearchDate: Date? = null

        fun updateLastSearchDate() {
            lastSearchDate = Date()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MainView()
        }
    }
}
