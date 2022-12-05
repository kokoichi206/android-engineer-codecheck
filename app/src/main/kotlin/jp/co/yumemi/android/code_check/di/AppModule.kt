package jp.co.yumemi.android.code_check.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.data.local.SearchDatabase
import jp.co.yumemi.android.code_check.data.remote.GitHubAPI
import jp.co.yumemi.android.code_check.data.repository.GitHubRepository
import jp.co.yumemi.android.code_check.data.repository.GitHubRepositoryImpl
import jp.co.yumemi.android.code_check.data.repository.SearchResultRepository
import jp.co.yumemi.android.code_check.data.repository.SearchResultRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGitHubRepository(): GitHubRepository {
        val api = GitHubAPI()
        return GitHubRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideSearchDatabase(app: Application): SearchDatabase {
        return Room.databaseBuilder(
            app,
            SearchDatabase::class.java,
            SearchDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideSearchResultRepository(db: SearchDatabase): SearchResultRepository {
        return SearchResultRepositoryImpl(db.searchDao)
    }
}
