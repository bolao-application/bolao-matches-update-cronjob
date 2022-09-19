package br.dev.vieira.client

import br.dev.vieira.domain.Match
import br.dev.vieira.domain.UpdateScoreRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.http.client.annotation.Client

@Client("\${clients.backend.api-url}")
interface MatchClient {

    @Get("/jogos")
    fun listMatches(@QueryValue("emAndamento") inPlay: Boolean): List<Match>

    @Patch("/admin/jogos/{matchId}/placar")
    fun updateScore(
        @PathVariable matchId: Long,
        @Body request: UpdateScoreRequest,
        @Header authorization: String,
    ): HttpResponse<Unit>
}
