package br.dev.vieira.services

import br.dev.vieira.client.AuthClient
import br.dev.vieira.domain.CredentialsRequest
import br.dev.vieira.domain.TokenResponse
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class AuthService(
    private val client: AuthClient,
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun authenticate(login: String, password: String): TokenResponse = try {
        client.authenticate(CredentialsRequest(login, password))
    } catch (ex: HttpClientResponseException) {
        logger.error("Authentication error. Status ${ex.status}. Body: ${ex.response.body()}")
        throw ex
    } catch (ex: Exception) {
        logger.error("Authentication error. ${ex.javaClass.simpleName} with message: ${ex.message}")
        throw ex
    }
}
