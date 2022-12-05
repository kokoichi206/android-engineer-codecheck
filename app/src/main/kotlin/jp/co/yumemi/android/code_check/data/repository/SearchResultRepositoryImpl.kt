package jp.co.yumemi.android.code_check.data.repository

import jp.co.yumemi.android.code_check.data.local.SearchDao
import jp.co.yumemi.android.code_check.data.local.model.SearchEntity
import java.util.*

class SearchResultRepositoryImpl(
    private val searchDao: SearchDao,
) : SearchResultRepository {

    override suspend fun getTopSearched(): List<String> {
        return searchDao.topSearched()
    }

    override suspend fun insertRecord(query: String, result: Boolean) {
        return searchDao.insertSearchResult(
            SearchEntity(
                query = query,
                result = result,
                searchedAt = Calendar.getInstance().time.time,
            )
        )
    }
}
