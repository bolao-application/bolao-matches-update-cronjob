package br.dev.vieira.scheduled

import br.dev.vieira.domain.MatchStatus
import br.dev.vieira.domain.Score
import br.dev.vieira.domain.UpdateMatchRequest
import br.dev.vieira.domain.isUpToDate
import br.dev.vieira.services.AuthService
import br.dev.vieira.services.FootballDataService
import br.dev.vieira.services.MatchService
import io.micronaut.context.annotation.Property
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
    @Property(name = "user-credentials.login") private val login: String,
    @Property(name = "user-credentials.password") private val password: String,
) {
    private val logger = LoggerFactory.getLogger(UpdateMatchesScheduled::class.java)

    @Scheduled(cron = "\${feature.update-match-score.cron-expression}")
    fun updateMatches() {

        val allMatches = matchService.listMatches(status = null)
        logger.info("${allMatches.size} matches loaded")

        if (allMatches.isEmpty()) return

        val allMatchesFD = footballData.getCompetitionMatches(competitionId = 2000)
        val tokenResponse = auth.authenticate(login = login, password = password)
        val token = "${tokenResponse.type} ${tokenResponse.token}"
        var updatedMatchesCount = 0

        for (i in allMatches.indices) {
            val match = allMatches[i]

            val matchResource = allMatchesFD.matches.find { it.id == match.footballDataId } ?: continue
            val matchStatus: MatchStatus = matchResource.status?.convert() ?: continue
            val startTime: OffsetDateTime = matchResource.utcDate

            val placar: Score.AuxScore? = matchResource.mostRecentScore()
            val t1Placar: Int? = placar?.homeTeam
            val t2Placar: Int? = placar?.awayTeam

            try {
//                if (t1Placar == match.t1Score && t2Placar == match.t2Score && matchStatus == match.status && startTime == match.startTime) continue
                if (match.isUpToDate(t1Placar = t1Placar, t2Placar = t2Placar, matchStatus, startTime)) continue

                val request = UpdateMatchRequest(t1Placar, t2Placar, matchStatus, startTime)
                matchService.updateScore(matchId = match.id, request = request, authorization = token)
            } catch (ex: Exception) {
                continue
            }

            updatedMatchesCount++
            logger.info("Success updating match with id ${match.id}")
        }

        logger.info("$updatedMatchesCount matches updated")
    }

}
