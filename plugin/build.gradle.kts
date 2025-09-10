dependencies {
    taboo("ink.ptms:um:1.1.5")
    taboo("com.notkamui.libs:keval:1.1.1")
}

taboolib {
    description {
        name(rootProject.name)
        contributors {
            name("坏黑")
        }
    }
    relocate("ink.ptms.um", "ink.ptms.zaphkiel.um")
    relocate("com.notkamui.keval", "ink.ptms.zaphkiel.libs.keval")
}

tasks {
    jar {
        // 构件名
        archiveFileName.set("${rootProject.name}-${archiveFileName.get().substringAfter('-')}")
        // 打包子项目源代码
        rootProject.subprojects.forEach { from(it.sourceSets["main"].output) }
    }
}