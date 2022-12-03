package jp.co.yumemi.android.code_check.util

import jp.co.yumemi.android.code_check.models.Repository
import org.json.JSONObject

/**
 * GitHub API から取得される JSON から [Repository] を取得する。
 *
 * see documentation:
 * https://docs.github.com/en/rest/search?apiVersion=2022-11-28#search-repositories
 */
fun JSONObject.toRepository(): Repository {
    val name = optString("full_name")
    val ownerIconUrl = optJSONObject("owner")?.optString("avatar_url") ?: "empty_url"
    val language = optString("language")
    val stargazersCount = optLong("stargazers_count")
    val watchersCount = optLong("watchers_count")
    val forksCount = optLong("forks_count")
    val openIssuesCount = optLong("open_issues_count")

    return Repository(
        name = name,
        ownerIconUrl = ownerIconUrl,
        language = language,
        stargazersCount = stargazersCount,
        watchersCount = watchersCount,
        forksCount = forksCount,
        openIssuesCount = openIssuesCount
    )
}
