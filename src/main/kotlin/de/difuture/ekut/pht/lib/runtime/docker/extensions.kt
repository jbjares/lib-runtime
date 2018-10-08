package de.difuture.ekut.pht.lib.runtime.docker

import de.difuture.ekut.pht.lib.data.DockerContainerId
import de.difuture.ekut.pht.lib.data.DockerImageId
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerCommitOptionalParameters
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerRunOptionalParameters
import jdregistry.client.data.DockerRepositoryName
import jdregistry.client.data.DockerTag

fun DockerRuntimeClient.withDefaultRunParameters(params: DockerRunOptionalParameters): DockerRuntimeClient {

    val outer = this
    return object : DockerRuntimeClient by outer {

        override fun run(
            imageId: DockerImageId,
            commands: List<String>,
            rm: Boolean,
            optionalParams: DockerRunOptionalParameters?
        ) = outer.run(
                imageId,
                commands,
                rm,
                DockerRunOptionalParameters(
                        env = optionalParams?.env ?: params.env,
                        networkId = optionalParams?.networkId ?: params.networkId,
                        interruptSignaler = optionalParams?.interruptSignaler ?: params.interruptSignaler,
                        interruptHandler = optionalParams?.interruptHandler ?: params.interruptHandler))
        }
}

fun DockerRuntimeClient.withDefaultCommitParameters(params: DockerCommitOptionalParameters): DockerRuntimeClient {

    val outer = this
    return object : DockerRuntimeClient by outer {

        override fun commit(
            containerId: DockerContainerId,
            targetRepo: DockerRepositoryName,
            targetTag: DockerTag,
            optionalParams: DockerCommitOptionalParameters?
        ) = outer.commit(
                containerId,
                targetRepo,
                targetTag,
                DockerCommitOptionalParameters(
                        targetHost = optionalParams?.targetHost ?: params.targetHost,
                        author = optionalParams?.author ?: params.author,
                        comment = optionalParams?.comment ?: params.comment))
    }
}
