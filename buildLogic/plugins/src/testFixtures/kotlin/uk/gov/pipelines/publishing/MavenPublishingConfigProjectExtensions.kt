package uk.gov.pipelines.publishing

import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties
import uk.gov.pipelines.extras.BUILD_LOGIC_DIR

object MavenPublishingConfigProjectExtensions {
    const val GITHUB_REPOSITORY_NAME = "my-repo"
    const val MAVEN_GROUP_ID = "uk.gov"

    fun Project.configurePublishingPlugin(
        githubRepositoryName: String = GITHUB_REPOSITORY_NAME,
        mavenGroupId: String = MAVEN_GROUP_ID,
        buildLogicDir: String = BUILD_LOGIC_DIR,
    ) {
        this.rootProject.extraProperties.set("githubRepositoryName", githubRepositoryName)
        this.rootProject.extraProperties.set("mavenGroupId", mavenGroupId)
        this.rootProject.extraProperties.set(
            "buildLogicDir",
            buildLogicDir,
        )
    }
}
