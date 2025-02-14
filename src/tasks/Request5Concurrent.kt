package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User>  = coroutineScope{
    val repos = service.getOrgRepos(req.org).bodyList()
    val usersDeferred = repos.map { repo ->
        async {
            service.getRepoContributors(
                req.org,
                repo.name
            ).also { logUsers(repo, it) }.bodyList()
        }
    }
    val users = usersDeferred.awaitAll().flatten().aggregate()
    users
}

