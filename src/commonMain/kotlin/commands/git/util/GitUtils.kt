package commands.git.util

import kotlinx.datetime.Clock
import util.NativeUtil

object GitUtils {
    fun getDefaultBranch(): String {
        val command = "git remote show origin | grep 'HEAD branch' | cut -d' ' -f5"
        return NativeUtil.executeCommand(command)
    }

    fun checkDirtyBranch(): Boolean {
        val command = "git status --porcelain=v1 2>/dev/null | wc -l"
        val res = NativeUtil.executeCommand(command)
        return res.toInt() == 0
    }

    fun currentBranch(): String {
        val command = "git rev-parse --abbrev-ref HEAD"
        return NativeUtil.executeCommand(command)
    }

    fun defaultStashName(): String {
        val currentDate = Clock.System.now()
        val date = currentDate.toString()
        return "${currentBranch()}_$date"
    }
}
