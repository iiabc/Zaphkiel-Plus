taboolib { subproject = true }

dependencies {
    api(project(":project:common"))
    compileOnly(project(":project:common-impl"))
    compileOnly("ink.ptms.core:v12104:12104:mapped")
}


