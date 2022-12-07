package jp.co.yumemi.android.code_check.data

import jp.co.yumemi.android.code_check.data.repository.GitHubRepository
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.models.User

class MockGitHubRepositoryImpl : GitHubRepository {

    companion object {
        // FIXME: 順番によっては失敗しうる気がする。
        var searchRepositoriesCounter = 0
        var searchUsersCounter = 0
        var passedQuery: String? = null
        var passedPage = 0
        var error: Exception? = null
        var repositories = listOf(
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
        var users = listOf(
            User(
                name = "kokoichi206",
                avatarUrl = "https://avatars.githubusercontent.com/u/112583732?v=4",
                htmlUrl = "https://github.com/kokoichi206",
                type = "User",
            ),
            User(
                name = "kokoichi2",
                avatarUrl = "https://avatars.githubusercontent.com/u/112583732?v=4",
                htmlUrl = "https://github.com/kokoichi2",
                type = "User",
            ),
        )

        fun initMock() {
            searchRepositoriesCounter = 0
            searchUsersCounter = 0
            passedQuery = null
            passedPage = 0
            error = null
            repositories = listOf(
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
            users = listOf(
                User(
                    name = "kokoichi206",
                    avatarUrl = "https://avatars.githubusercontent.com/u/112583732?v=4",
                    htmlUrl = "https://github.com/kokoichi206",
                    type = "User",
                ),
                User(
                    name = "kokoichi2",
                    avatarUrl = "https://avatars.githubusercontent.com/u/112583732?v=4",
                    htmlUrl = "https://github.com/kokoichi2",
                    type = "User",
                ),
            )
        }
    }

    override suspend fun searchRepositories(query: String, page: Int): List<Repository> {

        searchRepositoriesCounter += 1
        passedQuery = query
        passedPage = page

        error?.let { throw it }

        return repositories
    }

    override suspend fun searchUsers(query: String, page: Int): List<User> {

        searchUsersCounter += 1
        passedQuery = query
        passedPage = page

        error?.let { throw it }

        return users
    }
}
