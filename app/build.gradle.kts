plugins {
    pluginsList.forEach{ if(it == checkUpdate) id(it) version "1.5.0" else id(it) }
}
dependencies { implementations() }
android {
    compileSdk = 31
    defaultConfig {
        applicationId = appId
        minSdk = 26
        targetSdk = 31
        versionCode = 1
        versionName = appVersion
        testInstrumentationRunner = testRunner
        vectorDrawables { useSupportLibrary = true }
        buildTypes {
            release {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile(proGName), proGRules)
            }
        }
        composeOptions { kotlinCompilerExtensionVersion = composeVersion }
        compileOptions { sourceCompatibility = javaVersion; targetCompatibility = javaVersion }
        kotlinOptions { jvmTarget = jvm; freeCompilerArgs = argsList }
        buildFeatures { compose = true }
        packagingOptions { resources { excludes += res } }
    }
}





