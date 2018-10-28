package de.difuture.ekut.pht.lib.runtime.docker

import de.difuture.ekut.pht.lib.data.DockerContainerId
import de.difuture.ekut.pht.lib.data.DockerImageId
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerCommitOptionalParameters
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerRunOptionalParameters
import jdregistry.client.data.RepositoryName as DockerRepositoryName
import jdregistry.client.data.Tag as DockerTag
import java.nio.file.Path

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
                        networkId = optionalParams?.networkId ?: params.networkId,
                        interruptSignaler = optionalParams?.interruptSignaler ?: params.interruptSignaler,
                        interruptHandler = optionalParams?.interruptHandler ?: params.interruptHandler))
        }

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
