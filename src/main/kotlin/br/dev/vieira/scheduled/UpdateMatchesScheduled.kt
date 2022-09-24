package br.dev.vieira.scheduled

import br.dev.vieira.domain.MatchStatusFD
import br.dev.vieira.domain.Score
import br.dev.vieira.domain.UpdateScoreRequest
import br.dev.vieira.services.AuthService
import br.dev.vieira.services.FootballDataService
import br.dev.vieira.services.MatchService
import io.micronaut.context.annotation.Property
import io.micronaut.context.annotation.Requires
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

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
    fun updateMatchesInPlay() {

        val matchesInPlay = matchService.listMatches(status = MatchStatus.EM_ANDAMENTO)
        logger.info("${matchesInPlay.size} matches in play")

        if (matchesInPlay.isEmpty()) return

        val allMatches = footballData.getCompetitionMatches(competitionId = 2000)
        val token = auth.getToken(login = login, password = password)
        var updatedMatchesCount = 0

        for (i in matchesInPlay.indices) {
            val match = matchesInPlay[i]

            val matchResource = allMatches.matches.find { it.id == match.footballDataId } ?: continue
            val placar: Score.AuxScore? = matchResource.mostRecentScore()
            val t1Placar: Int = placar?.homeTeam ?: continue
            val t2Placar: Int = placar.awayTeam ?: continue
            val matchStatusFD: MatchStatusFD = matchResource.status ?: continue

            try {
                val request = UpdateScoreRequest(t1Placar, t2Placar, matchStatusFD.convert())
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
