package tasks

import contributors.*
import kotlinx.coroutines.*

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User>  {
   return loadContributorNotCancellable(service, req)
    //val repos = service.getOrgRepos(req.org).bodyList()
    //val usersDeferred = repos.map { repo ->
    //    async {
    //        log("starting loading for ${repo.name}")
    //        delay(3000)
    //        service.getRepoContributors(
    //            req.org,
    //            repo.name
    //        ).also { logUsers(repo, it) }.bodyList()
    //    }
    //}
    //val users = usersDeferred.awaitAll().flatten().aggregate()
    //users
}

suspend fun loadContributorNotCancellable(service: GitHubService, req: RequestData): List<User> {
    val repos = service.getOrgRepos(req.org).bodyList()
    val usersDeferred = repos.map { repo ->
        GlobalScope.async {
            log("starting loading for ${repo.name}")
            delay(3000)
            service.getRepoContributors(
                req.org,
                repo.name
            ).also { logUsers(repo, it) }.bodyList()
        }
    }
    val users = usersDeferred.awaitAll().flatten().aggregate()
    return users
}