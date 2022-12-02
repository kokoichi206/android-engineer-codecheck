package jp.co.yumemi.android.code_check.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repository(
    val name: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable
