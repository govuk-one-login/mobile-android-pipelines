package uk.gov.publishing

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.api.model.ObjectFactory
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.provideDelegate
import uk.gov.pipelines.extensions.ProjectExtensions.infoLog
import uk.gov.publishing.handlers.MavenPublishingConfigHandler
import javax.inject.Inject

/**
 * Configuration class used with the [MavenPublishingConfigPlugin].
 */
open class MavenPublishingConfigExtension
    @Inject
    constructor(
        objects: ObjectFactory,
        githubRepositoryName: String,
        mavenGroupId: String,
    ) {
        /**
         * Configuration for the [org.gradle.api.publish.PublishingExtension] made available via the
         * maven publishing plugin.
         */
        val mavenConfigBlock: MavenPublishingConfigHandler =
            objects.newInstance(
                MavenPublishingConfigHandler::class.java,
                githubRepositoryName,
                mavenGroupId,
            )

        /**
         * Applies the configuration contained within the [action] to the internal [mavenConfigBlock].
         */
        fun mavenConfigBlock(action: Action<in MavenPublishingConfigHandler>?) {
            action?.execute(mavenConfigBlock)
        }

        companion object {
            private val Project.githubRepositoryName
                get(): String {
                    val githubRepositoryName: String by project.rootProject.extra
                    return githubRepositoryName
                }

            private val Project.mavenGroupId
                get(): String {
                    val mavenGroupId: String by project.rootProject.extra
                    return mavenGroupId
                }

            /**
             * Extension function that creates an instance of the [MavenPublishingConfigExtension]
             * if necessary.
             *
             * If an instance of the [MavenPublishingConfigExtension] already exists, the function
             * returns that instance instead.
             */
            fun Project.mavenPublishingConfig(): MavenPublishingConfigExtension =
                this.extensions.findByType(
                    MavenPublishingConfigExtension::class.java,
                )?.also {
                    project.infoLog("Found custom publishing configuration extension: $it")
                } ?: this.extensions.create(
                    "mavenPublishingConfig",
                    MavenPublishingConfigExtension::class.java,
                    githubRepositoryName,
                    mavenGroupId,
                )
        }
    }
