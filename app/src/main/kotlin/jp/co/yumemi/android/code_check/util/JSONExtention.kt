package jp.co.yumemi.android.code_check.util

import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.models.User
import org.json.JSONObject

/**
 * GitHub API から取得される JSON から [Repository] を取得する。
 *
 * see documentation:
 * https://docs.github.com/en/rest/search?apiVersion=2022-11-28#search-repositories
 */
fun JSONObject.toRepository(): Repository {
    val name = optString("full_name")
    val repoUrl = optString("html_url")
    val ownerIconUrl = optJSONObject("owner")?.optString("avatar_url") ?: "empty_url"
    val language = optString("language")
    val stargazersCount = optLong("stargazers_count")
    val watchersCount = optLong("watchers_count")
    val forksCount = optLong("forks_count")
    val openIssuesCount = optLong("open_issues_count")

    return Repository(
        name = name,
        repoUrl = repoUrl,
        ownerIconUrl = ownerIconUrl,
        language = language,
        stargazersCount = stargazersCount,
        watchersCount = watchersCount,
        forksCount = forksCount,
        openIssuesCount = openIssuesCount
    )
}

/**
 * GitHub API から取得される JSON から [User] を取得する。
 *
 * see documentation:
 * https://docs.github.com/ja/rest/search?apiVersion=2022-11-28#search-users
 */
fun JSONObject.toUser(): User {
    val name = optString("login")
    val avatarUrl = optString("avatar_url")
    val htmlUrl = optString("html_url")
    val type = optString("type")

    return User(
        name = name,
        avatarUrl = avatarUrl,
        htmlUrl = htmlUrl,
        type = type,
    )
}
