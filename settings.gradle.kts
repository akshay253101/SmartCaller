rootProject.name = "SmartCaller"

include(":app")

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
}

val authToken: String by settings

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositories {
        google()
        jcenter()
    }
}