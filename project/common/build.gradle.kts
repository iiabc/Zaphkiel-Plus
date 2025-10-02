import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

taboolib { subproject = true }

dependencies {
    compileOnly("ink.ptms.core:v11904:11904:mapped")
    compileOnly("ink.ptms.core:v11200:11200")
}

tasks.withType(KotlinCompile::class.java) {
    kotlinOptions {
        freeCompilerArgs = listOf("-module-name", "zap_common")
    }
}