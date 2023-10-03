package commands.git

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.boolean
import commands.git.util.GitUtils.checkDirtyBranch
import commands.git.util.GitUtils.defaultStashName
import commands.git.util.GitUtils.getDefaultBranch
import util.NativeUtil

private fun findPackageManager(): String? {
    return if (NativeUtil.fileExists("pnpm-lock.yaml")) {
        "pnpm"
    } else if (NativeUtil.fileExists("yarn.lock")) {
        "yarn"
    } else if (NativeUtil.fileExists("package-lock.json")) {
        "npm"
    } else {
        null
    }
}

class Branch : CliktCommand(
    help = "Create a new branch from the default branch",
) {
    val stashName by option(
        "-n",
        "--stash-name",
        help = "Name of the stash to create if there are uncommitted changes",
        envvar = "STASH_NAME",
    )
        .defaultLazy(
            defaultForHelp = "currentBranch_currentDate",
            value = ::defaultStashName,
        )

    val installNpmDependencies by option(
        "-i",
        "--install",
        help = "Install npm dependencies after creating the branch",
        envvar = "STASH_INSTALL_NPM_DEPENDENCIES",
    )
        .boolean()
        .default(true)

    val branchName by argument(
        help = "Name of the branch to create",
    )

    override fun run() {
        if (checkDirtyBranch()) {
            println("Stashing changes to $stashName")
            NativeUtil.executeCommand("git stash push -m $stashName")
        }
        val defaultBranch = getDefaultBranch()
        val checkoutCommand = "git checkout $defaultBranch"
        val fetchCommand = "git fetch origin $defaultBranch"
        val pullCommand = "git pull"
        val checkoutNewBranchCommand = "git checkout -b $branchName"
        val commands =
            listOf(
                checkoutCommand,
                fetchCommand,
                pullCommand,
                checkoutNewBranchCommand,
            )
                .joinToString(" && ")
        NativeUtil.executeCommand(commands)

        if (installNpmDependencies) {
            val currentDir = NativeUtil.currentDir()
            if (NativeUtil.dirExists("$currentDir/node_modules")) {
                val packageManager = findPackageManager() ?: "npm"
                val installCommand = "$packageManager install"
                NativeUtil.executeCommand(installCommand)
            }
        }
    }
}
