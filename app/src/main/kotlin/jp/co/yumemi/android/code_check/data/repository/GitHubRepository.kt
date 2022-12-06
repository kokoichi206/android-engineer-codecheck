package jp.co.yumemi.android.code_check.data.repository

import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.models.User

/**
 * データ層のリポジトリ。
 */
interface GitHubRepository {

    suspend fun searchRepositories(query: String): List<Repository>

    suspend fun searchUsers(query: String): List<User>
}
