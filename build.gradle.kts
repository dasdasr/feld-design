plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.test) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply false
}

val resolvedVersion: String = providers.gradleProperty("versionName")
    .orElse(providers.environmentVariable("VERSION"))
    .orElse("0.0.1-dev")
    .get()

allprojects {
    group = "com.github.dasdasr"
    version = resolvedVersion
}
