package jp.co.yumemi.android.code_check.util

import org.json.JSONObject
import org.junit.Assert.*

import org.junit.Test

class JSONExtentionKtTest {

    @Test
    fun `toRepository_convert_correctly`() {
        // Arrange
        val str =
            "{\"id\":454971277,\"node_id\":\"R_kgDOGx5PjQ\",\"name\":\"kokoichi206\",\"full_name\":\"kokoichi206\\/kokoichi206\",\"private\":false,\"owner\":{\"login\":\"kokoichi206\",\"id\":52474650,\"node_id\":\"MDQ6VXNlcjUyNDc0NjUw\",\"avatar_url\":\"https:\\/\\/avatars.githubusercontent.com\\/u\\/52474650?v=4\",\"gravatar_id\":\"\",\"url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\",\"html_url\":\"https:\\/\\/github.com\\/kokoichi206\",\"followers_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/followers\",\"following_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/following{\\/other_user}\",\"gists_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/gists{\\/gist_id}\",\"starred_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/starred{\\/owner}{\\/repo}\",\"subscriptions_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/subscriptions\",\"organizations_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/orgs\",\"repos_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/repos\",\"events_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/events{\\/privacy}\",\"received_events_url\":\"https:\\/\\/api.github.com\\/users\\/kokoichi206\\/received_events\",\"type\":\"User\",\"site_admin\":false},\"html_url\":\"https:\\/\\/github.com\\/kokoichi206\\/kokoichi206\",\"description\":null,\"fork\":false,\"url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\",\"forks_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/forks\",\"keys_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/keys{\\/key_id}\",\"collaborators_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/collaborators{\\/collaborator}\",\"teams_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/teams\",\"hooks_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/hooks\",\"issue_events_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/issues\\/events{\\/number}\",\"events_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/events\",\"assignees_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/assignees{\\/user}\",\"branches_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/branches{\\/branch}\",\"tags_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/tags\",\"blobs_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/git\\/blobs{\\/sha}\",\"git_tags_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/git\\/tags{\\/sha}\",\"git_refs_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/git\\/refs{\\/sha}\",\"trees_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/git\\/trees{\\/sha}\",\"statuses_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/statuses\\/{sha}\",\"languages_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/languages\",\"stargazers_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/stargazers\",\"contributors_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/contributors\",\"subscribers_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/subscribers\",\"subscription_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/subscription\",\"commits_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/commits{\\/sha}\",\"git_commits_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/git\\/commits{\\/sha}\",\"comments_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/comments{\\/number}\",\"issue_comment_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/issues\\/comments{\\/number}\",\"contents_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/contents\\/{+path}\",\"compare_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/compare\\/{base}...{head}\",\"merges_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/merges\",\"archive_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/{archive_format}{\\/ref}\",\"downloads_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/downloads\",\"issues_url\":\"https:\\/\\/api.github.com\\/repos\\/kokoichi206\\/kokoichi206\\/issues{\\/number}\", \"language\":\"Kotlin\", \"stargazers_count\":\"1\", \"watchers_count\":\"2\",\"forks_count\":3, \"open_issues_count\":4}"
        val jsonObject = JSONObject(str)

        // Act
        val repository = jsonObject.toRepository()

        // Assert
        assertEquals("kokoichi206/kokoichi206", repository.name)
        assertEquals("https://avatars.githubusercontent.com/u/52474650?v=4", repository.ownerIconUrl)
        assertEquals("Kotlin", repository.language)
        assertEquals(1, repository.stargazersCount)
        assertEquals(2, repository.watchersCount)
        assertEquals(3, repository.forksCount)
        assertEquals(4, repository.openIssuesCount)
    }

    @Test
    fun `toRepository_with_no_field_in_json_should_not_crash`() {
        // Arrange
        val str = "{\"language\":\"Kotlin\", \"watchers_count\":\"2\",\"forks_count\":3, \"open_issues_count\":4}"
        val jsonObject = JSONObject(str)

        // Act
        val repository = jsonObject.toRepository()

        // Assert
        // 存在しないときは空文字 or 0 が返ってくる
        assertEquals("", repository.name)
        // URL のみ、empty_url が返ってくる
        assertEquals("empty_url", repository.ownerIconUrl)
        assertEquals("Kotlin", repository.language)
        assertEquals(0, repository.stargazersCount)
        assertEquals(2, repository.watchersCount)
        assertEquals(3, repository.forksCount)
        assertEquals(4, repository.openIssuesCount)
    }
}