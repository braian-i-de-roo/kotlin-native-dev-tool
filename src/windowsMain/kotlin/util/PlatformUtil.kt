package util

import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValuesRef
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.FILE
import platform.posix._getcwd
import platform.posix._pclose
import platform.posix._popen
import platform.posix.getenv
import util.MultiPlatformUtil.PROPERTY_FILE_NAME

actual object PlatformUtil {
    @OptIn(ExperimentalForeignApi::class)
    actual fun executeCommand(command: String): CPointer<FILE>? = _popen(command, "r")

    @OptIn(ExperimentalForeignApi::class)
    actual fun closeCommand(fp: CPointer<FILE>?): Int = _pclose(fp)

    @OptIn(ExperimentalForeignApi::class)
    actual fun currentDir(
        valueRef: CValuesRef<ByteVar>,
        size: ULong,
    ): CPointer<ByteVar>? = _getcwd(valueRef, size.toInt())

    @OptIn(ExperimentalForeignApi::class)
    actual fun configFiles(): List<String> {
        val userHome = getenv("USERPROFILE")?.toKString() ?: ""
        val currentDir = NativeUtil.currentDir()
        val homeConfig = "$userHome/.config/devTool/$PROPERTY_FILE_NAME"
        // no global config on windows for now
        val pwdConfig = "$currentDir/.config/devTool/$PROPERTY_FILE_NAME"
        val sameDirConfig = "$currentDir/.devToolConfig"
        return listOf(sameDirConfig, pwdConfig, homeConfig)
    }
}
