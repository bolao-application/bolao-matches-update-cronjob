package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.net.URI

@JsonIgnoreProperties(ignoreUnknown = true)
data class Team(
    val id: Long,
    val nome: String,
    val urlBandeira: URI?,
)
