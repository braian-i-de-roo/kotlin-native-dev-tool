package commands.git

import com.github.ajalt.clikt.core.CliktCommand
import util.NativeUtil

class Push : CliktCommand(
    help = "Push the current branch to the remote, creating a new branch if necessary",
) {
    override fun run() {
        NativeUtil.executeCommand("git push -u origin HEAD")
    }
}
