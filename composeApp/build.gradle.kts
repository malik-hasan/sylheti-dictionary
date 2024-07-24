import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.room)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions.jvmTarget.set(JvmTarget.JVM_11)
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    jvm("desktop")

    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata")
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.ui)
                implementation(compose.material3)
                implementation(compose.components.resources)
                implementation(compose.components.uiToolingPreview)

                implementation(libs.lifecycle.viewmodel)
                implementation(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.sqldelight.coroutines.extensions)
                implementation(libs.room.runtime)
                implementation(libs.sqlite.bundled)
                implementation(libs.datastore.preferences)
                implementation(libs.kermit)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.activity.compose)
            implementation(libs.appcompat)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.koin.android)
            implementation(libs.sqldelight.android.driver)
            implementation(libs.room.runtime.android)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
        val desktopMain by getting
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.lifecycle.runtime.compose)
            implementation(libs.sqldelight.sqlite.driver)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }

    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }
}

dependencies.kspCommonMainMetadata(libs.room.compiler)

room.schemaDirectory("$projectDir/schemas")

val sylhetiDictionaryPackage = "oats.mobile.sylhetidictionary"

android {
    namespace = sylhetiDictionaryPackage
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets.getByName("main") {
        manifest.srcFile("src/androidMain/AndroidManifest.xml")
        res.srcDirs("src/androidMain/res")
        resources.srcDirs("src/commonMain/resources")
    }

    defaultConfig {
        applicationId = sylhetiDictionaryPackage
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        resourceConfigurations.addAll(setOf("en", "bn"))
    }

    packaging.resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")

    buildTypes.getByName("release").isMinifyEnabled = false

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures.compose = true

    dependencies {
        debugImplementation(compose.uiTooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = sylhetiDictionaryPackage
            packageVersion = "1.0.0"
        }
    }
}

sqldelight.databases.create("DictionaryDatabase").packageName.set(sylhetiDictionaryPackage)

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask<*>>().configureEach {
    if (name != "kspCommonMainKotlinMetadata") {
        dependsOn("kspCommonMainKotlinMetadata")
    }
}
