plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.files.zip.unzip.unrar.ultrapro"
    compileSdk = 34

    defaultConfig {

        applicationId = "com.files.zip.unzip.unrar.ultrapro"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

//    buildTypes {
//        release {
//            isMinifyEnabled = false
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
//    }



    signingConfigs {
        create("release") {
            storeFile = file("D:\\ZipExtractorUnZIPUnRAR\\app\\zipextractorunzipunrar.jks")
            storePassword = "Prince@9313"
            keyAlias = "key0"
            keyPassword = "Prince@9313"
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }



    viewBinding {
        enable = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0")
    implementation("com.google.firebase:firebase-analytics:22.1.2")
    implementation("com.google.firebase:firebase-crashlytics:19.2.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")


    implementation ("com.intuit.sdp:sdp-android:1.1.1")
    implementation ("com.intuit.ssp:ssp-android:1.1.1")
    implementation("com.airbnb.android:lottie:6.6.0")
    implementation ("commons-io:commons-io:2.17.0")
    implementation ("com.google.firebase:firebase-messaging:24.0.3")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("net.lingala.zip4j:zip4j:1.3.3")
    implementation ("com.github.antonKozyriatskyi:CircularProgressIndicator:1.3.0")




//    implementation ("com.facebook.shimmer:shimmer:0.5.0")

//    implementation (platform("com.google.firebase:firebase-bom:32.1.1"))
//    implementation ("com.google.firebase:firebase-analytics")
//    implementation ("com.google.firebase:firebase-config")
//    implementation ("com.google.firebase:firebase-core:21.1.1")
//    implementation ("com.google.firebase:firebase-crashlytics:19.2.1")
//    implementation ("com.google.android.ump:user-messaging-platform:3.1.0")
//    implementation("android.arch.lifecycle:extensions:1.1.1")
    //facebook ads
//    implementation ("com.facebook.android:audience-network-sdk:6.12.0")
//    implementation ("com.google.code.gson:gson:2.11.0")

}