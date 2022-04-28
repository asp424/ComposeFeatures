import org.gradle.api.Project
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.dsl.ScriptHandler
import org.gradle.api.initialization.dsl.ScriptHandler.CLASSPATH_CONFIGURATION
import org.gradle.api.tasks.Delete
import org.gradle.kotlin.dsl.register
import java.io.File

val ScriptHandler.scriptBuilder
    get() = run { gradleList.forEach {
        dependencies.add(CLASSPATH_CONFIGURATION, it) }; repositories.repository
    }

val RepositoryHandler.repository get() = run { google(); mavenCentral() }

fun Project.clearProject(file: File) = tasks.register("type", Delete::class) { delete(file) }














