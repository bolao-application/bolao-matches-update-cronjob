package br.dev.vieira.domain

import java.time.OffsetDateTime

data class UpdateMatchRequest(
    val homeTeamScore: Int?,
    val awayTeamScore: Int?,
    val status: MatchStatusFD,
    val dateTime: OffsetDateTime,
    val group: GroupFD?,
    val stage: StageFD,
    val matchDay: Int?,
)
