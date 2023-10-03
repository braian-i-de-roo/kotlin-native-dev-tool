package util

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import platform.posix.FILE

expect object PlatformUtil {
    @OptIn(ExperimentalForeignApi::class)
    fun executeCommand(command: String): CPointer<FILE>?

    @OptIn(ExperimentalForeignApi::class)
    fun closeCommand(fp: CPointer<FILE>?): Int

    @OptIn(ExperimentalForeignApi::class)
    fun currentDir(
        valueRef: CValuesRef<ByteVar>,
        size: ULong,
    ): CPointer<ByteVar>?

    fun configFiles(): List<String>
}
