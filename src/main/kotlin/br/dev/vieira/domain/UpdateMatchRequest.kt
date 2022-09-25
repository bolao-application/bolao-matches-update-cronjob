package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.OffsetDateTime

data class UpdateMatchRequest(
    @JsonProperty("time1Placar") val team1Score: Int?,
    @JsonProperty("time2Placar") val team2Score: Int?,
    @JsonProperty("status") val status: MatchStatus,
    @JsonProperty("dataHora") val startTime: OffsetDateTime,
)
