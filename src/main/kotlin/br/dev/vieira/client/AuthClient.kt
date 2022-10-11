package br.dev.vieira.client

import br.dev.vieira.domain.CredentialsRequest
import br.dev.vieira.domain.TokenResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Post
import io.micronaut.http.client.annotation.Client

@Client("\${clients.backend.base-url}")
interface AuthClient {

    @Post("/auth/login")
    fun authenticate(@Body request: CredentialsRequest): TokenResponse
}
