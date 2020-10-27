/*
 * This Scala source file was generated by the Gradle 'init' task.
 */
package vchat.utilities

import vchat.list.LinkedList

object StringUtils {
    def join(source: LinkedList): String = {
        JoinUtils.join(source)
    }

    def split(source: String): LinkedList = {
        SplitUtils.split(source)
    }
}
