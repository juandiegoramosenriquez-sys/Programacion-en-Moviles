plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    // KSP: genera el codigo de Room a partir de las anotaciones
    id("com.google.devtools.ksp") version "2.3.12"
}

android {
    namespace = "com.ramos.avisoscampus"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.ramos.avisoscampus"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // ViewModel y viewModelScope: el ViewModel decide que avisar sin usar Context
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Room: guarda el historial de avisos aunque se cierre la app
    implementation("androidx.room:room-runtime:2.8.5")

    // Room con corrutinas: funciones suspend y Flow en el DAO
    implementation("androidx.room:room-ktx:2.8.5")

    // Compilador de Room: genera el DAO y la base de datos
    ksp("androidx.room:room-compiler:2.8.5")

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}
