package jp.co.yumemi.android.code_check.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import jp.co.yumemi.android.code_check.data.remote.GitHubAPI
import jp.co.yumemi.android.code_check.data.repository.GitHubRepository
import jp.co.yumemi.android.code_check.data.repository.GitHubRepositoryImpl
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
}
