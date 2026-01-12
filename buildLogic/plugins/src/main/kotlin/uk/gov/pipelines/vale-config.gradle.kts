package uk.gov.pipelines

import uk.gov.pipelines.extensions.valeConfigFile

val valeSync =
    rootProject.tasks.register("valeSync", Exec::class.java) {
        description = "Lint the project's markdown and text files with Vale."
        group = "verification"
        executable = "vale"
        setArgs(
            listOf(
                "sync",
                "--config=${project.valeConfigFile()}",
            ),
        )
    }

val vale =
    rootProject.tasks.register("vale", Exec::class.java) {
        description = "Lint the project's markdown and text files with Vale."
        group = "verification"
        executable = "vale"
        dependsOn(valeSync)
        setArgs(
            listOf(
                "--no-wrap",
                "--config=${project.valeConfigFile()}",
                "--glob=!**/{build,.gradle,mobile-android-pipelines}/**",
                rootProject.projectDir.toString(),
            ),
        )
    }

val check =
    rootProject.tasks.maybeCreate(
        "check",
    )
        .apply {
            dependsOn("vale")
        }
