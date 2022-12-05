package jp.co.yumemi.android.code_check.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.data.MockGitHubRepositoryImpl
import jp.co.yumemi.android.code_check.data.local.SearchDatabase
import jp.co.yumemi.android.code_check.data.repository.GitHubRepository
import jp.co.yumemi.android.code_check.data.repository.SearchResultRepository
import jp.co.yumemi.android.code_check.data.repository.SearchResultRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideGithubRepository(): GitHubRepository {
        return MockGitHubRepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideInMemorySearchDatabase(app: Application): SearchDatabase {
        return Room.inMemoryDatabaseBuilder(
            app,
            SearchDatabase::class.java,
        ).build()
    }

    @Provides
    @Singleton
    fun provideSearchResultRepository(db: SearchDatabase): SearchResultRepository {
        return SearchResultRepositoryImpl(db.searchDao)
    }
}
