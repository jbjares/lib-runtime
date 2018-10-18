package de.difuture.ekut.pht.lib.runtime.docker

import de.difuture.ekut.pht.lib.data.DockerContainerId
import de.difuture.ekut.pht.lib.data.DockerContainerOutput
import de.difuture.ekut.pht.lib.data.DockerImageId
import de.difuture.ekut.pht.lib.data.DockerNetworkId
import de.difuture.ekut.pht.lib.runtime.RuntimeClient
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerCommitOptionalParameters
import de.difuture.ekut.pht.lib.runtime.docker.params.DockerRunOptionalParameters
import jdregistry.client.data.DockerRepositoryName
import jdregistry.client.data.DockerTag

/**
 * Docker client api that a station needs to implement for using the library components.
 *
 * Interface for implementing Docker Clients that can be used for Train interfaces. The api
 * was designed with two key considerations in mind:
 * * The api represents the minimal set of operations the implementing Docker client needs to support to be used
 *   in a PHT setting.
 * * Each method reflects an operation and a subset of the corresponding options of the data CLI.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
interface DockerRuntimeClient : RuntimeClient {

    /**
     * Runs (create and start) the Docker image with the given commands, waits for the resulting container to exit.
     *
     * This method reflects a simplified version of the `data run` trainCommand, which only supports specifying
     * the commands to be passed to the Docker image and optionally whether the exited container should be removed
     * after it has exited (reflecting the `--rm` option of `data run`).
     *
     * *Contract:* If the container exits, the statusCode code has to be reflected in the returned [DockerContainerOutput]
     * object. If something prevents an exited container to be created, the method needs to fail by throwing an
     * exception. Specifically, if the specified Docker Image does not exist, the method should throw
     * [NoSuchDockerImageException].
     *
     * Furthermore, note that this interface does not support the procedural separation of creating and
     * starting containers, as, in the current state of concerns, this separation is considered to be meaningless
     * for the PHT library.
     *
     * @param imageId The Image ID of the Docker Image that should be run.
     * @param commands The list of Strings that is passed to the image.
     * @param rm Whether the Docker client should remove the container after it has been exited.
     * @param env The environment variables that should be made available to the container
     * @param networkId The [DockerNetworkId] that the run container will attach to.
     * @param interruptHandler A [IInterruptHandler] that will be used to interrupt the running
     * container once the timeout has been reached.
     *
     * @return [DockerContainerOutput] object describing the output of the Docker run trainCommand.
     *
     */
    fun run(
        imageId: DockerImageId,
        commands: List<String>,
        rm: Boolean,
        optionalParams: DockerRunOptionalParameters? = null
    ): DockerContainerOutput

    /**
     * Removes container with specified ID.
     *
     * This trainCommand resembles the `data rm` command.
     *
     * *Contract:* If something prevents the container to be removed, the method needs to fail by throwing an exception.
     *  Specifically, if the specified container does not exist,
     *  the method should throw [NoSuchDockerContainerException].
     *
     * @param containerId The [DockerContainerId] of the container to be removed.
     *
     */
    fun rm(containerId: DockerContainerId)

    /**
     * Pulls the repository specified by [DockerRepositoryName] and [DockerTag].
     *
     * Resembles the `data pull` trainCommand. Unlike the Docker CLI, the dockerTag `latest` is never implied, as
     * it does not bear any meaning in the PHT context. Hence, the dockerTag is a required parameter.
     *
     * *Contract:* If the targeted image cannot be created locally for some reason, the method needs to fail by throwing
     * and exception. Note that trying to pull an image which already exists locally is never a failure. This is
     * compatible with the `data pull` trainCommand. Furthermore, if the selected repository and dockerTag do not point to a
     * valid Docker image, the method should throw [NoSuchDockerRepositoryException].
     *
     * @param host The remote host, designated as a [String]
     * @param port The port the pull should target, represented as [Int]
     * @param repo The [DockerRepositoryName] of the repository to be pulled.
     * @param tag The [DockerTag] of the repository to be pulled.
     *
     * @return [DockerImageId] of the image retrieved via pulling.
     *
     */
    fun pull(repo: DockerRepositoryName, tag: DockerTag, host: String? = null): DockerImageId

    /**
     * Pushes the specified data image via the provided [DockerRepositoryName] and [DockerTag].
     *
     * Resembles the `data push` trainCommand. Unlike the Docker CLI, the dockerTag `latest` is never implied, as it does not
     * bear any meaning in the PHT context. Hence, the dockerTag is a required parameter.
     *
     * *Contract:* If anything prevents the image to be pushed to the registry (like networking errors), the
     * method needs to fail by throwing an exception. Pushing to a repository that already exits and would not
     * be updated via push is never a failure. This is compatible with the `data push` trainCommand.
     *
     * @param host The host represented as [String] that the pull should target
     * @param port The port that the push should target, represented as [Int]
     * @param repo The [DockerRepositoryName] that should be pushed to.
     * @param tag The [DockerTag] that should be pushed to.
     *
     */
    fun push(repo: DockerRepositoryName, tag: DockerTag, host: String? = null)

    /**
     * Commits the Docker container and creates new image.
     *
     * Resembles the `data commit` command.
     *
     * *Contract:* If the container selected via the [DockerContainerId] parameter does not exit, the method
     * should throw an [NoSuchDockerContainerException]. Otherwise, if anything else the prevents the target repo
     * to be created, the method should also throw an exception.
     *
     * @param containerId The [DockerContainerId] of the container to be commited.
     * @param targetRepo The [DockerRepositoryName] of the Docker repository to commit to.
     * @param targetTag The [DockerTag] that the resulting image should be tagged with.
     * @param author
     * @return The [DockerImageId] that points to the newly created image.
     *
     */
    fun commit(
        containerId: DockerContainerId,
        targetRepo: DockerRepositoryName,
        targetTag: DockerTag,
        optionalParams: DockerCommitOptionalParameters? = null
    ): DockerImageId

    /**
     * Lists the [DockerImageId] that this [DockerRuntimeClient] has access to.
     *
     * Resembles the `data images -q` command.
     *
     * *Contract:* The method should fail by throwing an exception if something prevents listing the available
     * images.
     *
     * @return The list of [DockerImageId] that this [DockerRuntimeClient] has access to.
     *
     */
    fun images(): List<DockerImageId>

    /**
     * Tag as a repository with another repository name
     *
     * Resembles the `docker tag` command
     *
     * *Contract:* This method should fail by throwning an exception if something prevents the successful
     * tagging of the image
     *
     * @param imageId: The [DockerImageId] used as the source for tagging
     * @param targetRepo The [DockerRepositoryName] to tag to (or tag with)
     * @param targetTag The [DockerTag] to tag to (or tag with)
     * @param host The optional Host [String] that is to be used for tagging
     *
     */
    fun tag(
        imageId: DockerImageId,
        targetRepo: DockerRepositoryName,
        targetTag: DockerTag,
        host: String? = null
    )

    /**
     * Logs the Docker Client in to the remote host using the provided `username` and `password`.
     *
     * @param username The username that is used for login
     * @param password Password for login.
     * @param host The host for login. If omitted, the implementor is expected to fall back to Docker Hub.
     * @return Whether login has succeeded.
     *
     */
    fun login(username: String, password: String, host: String? = null): Boolean
}
