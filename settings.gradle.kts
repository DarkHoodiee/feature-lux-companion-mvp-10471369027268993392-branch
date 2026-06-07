pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "LuxCompanionV2"

include(":app")
include(":domain")
include(":renderer")
include(":engine")
include(":interaction")
include(":assistant")
include(":widget")
