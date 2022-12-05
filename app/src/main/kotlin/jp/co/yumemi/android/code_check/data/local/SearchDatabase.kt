package jp.co.yumemi.android.code_check.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import jp.co.yumemi.android.code_check.data.local.model.SearchEntity

@Database(
    entities = [SearchEntity::class],
    version = 1,
)
abstract class SearchDatabase: RoomDatabase() {

    abstract val searchDao: SearchDao

    companion object {
        const val DATABASE_NAME = "search_result_db"
    }
}
