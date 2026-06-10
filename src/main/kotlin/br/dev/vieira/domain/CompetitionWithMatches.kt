package br.dev.vieira.domain

import br.dev.vieira.domain.ScoreDurationFD.EXTRA_TIME
import br.dev.vieira.domain.ScoreDurationFD.PENALTY_SHOOTOUT
import br.dev.vieira.domain.ScoreDurationFD.REGULAR
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
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
    val group: GroupFD?,
    val homeTeam: MatchTeam?,
    val awayTeam: MatchTeam?,
    val score: Score,
    val stage: StageFD,
    @field:JsonProperty("matchday") val matchDay: Int?,
) {

    fun mostRecentScore(): Score.AuxScore? {
        return score.mostRecentScore()
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class MatchTeam(
    val id: Long?,
    val name: String?,
)


@JsonIgnoreProperties(ignoreUnknown = true)
data class Score(
    val winner: ScoreWinnerFD?,
    val duration: ScoreDurationFD,
    val fullTime: AuxScore,
    val halfTime: AuxScore,
    val regularTime: AuxScore?,
    val extraTime: AuxScore?,
    val penalties: AuxScore?,
) {
    data class AuxScore(
        val home: Int?,
        val away: Int?,
    ) {
        fun isValid(): Boolean = home != null && away != null
    }

    fun mostRecentScore(): AuxScore? = when (duration) {
        REGULAR -> regularTime ?: fullTime.takeIf { it.isValid() } ?: halfTime.takeIf { it.isValid() }
        EXTRA_TIME, PENALTY_SHOOTOUT -> regularTime
    }
}

@Suppress("unused")
enum class MatchStatusFD {
    TIMED,
    AWARDED,
    SCHEDULED,
    IN_PLAY,
    PAUSED,
    FINISHED,
    POSTPONED,
    SUSPENDED,
    CANCELLED,
    EXTRA_TIME,
    PENALTY_SHOOTOUT;
}

enum class StageFD {
    FINAL,
    THIRD_PLACE,
    SEMI_FINALS,
    QUARTER_FINALS,
    LAST_16,
    LAST_32,
    LAST_64,
    GROUP_STAGE,
    LEAGUE_STAGE,
    PLAYOFFS,
    REGULAR_SEASON,
//    ROUND_4,
//    ROUND_3,
//    ROUND_2,
//    ROUND_1,
//    PRELIMINARY_ROUND,
//    QUALIFICATION,
//    QUALIFICATION_ROUND_1,
//    QUALIFICATION_ROUND_2,
//    QUALIFICATION_ROUND_3,
//    PLAYOFF_ROUND_1,
//    PLAYOFF_ROUND_2,
//    CLAUSURA,
//    APERTURA,
//    CHAMPIONSHIP,
//    RELEGATION,
//    RELEGATION_ROUND,
}

enum class GroupFD {
    GROUP_A,
    GROUP_B,
    GROUP_C,
    GROUP_D,
    GROUP_E,
    GROUP_F,
    GROUP_G,
    GROUP_H,
    GROUP_I,
    GROUP_J,
    GROUP_K,
    GROUP_L;
}

enum class ScoreDurationFD {
    REGULAR, EXTRA_TIME, PENALTY_SHOOTOUT
}

enum class ScoreWinnerFD {
    DRAW, AWAY_TEAM, HOME_TEAM
}
