import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.output.MordantHelpFormatter
import commands.Dev
import commands.git.Branch
import commands.git.Push
import commands.git.Stash
import commands.git.UndoLast
import commands.git.UpdateBranch
import commands.ngrok.Host
import config.PropertyFileValueSource
import util.PlatformUtil

object CommandBuilder {
    fun buildCommands(args: Array<String>) {
        val configFiles =
            PlatformUtil.configFiles().map { dir ->
                PropertyFileValueSource.from(dir)
            }
                .toTypedArray()

        Dev()
            .context {
                helpFormatter = {
                    MordantHelpFormatter(
                        it,
                        showDefaultValues = true,
                        showRequiredTag = true,
                    )
                }
                valueSources(*configFiles)
            }
            .subcommands(
                Branch(),
                Stash(),
                UpdateBranch(),
                Push(),
                UndoLast(),
                Host(),
            )
            .main(args)
    }
}
