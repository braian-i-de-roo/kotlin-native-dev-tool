package commands.git

import com.github.ajalt.clikt.core.CliktCommand
import commands.git.util.GitUtils
import util.NativeUtil

class UpdateBranch : CliktCommand(
    help = "Update the current branch with the latest changes from the default branch",
) {
    override fun run() {
        val defaultBranch = GitUtils.getDefaultBranch()
        val updateCommand = "git fetch origin $defaultBranch:$defaultBranch"
        val mergeCommand = "git merge $defaultBranch"
        val commands = listOf(updateCommand, mergeCommand).joinToString(" && ")
        NativeUtil.executeCommand(commands)
    }
}
