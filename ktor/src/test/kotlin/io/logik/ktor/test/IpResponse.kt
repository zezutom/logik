package io.logik.ktor.test

import kotlinx.serialization.Serializable

@Serializable
data class IpResponse(val ip: String)