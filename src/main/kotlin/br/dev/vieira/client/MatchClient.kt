package br.dev.vieira.client

import br.dev.vieira.domain.Match
import br.dev.vieira.domain.MatchStatusFD
import br.dev.vieira.domain.UpdateMatchRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${clients.backend.api-url}")
interface MatchClient {

    @Get("/competitions/{competitionId}/seasons/{seasonId}/matches")
    fun listMatches(@QueryValue("status") status: MatchStatusFD?, @PathVariable competitionId: Long, @PathVariable seasonId: Long): List<Match>

    @Put("/admin/matches/{matchId}")
    fun updateScore(
        @PathVariable matchId: Long,
        @Body request: UpdateMatchRequest,
        @Header authorization: String,
    ): HttpResponse<Unit>
}
