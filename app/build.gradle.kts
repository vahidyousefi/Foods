plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // for ksp plugin
    id("com.google.devtools.ksp") version ("2.2.20-2.0.3")
}

android {
    namespace = "ir.vy.food"
    compileSdk = 36

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "ir.vy.food"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

//    tasks.withType<KotlinJvmCompile>().configureEach {
//        compilerOptions {
//            jvmTarget.set(JvmTarget.JVM_11)
//            freeCompilerArgs.add("-opt-in=kotlin.RequiresOptIn")
//        }
//    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Glide > "com.github.bumptech.glide:glide:4.16.0" , transformation : "jp.wasabeef:glide-transformations:4.3.0"
    implementation(libs.glide)
    implementation(libs.glide.transformations)

    // lottie Animation > "com.airbnb.android:lottie:6.6.9"
    implementation(libs.lottie)

    // Circle image view > "com.github.abdularis:circularimageview:<latest-version>"
    implementation(libs.circularimageview)

    // Room > "androidx.room:room-runtime:2.8.0"
    implementation(libs.androidx.room.runtime)
    // plugin for Room > "androidx.room:room-compiler:2.8.0"
    ksp(libs.androidx.room.compiler)

    // annotation > "androidx.annotation:annotation:1.9.1"
    implementation(libs.androidx.annotation)
}