package jp.co.yumemi.android.code_check.presentation.detail.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import jp.co.yumemi.android.code_check.models.Repository
import jp.co.yumemi.android.code_check.presentation.detail.DetailView
import jp.co.yumemi.android.code_check.presentation.util.Constants


const val detailRoute = "detail_route"

const val name = "name"
const val repoUrl = "repoUrl"
const val iconUrl = "icon_url"
const val language = "language"
const val stars = "stars"
const val watchers = "watchers"
const val forks = "forks"
const val issues = "issues"


fun NavController.navigateToDetailView(repository: Repository) {

    val icon = repository.ownerIconUrl
        .replace("/", Constants.SLASH_ENCODED)
        .replace("?", Constants.QUESTION_ENCODED)
    val url = repository.repoUrl
        .replace("/", Constants.SLASH_ENCODED)
        .replace("?", Constants.QUESTION_ENCODED)
    val repoName = repository.name
        .replace("/", Constants.SLASH_ENCODED)
        .replace("?", Constants.QUESTION_ENCODED)
    this.navigateUp()
    this.navigate(
        detailRoute
                + "/$name=${repoName}&$repoUrl=${url}&$iconUrl=${icon}&$language=${repository.language}"
                + "&$stars=${repository.stargazersCount}&$watchers=${repository.watchersCount}"
                + "&$forks=${repository.forksCount}&$issues=${repository.openIssuesCount}"
    )
}

fun NavGraphBuilder.detailView() {
    composable(
        route = detailRoute
                + "/$name={${name}}&$repoUrl={${repoUrl}}&$iconUrl={${iconUrl}}&$language={${language}}"
                + "&$stars={${stars}}&$watchers={${watchers}}"
                + "&$forks={${forks}}&$issues={${issues}}",

        arguments = listOf(
            navArgument(name) { type = NavType.StringType },
            navArgument(repoUrl) { type = NavType.StringType },
            navArgument(iconUrl) { type = NavType.StringType },
            navArgument(language) { type = NavType.StringType },
            navArgument(stars) { type = NavType.LongType },
            navArgument(watchers) { type = NavType.LongType },
            navArgument(forks) { type = NavType.LongType },
            navArgument(issues) { type = NavType.LongType },
        )
    ) { backStackEntry ->

        val name = backStackEntry.arguments?.getString(name)
            ?.replace(Constants.SLASH_ENCODED, "/")
            ?.replace(Constants.QUESTION_ENCODED, "?") ?: ""
        val repoUrl = backStackEntry.arguments?.getString(repoUrl)
            ?.replace(Constants.SLASH_ENCODED, "/")
            ?.replace(Constants.QUESTION_ENCODED, "?") ?: ""
        val iconUrl = backStackEntry.arguments?.getString(iconUrl)
            ?.replace(Constants.SLASH_ENCODED, "/")
            ?.replace(Constants.QUESTION_ENCODED, "?") ?: ""
        val language = backStackEntry.arguments?.getString(language) ?: ""
        val stars = backStackEntry.arguments?.getLong(stars) ?: 0L
        val watchers = backStackEntry.arguments?.getLong(watchers) ?: 0L
        val forks = backStackEntry.arguments?.getLong(forks) ?: 0L
        val issues = backStackEntry.arguments?.getLong(issues) ?: 0L
        val repository = Repository(
            name = name,
            repoUrl = repoUrl,
            ownerIconUrl = iconUrl,
            language = language,
            stargazersCount = stars,
            watchersCount = watchers,
            forksCount = forks,
            openIssuesCount = issues
        )

        DetailView(repository = repository)
    }
}
