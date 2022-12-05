package jp.co.yumemi.android.code_check.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import jp.co.yumemi.android.code_check.data.local.model.SearchEntity

@Dao
interface SearchDao {

    /**
     * 検索候補として表示する文字列を取得する。
     * 成功した API 検索のうち、上位 10 件を多い順に取得。
     */
    @Query(
        value = """
            SELECT `query` FROM SearchEntity
            WHERE `result` == 1
            GROUP BY `query`
            ORDER BY COUNT(`query`) DESC
            LIMIT 10
        """
    )
    suspend fun topSearched(): List<String>

    @Insert
    suspend fun insertSearchResult(entity: SearchEntity)
}
