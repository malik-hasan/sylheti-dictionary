
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.room)
}

val sylhetiDictionaryPackage = "oats.mobile.sylhetidictionary"

kotlin {
    targets.all {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")
            }
        }
    }

    androidTarget {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
    }

    listOf(
        iosArm64(),
        iosX64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
            linkerOpts.add("-lsqlite3")
            binaryOption("bundleId", sylhetiDictionaryPackage)
        }
    }

    jvm("desktop")

    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            kotlin.srcDir("build/generated/ksp/metadata")
            dependencies {
                implementation(compose.foundation)
                implementation(compose.runtime)
                implementation(compose.ui)
//                implementation(compose.material3) # overriding with alpha for m3 expressive
                implementation(compose.components.resources)

                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.datastore.preferences)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kermit)
                implementation(libs.kermit.koin)
                implementation(project.dependencies.platform(libs.koin.bom))
                api(libs.koin.core)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.koin.compose)
                implementation(libs.lifecycle.viewmodel)
                implementation(libs.material3)
                implementation(libs.navigation.compose)
                implementation(libs.room.runtime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.sqldelight.coroutines.extensions)
                implementation(libs.sqldelight.async.extensions)
                implementation(libs.sqlite.bundled)
            }
        }

        val mobileMain by creating {
            dependsOn(commonMain.get())
        }

        androidMain {
            dependsOn(mobileMain)
            dependencies {
                implementation(compose.preview)
                implementation(libs.activity.compose)
                implementation(libs.appcompat)
                implementation(libs.koin.android)
                implementation(libs.lifecycle.runtime.compose)
                implementation(libs.room.runtime.android)
                implementation(libs.core.splashscreen)
                implementation(libs.sqldelight.android.driver)
            }
        }

        val nonAndroidMain by creating {
            dependsOn(commonMain.get())
        }

        iosMain {
            dependsOn(mobileMain)
            dependsOn(nonAndroidMain)
            dependencies {
                implementation(libs.sqldelight.native.driver)
            }
        }

        val desktopMain by getting {
            dependsOn(nonAndroidMain)
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutines.swing)
                implementation(libs.lifecycle.runtime.compose)
                implementation(libs.sqldelight.sqlite.driver)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

dependencies {
    listOf(
        "kspAndroid",
        "kspIosArm64",
        "kspIosX64",
        "kspIosSimulatorArm64",
        "kspDesktop"
    ).forEach {
        add(it, libs.room.compiler)
    }
}

room.schemaDirectory("$projectDir/schemas")

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
    }

    androidResources.localeFilters.addAll(setOf("en", "bn"))

    packaging.resources.excludes.add("/META-INF/{AL2.0,LGPL2.1}")

    buildTypes.getByName("release").isMinifyEnabled = true

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
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

sqldelight.databases.create("DictionaryDatabase").packageName = sylhetiDictionaryPackage
