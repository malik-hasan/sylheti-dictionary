
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.BOOLEAN
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.plugin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.sqldelight)
    alias(libs.plugins.room)
}

val sylhetiDictionaryPackage = "oats.mobile.sylhetidictionary"

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

kotlin {
    compilerOptions.freeCompilerArgs.add("-Xexpect-actual-classes")

    androidLibrary {
        namespace = "$sylhetiDictionaryPackage.composeapp"
        compileSdk = libs.versions.android.targetSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
        androidResources.enable = true
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
                implementation(libs.compose.components.resources)
                implementation(libs.compose.foundation)
                implementation(libs.compose.material3)
                implementation(libs.compose.material3.adaptive)
                implementation(libs.compose.runtime)
                implementation(libs.compose.ui)
                implementation(libs.constraintlayout.compose.multiplatform)
                api(libs.datastore.preferences)
                api(libs.kermit)
                implementation(libs.kermit.koin)
                api(project.dependencies.platform(libs.koin.bom))
                implementation(libs.koin.core)
                implementation(libs.koin.compose)
                implementation(libs.koin.compose.viewmodel)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.lifecycle.viewmodel)
                implementation(libs.media3.exoplayer)
                implementation(libs.navigation.compose)
                implementation(libs.room.runtime)
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
                implementation(libs.room.runtime.android)
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
                implementation(libs.vorbisspi)
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

room.schemaDirectory("$projectDir/schemas")

sqldelight.databases.create("DictionaryDatabase").packageName = sylhetiDictionaryPackage

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

buildkonfig {
    packageName = sylhetiDictionaryPackage

    val debug = "DEBUG"
    defaultConfigs {
        buildConfigField(BOOLEAN, debug, "true")
    }

    defaultConfigs("release") {
        buildConfigField(BOOLEAN, debug, "false")
    }
}
