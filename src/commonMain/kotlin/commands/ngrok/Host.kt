package commands.ngrok

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.defaultLazy
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.types.restrictTo
import commands.ngrok.util.NgrokClient
import kotlinx.coroutines.runBlocking
import util.NativeUtil

class Host : CliktCommand(
    help = "Host a local server on Ngrok",
) {
    private fun defaultDomain(): String {
        val domains =
            runBlocking {
                NgrokClient.getDomains()
            }
        println(domains)
        return domains.first().domain
    }

    val domain by option(
        "-d",
        "--domain",
        help = "Domain on which to host",
        envvar = "HOST_DOMAIN",
    )
        .defaultLazy(
            defaultForHelp = "first domain in domain list",
            value = ::defaultDomain,
        )

    val port by option(
        "-p",
        "--port",
        help = "Port to host",
        envvar = "HOST_PORT",
    )
        .int()
        .restrictTo(min = 1, max = 65535)
        .default(80)

    override fun run() {
        val command = "ngrok http --domain=$domain $port"
        NativeUtil.executeCommand(command)
    }
}
