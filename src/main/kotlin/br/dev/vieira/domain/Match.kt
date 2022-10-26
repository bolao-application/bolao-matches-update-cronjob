package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class Match(
    @JsonProperty("id") val id: Long,
    @JsonProperty("grupo") val group: String?,
    @JsonProperty("rodada") val matchday: Int?,
    @JsonProperty("fase") val stage: CompetitionStage,
    @JsonProperty("t1") val team1: Team,
    @JsonProperty("t2") val team2: Team,
    @JsonProperty("t1Placar") var t1Score: Int?,
    @JsonProperty("t2Placar") var t2Score: Int?,
    @JsonProperty("dataHora") val startTime: OffsetDateTime,
    @JsonProperty("status") val status: MatchStatus,
)

fun Match.isUpToDate(
    t1Score: Int?,
    t2Score: Int?,
    status: MatchStatus,
    startTime: OffsetDateTime,
    group: String?,
    matchday: Int?,
    stage: CompetitionStage?
): Boolean = t1Score == this.t1Score && t2Score == this.t2Score && status == this.status &&
        startTime == this.startTime && group == this.group && matchday == this.matchday && stage == this.stage

