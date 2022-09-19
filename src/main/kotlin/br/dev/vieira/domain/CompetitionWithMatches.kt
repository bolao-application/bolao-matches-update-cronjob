package br.dev.vieira.domain

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.time.OffsetDateTime

@JsonIgnoreProperties(ignoreUnknown = true)
data class CompetitionWithMatches(
    val matches: List<MatchFD>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MatchFD(
    val id: Long,
    val status: MatchStatusFD?,
    val utcDate: OffsetDateTime,
    val group: String?,
    val homeTeam: MatchTeam?,
    val awayTeam: MatchTeam?,
    val score: Score,
) {

    fun mostRecentScore(): Score.AuxScore? {
        return when {
            score.penalties.isValid() -> score.penalties
            score.extraTime.isValid() -> score.extraTime
            score.fullTime.isValid() -> score.fullTime
            score.halfTime.isValid() -> score.halfTime
            else -> null
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class MatchTeam(
    val id: Long?,
    val name: String?,
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class Score(
    val fullTime: AuxScore,
    val halfTime: AuxScore,
    val extraTime: AuxScore,
    val penalties: AuxScore,
) {
    data class AuxScore(
        val homeTeam: Int?,
        val awayTeam: Int?,
    ) {
        fun isValid(): Boolean = homeTeam != null && awayTeam != null
    }
}

@Suppress("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
data class MatchResource(
    val match: MatchFD,
)

@Suppress("unused")
enum class MatchStatusFD {
    SCHEDULED {
        override fun convert(): MatchStatus {
            return MatchStatus.AGENDADO
        }
    },
    LIVE {
        override fun convert(): MatchStatus {
            return MatchStatus.EM_ANDAMENTO
        }
    },
    IN_PLAY {
        override fun convert(): MatchStatus {
            return MatchStatus.EM_ANDAMENTO
        }
    },
    PAUSED {
        override fun convert(): MatchStatus {
            return MatchStatus.PAUSADO
        }
    },
    FINISHED {
        override fun convert(): MatchStatus {
            return MatchStatus.ENCERRADO
        }
    },
    POSTPONED {
        override fun convert(): MatchStatus {
            return MatchStatus.AGENDADO
        }
    },
    SUSPENDED {
        override fun convert(): MatchStatus {
            return MatchStatus.PAUSADO
        }
    },
    CANCELED {
        override fun convert(): MatchStatus {
            return MatchStatus.PAUSADO
        }
    };

    abstract fun convert(): MatchStatus
}
