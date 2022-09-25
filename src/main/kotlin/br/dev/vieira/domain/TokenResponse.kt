package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TokenResponse(
    @JsonProperty("token") val token: String,
    @JsonProperty("refreshToken") val refreshToken: String,
    @JsonProperty("roles") val roles: List<String>,
    @JsonProperty("tipo") val type: String,
)
