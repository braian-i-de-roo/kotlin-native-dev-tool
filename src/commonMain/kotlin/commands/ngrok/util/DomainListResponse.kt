package commands.ngrok.util

import kotlinx.serialization.Serializable

@Serializable
data class Domain(
    val domain: String,
)

@Serializable
data class DomainListResponse(
    val reserved_domains: List<Domain>,
)
