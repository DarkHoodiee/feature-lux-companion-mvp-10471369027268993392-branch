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

rootProject.name = "LUX Hoodie"

include(":app")
include(":domain")
include(":renderer")
include(":animation")
include(":behavior")
include(":attention")
include(":events")
include(":perception")
include(":engine")
include(":ui")
