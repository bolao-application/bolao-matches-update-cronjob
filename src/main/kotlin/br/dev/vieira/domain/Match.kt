package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Match(
    val id: Long,
    val grupo: String,
    val t1: Team,
    val t2: Team,
    var t1Placar: Int?,
    var t2Placar: Int?,
    val dataHora: OffsetDateTime,
    val footballDataId: Long,
    val status: MatchStatus,
)
