package uk.gov.pipelines

import org.gradle.api.tasks.Exec
import uk.gov.pipelines.extensions.resolveValeExecutable
import uk.gov.pipelines.extensions.valeConfigFile

val valeSync =
    rootProject.tasks.register("valeSync", Exec::class.java) {
        description = "Sync Vale styles and vocabularies."
        group = "verification"

        doFirst {
            executable = project.resolveValeExecutable()
        }
        workingDir = rootProject.rootDir

        args(
            "sync",
            "--config=${project.valeConfigFile()}",
        )
    }

val vale =
    rootProject.tasks.register("vale", Exec::class.java) {
        description = "Lint the project's markdown and text files with Vale."
        group = "verification"

        doFirst {
            executable = project.resolveValeExecutable()
        }
        workingDir = rootProject.rootDir
        dependsOn(valeSync)

        args(
            ".",
            "--no-wrap",
            "--config=${project.valeConfigFile()}",
            "--glob=!**/{build,.gradle,mobile-android-pipelines}/**",
        )
    }

val check =
    rootProject.tasks.maybeCreate(
        "check",
    )
        .apply {
            dependsOn(vale)
        }
