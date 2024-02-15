import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
                freeCompilerArgs += "-Xmulti-platform"
            }
        }
    }
    
    jvm("desktop")
    
    sourceSets {
        val desktopMain by getting
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            api(libs.pre.compose)
            api(libs.pre.compose.viewmodel)
            implementation(libs.androidx.datastore.core)
        }
        desktopMain.dependencies {
            implementation(compose.desktop.currentOs)

            val os = DefaultNativePlatform.getCurrentOperatingSystem()
            val arch = DefaultNativePlatform.getCurrentArchitecture()
            println("当前操作系统: $os, 当前架构: $arch")
            val fxSuffix = when {
                os.isWindows -> "win"
                os.isMacOsX -> {
                    if (arch.isArm64) "mac-aarch64" else "mac"
                }
                os.isLinux -> {
                    if (arch.isArm64) "linux-aarch64" else "linux"
                }
                else -> throw IllegalStateException("不支持的操作系统或CPU架构！")
            }

            implementation("org.openjfx:javafx-base:21.0.1:$fxSuffix")
            implementation("org.openjfx:javafx-controls:21.0.1:$fxSuffix")
            implementation("org.openjfx:javafx-media:21.0.1:$fxSuffix")
            implementation("org.openjfx:javafx-graphics:21.0.1:$fxSuffix")
            implementation("org.openjfx:javafx-swing:21.0.1:$fxSuffix")
        }
    }
}

android {
    namespace = "com.tangping.monologue"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res", "src/commonMain/resources")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.tangping.monologue"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Monologue"
            packageVersion = "1.0.0"
            modules("jdk.unsupported", "jdk.unsupported.desktop")

            macOS {
                iconFile.set(project.file("icon.icns"))
            }

            windows {
                iconFile.set(project.file("icon.ico"))
            }
        }
    }
}
