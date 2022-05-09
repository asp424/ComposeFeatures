plugins {
	id("com.android.library")
	id("org.jetbrains.kotlin.android")
}

android {
	
	
	compileSdk = 32
	buildTypes {
		defaultConfig {
			vectorDrawables { useSupportLibrary = true }
			
			release {
				isMinifyEnabled = false
				proguardFiles(
					getDefaultProguardFile("proguard-android-optimize.txt"),
					"proguard-rules.pro"
				)
			}
			buildFeatures { compose = true }
			composeOptions { kotlinCompilerExtensionVersion = composeVersion }
			compileOptions { sourceCompatibility = javaVersion; targetCompatibility = javaVersion }
			kotlinOptions {
				jvmTarget = jvm; freeCompilerArgs = argsList
			}
		}
	}
}
	
	dependencies {
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")
		implementation("androidx.compose.ui:ui:$composeVersion")
		implementation("androidx.compose.ui:ui-tooling:$composeVersion")
		implementation("androidx.compose.compiler:compiler:$composeVersion")
		implementation("androidx.compose.foundation:foundation:$composeVersion")
		implementation("androidx.compose.material:material:$composeVersion")
		implementation("androidx.compose.material:material-icons-core:$composeVersion")
		implementation("androidx.compose.material:material-icons-extended:$composeVersion")
		implementation("androidx.compose.animation:animation:$composeVersion")
		implementation("androidx.compose.material3:material3:1.0.0-alpha08")
		
	}