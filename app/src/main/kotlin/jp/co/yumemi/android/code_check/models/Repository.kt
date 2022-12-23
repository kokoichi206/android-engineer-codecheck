package jp.co.yumemi.android.code_check.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Repository(
    val name: String,
    val repoUrl: String,
    val ownerIconUrl: String,
    val language: String,
    val stargazersCount: Long,
    val watchersCount: Long,
    val forksCount: Long,
    val openIssuesCount: Long,
) : Parcelable {

    fun uriEncode(): Repository {
        return Repository(
            name = Uri.encode(name),
            repoUrl = Uri.encode(repoUrl),
            ownerIconUrl = Uri.encode(ownerIconUrl),
            language = Uri.encode(language),
            stargazersCount = stargazersCount,
            watchersCount = watchersCount,
            forksCount = forksCount,
            openIssuesCount = openIssuesCount,
        )
    }

    fun uriDecode(): Repository {
        return Repository(
            name = Uri.decode(name),
            repoUrl = Uri.decode(repoUrl),
            ownerIconUrl = Uri.decode(ownerIconUrl),
            language = Uri.decode(language),
            stargazersCount = stargazersCount,
            watchersCount = watchersCount,
            forksCount = forksCount,
            openIssuesCount = openIssuesCount,
        )
    }
}
