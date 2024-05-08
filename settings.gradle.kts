pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        // Otros repositorios...
        mavenCentral()
        // Otros repositorios...
        jcenter()
        // Otros repositorios...
    }
}

rootProject.name = "Gesgaraj"
include(":app")
