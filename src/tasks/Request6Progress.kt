package tasks

import contributors.*

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    val repos = service.getOrgRepos(req.org).body()
    val users = mutableListOf<User>()
    repos?.forEach {
        val usersFromRequest = service.getRepoContributors(
            owner = req.org,
            repo = it.name,
        ).body()
        usersFromRequest?.let { remoteUsers ->
            users.addAll(remoteUsers)
            updateResults(users.aggregate(), false)
        }
    }
    return updateResults(users.aggregate(), true)
}
