package br.dev.vieira.scheduled

import br.dev.vieira.domain.*
import br.dev.vieira.services.AuthService
import br.dev.vieira.services.FootballDataService
import br.dev.vieira.services.MatchService
import io.micronaut.context.annotation.Requires
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.OffsetDateTime

@Singleton
@Requires(property = "feature.update-match-score.toggle", value = "true")
class UpdateMatchesScheduled(
    private val matchService: MatchService,
    private val auth: AuthService,
    private val footballData: FootballDataService,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(UpdateMatchesScheduled::class.java)

        private val competitionsToUpdate = listOf(
//            Competition.WORLD_CUP,
//            Competition.EURO,
            Competition.CHAMPIONS_LEAGUE,
//            Competition.PREMIER_LEAGUE,
//            Competition.BRASILEIRAO,
//            Competition.SERIE_A,
//            Competition.LA_LIGA,
//            Competition.BUNDESLIGA,
//            Competition.EREDIVISIE,
//            Competition.LIGUE_1,
        )
    }

    enum class Competition(val competitionId: Long, val seasonId: Long) {
        CHAMPIONS_LEAGUE(2001, 2454),
        BRASILEIRAO(2013, 2474),
    }

    @Scheduled(cron = "\${feature.update-match-score.cron-expression}")
    fun updateMatches() {
        logger.info("Starting scheduled task to update matches")
        competitionsToUpdate.forEach { competition ->
            runCatching {
                updateMatches(competition)
            }.onFailure { ex ->
                logger.error("Error updating matches for competition ${competition.name}. ${ex.javaClass.simpleName} with message: ${ex.message}")
            }
        }
        logger.info("Finished scheduled task to update matches")
    }

    private fun updateMatches(competition: Competition) {

        val allMatches = matchService.listMatches(
            status = null,
            competitionId = competition.competitionId,
            seasonId = competition.seasonId
        )
        logger.info("${allMatches.size} matches loaded")

        if (allMatches.isEmpty()) return

        val allMatchesFD = footballData.getCompetitionMatches(competitionId = competition.competitionId)
        val tokenResponse = auth.authenticate()
        val token = "${tokenResponse.type} ${tokenResponse.token}"
        var updatedMatchesCount = 0

        for (i in allMatches.indices) {
            val match = allMatches[i]

            val matchFD = allMatchesFD.matches.find { matchFD -> matchFD.id == match.id } ?: continue
            val matchStatus: MatchStatusFD = matchFD.status ?: continue
            val dateTime: OffsetDateTime = matchFD.utcDate
            val group: GroupFD? = matchFD.group
            val matchday: Int? = matchFD.matchDay
            val stage: StageFD = matchFD.stage

            val score: Score.AuxScore? = matchFD.mostRecentScore()
            val homeTeamScore: Int? = score?.home
            val awayTeamScore: Int? = score?.away

            try {
                val isUpToDate: Boolean = match.isUpToDate(
                    homeTeamScore = homeTeamScore,
                    awayTeamScore = awayTeamScore,
                    status = matchStatus,
                    dateTime = dateTime,
                    group = group,
                    matchDay = matchday,
                    stage = stage
                )

                if (isUpToDate) continue

                val request = UpdateMatchRequest(homeTeamScore, awayTeamScore, matchStatus, dateTime, group, stage, matchday)
                matchService.updateScore(matchId = match.id, request = request, authorization = token)
            } catch (ex: Exception) {
                logger.error("Error updating match with id ${match.id}. ${ex.javaClass.simpleName} with message: ${ex.message}")
                continue
            }

            updatedMatchesCount++
            logger.info("Success updating match with id ${match.id}")
        }

        logger.info("$updatedMatchesCount matches updated")
    }

}
