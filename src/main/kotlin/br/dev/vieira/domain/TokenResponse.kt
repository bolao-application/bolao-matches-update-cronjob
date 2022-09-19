package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class TokenResponse(
    val token: String,
    val refreshToken: String,
    val roles: List<String>,
    val tipo: String,
)
