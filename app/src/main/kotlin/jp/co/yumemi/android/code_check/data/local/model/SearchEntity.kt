package jp.co.yumemi.android.code_check.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class SearchEntity(
    val query: String,
    val result: Boolean,
    val searchedAt: Long,
    @PrimaryKey val id: Int? = null
)
