import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.aboutlib)
    alias(libs.plugins.parcelize)
    alias(libs.plugins.gms)
}

fun getProperty(propertyKey: String): String {
    val propertiesFile = rootProject.file("local.properties")
    val properties = Properties().apply {
        load(FileInputStream(propertiesFile))
    }
    return properties.getProperty(propertyKey)
}

android {
    namespace = "com.beemer.unofficial.fromis_9"
    compileSdk = 34

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.beemer.unofficial.fromis_9"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String","BASE_URL", getProperty("BASE_URL"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    // 테스트
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // androidx
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.datastore)

    // google
    implementation(libs.material)

    // hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation)
    ksp(libs.hilt.android.compiler)

    // 기타
    implementation(libs.glide)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.themedToggleButton)
    implementation(libs.youtubePlayer)
    implementation(libs.calendar)
    implementation(libs.readmore)
    implementation(libs.aboutlibraries)
    implementation(libs.shimmer)
    implementation(libs.touchImageView)

    coreLibraryDesugaring(libs.desugarJdkLibs)
}