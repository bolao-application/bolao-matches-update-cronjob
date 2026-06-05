package br.dev.vieira.services

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Property
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Singleton
class GoogleIdTokenService(
    private val objectMapper: ObjectMapper,
    @Property(name = "user-credentials.google.client-id", defaultValue = "") private val clientId: String,
    @Property(name = "user-credentials.google.client-secret", defaultValue = "") private val clientSecret: String,
    @Property(name = "user-credentials.google.refresh-token", defaultValue = "") private val refreshToken: String,
) {
    private val logger = LoggerFactory.getLogger(GoogleIdTokenService::class.java)

    fun getIdToken(): String {
        logger.info("Refreshing Google ID token")

        val body = listOf(
            "grant_type" to "refresh_token",
            "client_id" to clientId,
            "client_secret" to clientSecret,
            "refresh_token" to refreshToken,
        ).joinToString("&") { (k, v) -> "$k=${URLEncoder.encode(v, Charsets.UTF_8)}" }

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://oauth2.googleapis.com/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(HttpRequest.BodyPublishers.ofString(body))
            .build()

        val response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString())
        val tokenResponse = objectMapper.readValue(response.body(), GoogleTokenRefreshResponse::class.java)

        if (tokenResponse.error != null) {
            throw RuntimeException("Google token refresh failed: ${tokenResponse.error} - ${tokenResponse.errorDescription}")
        }

        return tokenResponse.idToken
            ?: throw RuntimeException("Google token response did not contain id_token. Ensure the original OAuth flow included the 'openid' scope.")
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class GoogleTokenRefreshResponse(
        @JsonProperty("id_token") val idToken: String?,
        @JsonProperty("error") val error: String?,
        @JsonProperty("error_description") val errorDescription: String?,
    )
}
