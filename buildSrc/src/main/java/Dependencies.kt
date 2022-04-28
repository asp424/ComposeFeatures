val implList = with(composeVersion) {
    listOf(
        "androidx.core:core-ktx:1.8.0-alpha06",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1",
        // "androidx.appcompat:appcompat:1.4.1",
        // "androidx.fragment:fragment-ktx:1.4.1",
        // "com.squareup.retrofit2:retrofit:2.9.0",
        // "com.squareup.retrofit2:converter-gson:2.9.0",
        "androidx.lifecycle:lifecycle-runtime-ktx:2.5.0-alpha05",
        // "androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.0-alpha05",
        // "com.google.dagger:dagger:2.41",
        "androidx.compose.ui:ui:$this",
        "androidx.compose.ui:ui-tooling:$this",
        "androidx.compose.compiler:compiler:$this",
        "androidx.compose.foundation:foundation:$this",
        "androidx.compose.material:material:$this",
        "androidx.compose.material:material-icons-core:$this",
        "androidx.compose.material:material-icons-extended:$this",
        "androidx.compose.animation:animation:$this",
        "androidx.compose.material3:material3:1.0.0-alpha08",
        "com.google.accompanist:accompanist-pager-indicators:0.24.1-alpha",
        "com.google.accompanist:accompanist-pager:0.24.1-alpha",
        "com.google.accompanist:accompanist-navigation-animation:0.24.1-alpha",
        // "androidx.room:room-runtime:2.4.2",
        //"androidx.room:room-ktx:2.4.2"
    )
}

/* val kaptList = listOf(
    "com.google.dagger:dagger-compiler:2.41",
    "androidx.room:room-compiler:2.4.2"
) */
val gradleList = listOf(
    "com.android.tools.build:gradle:7.1.0",
    "org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10"
)
const val checkUpdate = "name.remal.check-dependency-updates"

val pluginsList = listOf(
    "com.android.application", "org.jetbrains.kotlin.android", "kotlin-kapt", checkUpdate
)



