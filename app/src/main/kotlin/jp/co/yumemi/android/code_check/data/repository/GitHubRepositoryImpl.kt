package jp.co.yumemi.android.code_check.data.repository

import jp.co.yumemi.android.code_check.data.GitHubAPI
import jp.co.yumemi.android.code_check.models.Repository

/**
 * [GitHubRepository] に対する実装クラス。
 */
class GitHubRepositoryImpl(
    private val api: GitHubAPI,
) : GitHubRepository {

    override suspend fun searchRepositories(query: String): List<Repository> {
        return api.searchRepositories(query)
    }
}
