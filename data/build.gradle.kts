plugins {
    id("kotlin")
    alias(libs.plugins.kotlinSerialization)
}

dependencies {
    api(project(":domain"))

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization.json)

    implementation(libs.retrofit)
}