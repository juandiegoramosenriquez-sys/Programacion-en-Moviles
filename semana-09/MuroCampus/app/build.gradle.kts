plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

    // Serializacion: convierte el JSON en data class (misma version que Kotlin)
    kotlin("plugin.serialization") version "2.2.10"

    // KSP: genera el codigo de Room a partir de las anotaciones
    id("com.google.devtools.ksp") version "2.3.12"
}

android {
    namespace = "com.ramos.murocampus"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.ramos.murocampus"
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

    // ViewModel y viewModelScope: corrutinas que no bloquean la pantalla
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Retrofit: convierte una interface en llamadas HTTP
    implementation("com.squareup.retrofit2:retrofit:2.11.0")

    // Conversor de Retrofit que usa kotlinx.serialization para leer el JSON
    implementation("com.squareup.retrofit2:converter-kotlinx-serialization:2.11.0")

    // kotlinx.serialization: pasa de JSON a data class y al reves
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // OkHttp: hace la conexion HTTP real
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Logging de OkHttp: muestra en Logcat las peticiones y el JSON
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // collectAsStateWithLifecycle: la pantalla observa el StateFlow del ViewModel
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.7")

    // Room: base de datos local para ver las publicaciones sin internet
    implementation("androidx.room:room-runtime:2.8.5")

    // Room con corrutinas: funciones suspend en el DAO
    implementation("androidx.room:room-ktx:2.8.5")

    // Compilador de Room: genera el DAO y la base de datos
    ksp("androidx.room:room-compiler:2.8.5")

    // DataStore: recuerda si el usuario eligio modo claro u oscuro
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Iconos de Material: buscar, editar, eliminar, sol y luna
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
}