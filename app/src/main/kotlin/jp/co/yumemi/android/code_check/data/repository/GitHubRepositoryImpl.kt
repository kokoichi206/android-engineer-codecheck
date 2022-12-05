package jp.co.yumemi.android.code_check.data.repository

import jp.co.yumemi.android.code_check.data.remote.GitHubAPI
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.models.User

/**
 * [GitHubRepository] に対する実装クラス。
 */
class GitHubRepositoryImpl(
    private val api: GitHubAPI,
) : GitHubRepository {

    override suspend fun searchRepositories(query: String): List<Repository> {
        return api.searchRepositories(query)
    }

    override suspend fun searchUsers(query: String): List<User> {
        return api.searchUsers(query)
    }
}
