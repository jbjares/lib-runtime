package de.difuture.ekut.pht.lib.runtime.docker

import de.difuture.ekut.pht.lib.data.DockerContainerId
import de.difuture.ekut.pht.lib.data.DockerImageId
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerCommitOptionalParameters
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerRunOptionalParameters
import jdregistry.client.data.RepositoryName as DockerRepositoryName
import jdregistry.client.data.Tag as DockerTag
import java.nio.file.Path

/**
 * Returns a new [DockerRuntimeClient] where the default parameters of the `run` command
 * are replaced by the provided `param` object. Previous applied default run parameters
 * are overridden.
 *
 * @param params The default run parameters to apply
 *
 * @returrn A [DockerRuntimeClient] which uses the provided default parameters for `run`
 *
 */
fun DockerRuntimeClient.withDefaultRunParameters(params: DockerRunOptionalParameters): DockerRuntimeClient =

    object : DockerRuntimeClient by this {

        override fun run(
            imageId: DockerImageId,
            commands: List<String>,
            rm: Boolean,
            optionalParams: DockerRunOptionalParameters?
        ) =
                this@withDefaultRunParameters.run(
                imageId,
                commands,
                rm,
                DockerRunOptionalParameters(
                        env = optionalParams?.env ?: params.env,
                        network = optionalParams?.network ?: params.network,
                        interruptSignaler = optionalParams?.interruptSignaler ?: params.interruptSignaler,
                        interruptHandler = optionalParams?.interruptHandler ?: params.interruptHandler))
        }

/**
 * Returns a new [DockerRuntimeClient] where the default parameters of the `commit` command
 * are replaced by the provided `param` object. Previous applied default commit parameters
 * are overridden.
 *
 * @param params The default commit parameters to apply
 *
 * @returrn A [DockerRuntimeClient] which uses the provided default parameters for `commit`
 *
 */
fun DockerRuntimeClient.withDefaultCommitParameters(params: DockerCommitOptionalParameters): DockerRuntimeClient =

    object : DockerRuntimeClient by this {

        override fun commitByRebase(
            containerId: DockerContainerId,
            exportFiles: List<Path>,
            from: String,
            targetRepo: DockerRepositoryName,
            targetTag: DockerTag,
            optionalParams: DockerCommitOptionalParameters?
        ) =
                this@withDefaultCommitParameters.commitByRebase(
                        containerId = containerId,
                        exportFiles = exportFiles,
                        from = from,
                        targetRepo = targetRepo,
                        targetTag = targetTag,
                        optionalParams = DockerCommitOptionalParameters(
                                targetHost = optionalParams?.targetHost ?: params.targetHost,
                                author = optionalParams?.author ?: params.author,
                                comment = optionalParams?.comment ?: params.comment
                        ))
    }
