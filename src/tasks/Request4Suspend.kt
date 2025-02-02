package tasks

import contributors.*

suspend fun loadContributorsSuspend(service: GitHubService, req: RequestData): List<User> {
    val repos = service.getOrgRepos(req.org).body()
    val users = mutableListOf<User>()
    repos?.forEach {
        val usersFromRequest = service.getRepoContributors(
            owner = req.org,
            repo = it.name,
        ).body()
        usersFromRequest?.let { remoteUsers ->
            users.addAll(remoteUsers)
        }
    }
    return users.aggregate()
}