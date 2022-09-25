package br.dev.vieira.client

import br.dev.vieira.domain.Match
import br.dev.vieira.domain.MatchStatus
import br.dev.vieira.domain.UpdateMatchRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${clients.backend.api-url}")
interface MatchClient {

    @Get("/jogos")
    fun listMatches(@QueryValue("status") status: MatchStatus?): List<Match>

    @Patch("/admin/jogos/{matchId}")
    fun updateScore(
        @PathVariable matchId: Long,
        @Body request: UpdateMatchRequest,
        @Header authorization: String,
    ): HttpResponse<Unit>
}
