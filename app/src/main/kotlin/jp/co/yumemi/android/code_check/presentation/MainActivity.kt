/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.presentation

import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    companion object {
        // 最終検索日時
        var lastSearchDate: Date? = null

        fun updateLastSearchDate() {
            lastSearchDate = Date()
        }
    }
}
