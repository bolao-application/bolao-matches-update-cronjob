package br.dev.vieira.services

import br.dev.vieira.client.MatchClient
import br.dev.vieira.domain.Match
import br.dev.vieira.domain.MatchStatus
import br.dev.vieira.domain.UpdateMatchRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class MatchService(
    private val client: MatchClient,
) {
    private val logger = LoggerFactory.getLogger(MatchService::class.java)

    fun listMatches(status: MatchStatus?): List<Match> = try {
        client.listMatches(status)
    } catch (ex: HttpClientResponseException) {
        logger.error("Error getting matches. Status ${ex.status}. Body: ${ex.response.body()}")
        throw ex
    } catch (ex: Exception) {
        logger.error("Error getting matches. ${ex.javaClass.simpleName} with message: ${ex.message}")
        throw ex
    }

    fun updateScore(matchId: Long, request: UpdateMatchRequest, authorization: String): Unit = try {
        val response = client.updateScore(matchId, request, authorization)
        if (response.status.code >= 400)
            throw HttpClientResponseException("Error updating match", HttpResponse.status<Unit>(response.status))
        else
            Unit
    } catch (ex: HttpClientResponseException) {
        logger.error("Error updating match with id $matchId. Status ${ex.status}. Body: ${ex.response.body()}")
        throw ex
    } catch (ex: Exception) {
        logger.error("Error updating match with id $matchId. ${ex.javaClass.simpleName} with message: ${ex.message}")
        throw ex
    }
}
