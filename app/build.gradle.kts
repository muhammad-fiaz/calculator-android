import com.android.build.api.dsl.ApplicationExtension
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.aboutlibraries)
}

configure<ApplicationExtension> {
    namespace = "dev.fiaz.calculator"
    compileSdk = 37

    defaultConfig {
        applicationId = "dev.fiaz.calculator"
        minSdk = 23
        targetSdk = 37
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }

        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "DEVELOPER_NAME", "\"Muhammad Fiaz\"")
        buildConfigField("String", "DEVELOPER_EMAIL", "\"contact@muhammadfiaz.com\"")
        buildConfigField("String", "ORGANIZATION_NAME", "\"Fiaz Technologies\"")
        buildConfigField("String", "WEBSITE_URL", "\"https://muhammad-fiaz.github.io/\"")
        buildConfigField("String", "ORG_WEBSITE_URL", "\"https://fiaztechnologies.github.io/\"")
        buildConfigField("String", "PRIVACY_URL", "\"https://fiaztechnologies.github.io/privacy/\"")
        buildConfigField("String", "TERMS_URL", "\"https://fiaztechnologies.github.io/terms/\"")
        buildConfigField("String", "DEVELOPER_GITHUB", "\"https://github.com/muhammad-fiaz\"")
        buildConfigField("String", "ORG_GITHUB", "\"https://github.com/FiazTechnologies\"")
        buildConfigField("String", "SPONSOR_URL", "\"https://github.com/sponsors/muhammad-fiaz\"")
    }

    val localProperties = Properties()
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        localPropertiesFile.inputStream().use {
            localProperties.load(it)
        }
    }

    signingConfigs {
        create("uploadConfig") {
            val fileProp = localProperties.getProperty("signing.upload.storeFile")
            if (fileProp != null) {
                storeFile = rootProject.file(fileProp)
                storePassword = localProperties.getProperty("signing.upload.storePassword")
                keyAlias = localProperties.getProperty("signing.upload.keyAlias")
                keyPassword = localProperties.getProperty("signing.upload.keyPassword")
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val fileProp = localProperties.getProperty("signing.upload.storeFile")
            if (fileProp != null) {
                signingConfig = signingConfigs.getByName("uploadConfig")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    ndkVersion = "28.0.12674033"
}

kotlin {
    jvmToolchain(17)
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.aboutlibraries.compose.m3)

    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    implementation(libs.errorprone.annotations)
    ksp(libs.hilt.compiler)

    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)
    implementation(libs.play.review)
    implementation(libs.play.review.ktx)

    implementation(libs.kotlinx.coroutines.android)


    testImplementation(libs.junit)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}
