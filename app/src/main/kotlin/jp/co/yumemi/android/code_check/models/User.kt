package jp.co.yumemi.android.code_check.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val avatarUrl: String,
    val htmlUrl: String,
    val type: String,
) : Parcelable
