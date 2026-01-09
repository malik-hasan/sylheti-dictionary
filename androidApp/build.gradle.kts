import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.plugin.compose)
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.kermit)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.android)
    implementation(libs.activity.compose)
    implementation(libs.lifecycle.runtime.compose)
}

val sylhetiDictionaryPackage = "oats.mobile.sylhetidictionary"

android {
    namespace = sylhetiDictionaryPackage
    compileSdk = libs.versions.android.targetSdk.get().toInt()

    defaultConfig {
        applicationId = sylhetiDictionaryPackage
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }

    androidResources.localeFilters.addAll(setOf("en", "bn"))

    packaging.resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")

    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    kotlin.compilerOptions.jvmTarget.set(JvmTarget.JVM_25)

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_25
        targetCompatibility = JavaVersion.VERSION_25
    }
}
