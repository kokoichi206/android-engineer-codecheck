package jp.co.yumemi.android.code_check.data.repository

import jp.co.yumemi.android.code_check.models.Repository

/**
 * データ層のリポジトリ。
 */
interface GitHubRepository {

    suspend fun searchRepositories(query: String): List<Repository>
}
