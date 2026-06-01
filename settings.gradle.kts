rootProject.name = "ktor-sample"

pluginManagement {
    repositories {
        maven( "https://maven.aliyun.com/repository/gradle-plugin" )
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        maven(  "https://maven.aliyun.com/repository/public" )
        maven( "https://maven.aliyun.com/repository/google" )
        maven( "https://maven.aliyun.com/repository/gradle-plugin" )
        maven ("https://maven.aliyun.com/repository/central")
        mavenCentral()
    }
    versionCatalogs {
        create("ktorLibs").from("io.ktor:ktor-version-catalog:3.5.0")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}