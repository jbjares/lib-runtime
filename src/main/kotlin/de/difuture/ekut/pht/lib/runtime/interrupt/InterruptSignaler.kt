package de.difuture.ekut.pht.lib.runtime.interrupt

/**
 * Handler that can be passed to runtime commands to determine whether the call was interrupted from
 * outside.
 *
 * @author Lukas Zimmermann
 * @since 0.0.1
 *
 */
interface InterruptSignaler<in A> {

    /**
     * Signals whether object has been interrupted
     *
     * @param obj The object for which the client wants to check whether it was interrupted
     *
     * @return Whether object [A] has been interrupted
     *
     */
    fun wasInterrupted(obj: A): Boolean
}
