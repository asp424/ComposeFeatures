import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.kotlin.dsl.DependencyHandlerScope

fun DependencyHandlerScope.implementations() {
    implList.forEach { add(impl, it)}
    //kaptList.forEach { add(kapt, it) }

}







