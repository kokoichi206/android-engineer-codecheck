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

    override suspend fun searchRepositories(query: String, page: Int): List<Repository> {
        return api.searchRepositories(query, page)
    }

    override suspend fun searchUsers(query: String, page: Int): List<User> {
        return api.searchUsers(query, page)
    }
}
