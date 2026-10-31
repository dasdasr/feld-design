plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.detekt)
    id("maven-publish")
}

android {
    namespace = "com.feldelysium.designsystem"
    compileSdk = 37
    resourcePrefix = "feld_"

    defaultConfig {
        minSdk = 24
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    lint {
        abortOnError = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

kotlin {
    jvmToolchain(17)
    explicitApi()
}

detekt {
    buildUponDefaultConfig = true
    config.setFrom("$rootDir/config/detekt/detekt.yml")
    parallel = true
    autoCorrect = !providers.environmentVariable("CI").isPresent
}

dependencies {
    api(platform(libs.androidx.compose.bom))

    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.animation.core)

    debugImplementation(libs.androidx.compose.ui.tooling)
    detektPlugins(libs.detekt.rules.ktlint.wrapper)
}

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                from(components["release"])
                groupId = "com.github.dasdasr"
                artifactId = "feld-design"
                version = "0.1.0"
            }
        }
    }
}
