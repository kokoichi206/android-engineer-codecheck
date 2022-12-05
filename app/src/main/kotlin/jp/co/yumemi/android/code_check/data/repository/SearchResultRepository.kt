package jp.co.yumemi.android.code_check.data.repository

interface SearchResultRepository {

    suspend fun getTopSearched(): List<String>

    suspend fun insertRecord(query: String, result: Boolean)
}
