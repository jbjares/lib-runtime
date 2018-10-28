package de.difuture.ekut.pht.lib.runtime.docker.params

import de.difuture.ekut.pht.lib.data.DockerContainerId
import de.difuture.ekut.pht.lib.runtime.interrupt.InterruptSignaler
import de.difuture.ekut.pht.lib.runtime.interrupt.InterruptHandler

/**
 * Represents the optional parameters that can be passed to Docker run.
 *
 * @author Lukas Zimmermann
 * @see DockerRuntimeClient
 * @since 0.0.5
 */
data class DockerRunOptionalParameters(

    val env: Map<String, String>? = null,
    val network: String? = null,
    val interruptSignaler: InterruptSignaler<DockerContainerId>? = null,
    val interruptHandler: InterruptHandler<DockerContainerId>? = null
)
