package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TokenResponse(
    val token: String,
    val refreshToken: String,
    val type: String,
)
