package de.difuture.ekut.pht.lib.runtime.docker

import de.difuture.ekut.pht.lib.data.DockerContainerId
import de.difuture.ekut.pht.lib.data.DockerImageId
import de.difuture.ekut.pht.lib.data.DockerNetworkId

/**
 * Exception intended to be thrown by [DockerRuntimeClient] implementations.
 *
 * @author Lukas Zimmermann
 * @see DockerRuntimeClient
 * @since 0.0.1
 *
 */
open class DockerRuntimeClientException(msg: String?) : Exception(msg) {

    constructor(ex: Exception) : this(ex.message)
}

/**
 * Exception to be thrown when the selected Docker Container does not exits.
 *
 * Typical methods that use this exception are [DockerRuntimeClient.rm] and [DockerRuntimeClient.commitByRebase].
 *
 * @author Lukas Zimmermann
 * @see DockerRuntimeClient
 * @since 0.0.1
 *
 */
data class NoSuchDockerContainerException(val msg: String?, val containerId: DockerContainerId) : DockerRuntimeClientException(msg) {

    constructor(ex: Exception, containerId: DockerContainerId) : this(ex.message, containerId)
}

/**
 * Exception to be thrown when the selected Docker Image does not exist.
 *
 * Typical methods that use this exception are and [IDockerClient.run].
 *
 * @author Lukas Zimmermann
 * @see IDockerClient
 * @since 0.0.1
 *
 */
data class NoSuchDockerImageException(
    val msg: String?,
    val imageId: DockerImageId?,
    val repo: String?
) : DockerRuntimeClientException(msg) {

    constructor(ex: Exception, imageId: DockerImageId) : this(ex.message, imageId, null)
    constructor(ex: Exception, repo: String) : this(ex.message, null, repo)
}

/**
 * Exception to tbe thrown when the selected repository does not exits.
 *
 * Typically thrown by[IDockerClient.pull].
 *
 * @author Lukas Zimmermann
 * @see IDockerClient
 * @since 0.0.1
 *
 */
data class NoSuchDockerRepositoryException(val msg: String?) : DockerRuntimeClientException(msg) {

    constructor(ex: Exception) : this(ex.message)
}

/**
 * Exception to be thrown when the selected Docker Network does not exist.
 *
 * Typically thrown by [IDockerClient.run]
 *
 * @authoer Lukas Zimmermann
 * @see IDockerClient
 * @since 0.0.1
 *
 */
data class NoSuchDockerNetworkException(val msg: String?, val networkId: DockerNetworkId) : DockerRuntimeClientException(msg) {

    constructor(ex: Exception, networkId: DockerNetworkId) : this(ex.message, networkId)
}

/**
 * Exception that can be thrown to signal that the creation of a Docker container has failed
 *
 * Exception can be used for [IDockerClient.run]
 *
 * @author Lukas Zimmermann
 * @see IDockerClient
 * @since 0.0.1
 *
 */
data class CreateDockerContainerFailedException(val msg: String?) : DockerRuntimeClientException(msg) {

    constructor(ex: Exception) : this(ex.message)
}
