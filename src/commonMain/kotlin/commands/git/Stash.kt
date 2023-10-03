package commands.git

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import commands.git.util.GitUtils.defaultStashName
import util.NativeUtil

class Stash : CliktCommand(
    help = "Stash current branch's changes",
) {
    val checkoutBranch by option(
        "-b",
        "--checkout-branch",
        help = "Checkout the branch after creating the stash",
        envvar = "STASH_CHECKOUT_BRANCH",
    )

    val stashName by argument(
        help = "Name of the stash to create",
    )
        .defaultLazy(
            defaultForHelp = "currentBranch_currentDate",
            value = ::defaultStashName,
        )

    override fun run() {
        val commands =
            (
                listOf(
                    "git stash push -u -m $stashName",
                ) + (
                    if (checkoutBranch != null) {
                        listOf("git checkout $checkoutBranch")
                    } else {
                        emptyList()
                    }
                )
            )
                .joinToString(" && ")
        NativeUtil.executeCommand(commands)
    }
}
