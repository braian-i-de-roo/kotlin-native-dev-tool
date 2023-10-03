package util

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.FILE
import platform.posix.getcwd
import platform.posix.getenv
import platform.posix.pclose
import platform.posix.popen
import util.MultiPlatformUtil.PROPERTY_FILE_NAME

actual object PlatformUtil {
    @OptIn(ExperimentalForeignApi::class)
    actual fun executeCommand(command: String): CPointer<FILE>? = popen(command, "r")

    @OptIn(ExperimentalForeignApi::class)
    actual fun closeCommand(fp: CPointer<FILE>?): Int = pclose(fp)

    @OptIn(ExperimentalForeignApi::class)
    actual fun currentDir(
        valueRef: CValuesRef<ByteVar>,
        size: ULong,
    ): CPointer<ByteVar>? = getcwd(valueRef, size)

    @OptIn(ExperimentalForeignApi::class)
    actual fun configFiles(): List<String> {
        val userHome = getenv("HOME")?.toKString() ?: ""
        val homeConfig = "$userHome/.config/devTool/$PROPERTY_FILE_NAME"
        val globalConfig = "/etc/devTool/$PROPERTY_FILE_NAME"
        val pwdConfig = "${NativeUtil.currentDir()}/.config/devTool/$PROPERTY_FILE_NAME"
        val sameDirConfig = "${NativeUtil.currentDir()}/.devToolConfig"
        return listOf(sameDirConfig, pwdConfig, homeConfig, globalConfig)
    }
}
