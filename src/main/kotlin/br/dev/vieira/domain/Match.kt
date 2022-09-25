package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Match(
    @JsonProperty("id") val id: Long,
    @JsonProperty("grupo") val group: String,
    @JsonProperty("t1") val team1: Team,
    @JsonProperty("t2") val team2: Team,
    @JsonProperty("t1Placar") var t1Score: Int?,
    @JsonProperty("t2Placar") var t2Score: Int?,
    @JsonProperty("dataHora") val startTime: OffsetDateTime,
    @JsonProperty("footballDataId") val footballDataId: Long,
    @JsonProperty("status") val status: MatchStatus,
)

fun Match.isUpToDate(t1Placar: Int?, t2Placar: Int?, status: MatchStatus, startTime: OffsetDateTime): Boolean =
    t1Placar == this.t1Score && t2Placar == this.t2Score && status == this.status && startTime == this.startTime
