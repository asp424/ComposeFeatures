plugins {
	id("com.android.application")
	id("org.jetbrains.kotlin.android")
	id("kotlin-kapt")
}
dependencies {
	implementations()
	implementation(project(":expandedcolumn"))
	implementation(files("libs/fantasticprogress-release.aar"))
}

android {
	compileSdk = 32
	defaultConfig {
		applicationId = appId
		minSdk = 26
		targetSdk = 32
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
		kotlinOptions {
			jvmTarget = jvm; freeCompilerArgs = argsList
		}
		buildFeatures { compose = true }
		packagingOptions { resources { excludes += res } }
	}
}






