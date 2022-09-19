package br.dev.vieira.domain

data class UpdateScoreRequest(
    val time1Placar: Int,
    val time2Placar: Int,
    val status: MatchStatus,
)
