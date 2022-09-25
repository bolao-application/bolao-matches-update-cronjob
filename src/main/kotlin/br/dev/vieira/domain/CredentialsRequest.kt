package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonProperty

data class CredentialsRequest(
    @JsonProperty("login")
    val email: String,
    @JsonProperty("senha")
    val password: String
)
