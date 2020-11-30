import org.jetbrains.kotlin.gradle.tasks.AbstractKotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
//    kotlin("android.extensions")
}
//
//apply plugin: 'com.android.application'
//apply plugin: 'kotlin-android'
//apply plugin: 'kotlin-android-extensions'

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.moqi.prts"
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions{
        sourceCompatibility(JavaVersion.VERSION_1_8)
        targetCompatibility(JavaVersion.VERSION_1_8)
    }
    kotlinOptions{
        jvmTarget = "1.8"
    }
    buildFeatures{
        viewBinding = true
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.10")
    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.0.3")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.navigation:navigation-fragment:2.3.1")
    implementation("androidx.navigation:navigation-ui:2.3.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.navigation:navigation-fragment-ktx:2.3.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.3.1")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.2")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.3.0")

    implementation("org.greenrobot:eventbus:3.2.0")
}

//android {
//    compileSdkVersion 29
//    buildToolsVersion "29.0.3"
//
//    defaultConfig {
//        applicationId "com.moqi.prts"
//        minSdkVersion 24
//        targetSdkVersion 29
//        versionCode 1
//        versionName "1.0"
//
//        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"
//    }
//
//    buildTypes {
//        release {
//            debuggable true
//            minifyEnabled false
//            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt'), 'proguard-rules.pro'
//        }
//    }
//    compileOptions {
//        sourceCompatibility JavaVersion.VERSION_1_8
//        targetCompatibility JavaVersion.VERSION_1_8
//    }
//    kotlinOptions {
//        jvmTarget = '1.8'
//    }
//}

//dependencies {
//    implementation fileTree(dir: "libs", include: ["*.jar"])
//    implementation "org.jetbrains.kotlin:kotlin-stdlib:1.4.10"
//    implementation 'androidx.core:core-ktx:1.3.1'
//    implementation 'androidx.appcompat:appcompat:1.2.0'
//    implementation 'androidx.constraintlayout:constraintlayout:2.0.3'
//    implementation 'androidx.legacy:legacy-support-v4:1.0.0'
//    implementation 'com.google.android.material:material:1.2.1'
//    implementation 'androidx.navigation:navigation-fragment:2.3.1'
//    implementation 'androidx.navigation:navigation-ui:2.3.1'
//    implementation 'androidx.lifecycle:lifecycle-extensions:2.2.0'
//    implementation 'androidx.navigation:navigation-fragment-ktx:2.3.1'
//    implementation 'androidx.navigation:navigation-ui-ktx:2.3.1'
//    testImplementation 'junit:junit:4.12'
//    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
//    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
//
//    implementation 'org.greenrobot:eventbus:3.2.0'
//}