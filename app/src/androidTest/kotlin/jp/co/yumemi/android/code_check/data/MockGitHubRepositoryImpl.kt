package jp.co.yumemi.android.code_check.data

import jp.co.yumemi.android.code_check.data.repository.GitHubRepository
import jp.co.yumemi.android.code_check.models.Repository

class MockGitHubRepositoryImpl : GitHubRepository {

    companion object {
        // FIXME: 順番によっては失敗しうる気がする。
        var counter = 0
        var passedQuery: String? = null
        var error: Exception? = null

        fun initMock() {
            counter = 0
            passedQuery = null
            error = null
        }
    }

    override suspend fun searchRepositories(query: String): List<Repository> {

        counter += 1
        passedQuery = query

        error?.let { throw it }

        return listOf(
            Repository(
                name = "actions-marketplace-validations/kokoichi206_action-URL-watcher",
                repoUrl = "https://github.com/actions-marketplace-validations/kokoichi206_action-URL-watcher",
                ownerIconUrl = "https://avatars.githubusercontent.com/u/112583732?v=4",
                language = "Kotlin",
                stargazersCount = 1,
                watchersCount = 2,
                forksCount = 3,
                openIssuesCount = 4,
            ),
            Repository(
                name = "kokoichi206/kokoichi206",
                repoUrl = "https://github.com/kokoichi206/kokoichi206",
                ownerIconUrl = "https://avatars.githubusercontent.com/u/52474650?v=4",
                language = "Shell",
                stargazersCount = 2,
                watchersCount = 1,
                forksCount = 1,
                openIssuesCount = 3,
            ),
        )
    }
}
