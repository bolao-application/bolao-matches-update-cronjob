package br.dev.vieira.client

import br.dev.vieira.domain.CompetitionWithMatches
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Header
import io.micronaut.http.annotation.Headers
import io.micronaut.http.annotation.PathVariable
import io.micronaut.http.client.annotation.Client


@Client("\${clients.football-data-org.api-url}")
@Headers(
    Header(name = "X-Auth-Token", value = "\${clients.football-data-org.api-key}"),
)
interface FootballDataOrgClient {
    @Get("/v2/competitions/{competitionId}/matches")
    fun getCompetitionMatches(@PathVariable competitionId: Long): CompetitionWithMatches

}

