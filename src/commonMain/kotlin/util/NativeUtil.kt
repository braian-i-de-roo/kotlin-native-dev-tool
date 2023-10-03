package util

import com.github.ajalt.clikt.core.CliktError
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toKString
import platform.posix.FILE
import platform.posix.NULL
import platform.posix.closedir
import platform.posix.exit
import platform.posix.fclose
import platform.posix.fgets
import platform.posix.fopen
import platform.posix.opendir
import platform.posix.printf

object NativeUtil {
    @OptIn(ExperimentalForeignApi::class)
    fun executeCommand(command: String): String {
        val fp: CPointer<FILE>? = PlatformUtil.executeCommand(command)
        val buffer = ByteArray(4096)
        val returnString = StringBuilder()

        if (fp == NULL) {
            printf("Failed to run command\n")
            exit(1)
        }

        var scan = fgets(buffer.refTo(0), buffer.size, fp)
        if (scan != null) {
            while (scan != NULL) {
                returnString.append(scan!!.toKString())
                scan = fgets(buffer.refTo(0), buffer.size, fp)
            }
        }
        val returnCode = PlatformUtil.closeCommand(fp)
        if (returnCode != 0) {
            throw CliktError("Command `$command` failed")
        }
        printf("Return code: $returnCode\n")
        return returnString.trim().toString()
    }

    @OptIn(ExperimentalForeignApi::class)
    fun currentDir(): String {
        val buffer = ByteArray(4096)
        val bufferSize = buffer.size.toULong()
        val currentDir = PlatformUtil.currentDir(buffer.refTo(0), bufferSize)
        return currentDir?.toKString() ?: ""
    }

    @OptIn(ExperimentalForeignApi::class)
    fun dirExists(dir: String): Boolean {
        val dirPointer = opendir(dir) ?: return false
        closedir(dirPointer)
        return true
    }

    @OptIn(ExperimentalForeignApi::class)
    fun fileExists(file: String): Boolean {
        val filePointer = fopen(file, "r") ?: return false
        fclose(filePointer)
        return true
    }
}
