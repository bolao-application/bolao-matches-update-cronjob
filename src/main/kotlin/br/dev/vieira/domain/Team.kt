package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

@JsonIgnoreProperties(ignoreUnknown = true)
data class Team(
    @JsonProperty("id") val id: Long,
    @JsonProperty("nome")val name: String,
    @JsonProperty("urlBandeira")val logoUrl: URI?,
)
