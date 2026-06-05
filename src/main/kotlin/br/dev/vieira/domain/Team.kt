package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.net.URI

@JsonIgnoreProperties(ignoreUnknown = true)
data class Team(
    val id: Long,
    val name: String,
    val crest: URI?,
)
