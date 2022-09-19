package br.dev.vieira.services

import br.dev.vieira.client.FootballDataOrgClient
import br.dev.vieira.domain.CompetitionWithMatches
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class FootballDataService(
    private val client: FootballDataOrgClient,
) {
    private val logger = LoggerFactory.getLogger(FootballDataService::class.java)

    fun getCompetitionMatches(competitionId: Long): CompetitionWithMatches = try {
        client.getCompetitionMatches(competitionId = competitionId)
    } catch (ex: HttpClientResponseException) {
        logger.error("Error getting competition matches with id $competitionId. Status ${ex.status}. Body: ${ex.response.body()}")
        throw ex
    } catch (ex: Exception) {
        logger.error("Error getting competition matches with id $competitionId. ${ex.javaClass.simpleName} with message: ${ex.message}")
        throw ex
    }
}
