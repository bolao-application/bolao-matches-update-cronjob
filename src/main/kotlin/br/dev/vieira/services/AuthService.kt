package br.dev.vieira.services

import br.dev.vieira.client.AuthClient
import br.dev.vieira.domain.CredentialsRequest
import io.micronaut.http.client.exceptions.HttpClientResponseException
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class AuthService(
    private val client: AuthClient,
) {
    private val logger = LoggerFactory.getLogger(AuthService::class.java)

    fun getToken(login: String, password: String): String = try {
        val tokenResponse = client.authenticate(CredentialsRequest(login, password))
        "${tokenResponse.type} ${tokenResponse.token}"
    } catch (ex: HttpClientResponseException) {
        logger.error("Authentication error. Status ${ex.status}. Body: ${ex.response.body()}")
        throw ex
    } catch (ex: Exception) {
        logger.error("Authentication error. ${ex.javaClass.simpleName} with message: ${ex.message}")
        throw ex
    }
}
