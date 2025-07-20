plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

rootProject.name = "PLOS"


include("backend")
include("desktop-client")
include("common")