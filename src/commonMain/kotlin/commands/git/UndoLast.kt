package commands.git

import com.github.ajalt.clikt.core.CliktCommand
import util.NativeUtil

class UndoLast : CliktCommand(
    help = "Undo the last commit",
) {
    override fun run() {
        NativeUtil.executeCommand("git reset --soft HEAD~1")
    }
}
