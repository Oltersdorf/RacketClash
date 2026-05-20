package dev.oltersdorf.racketclash.client

import dev.oltersdorf.racketclash.client.request.ClubRequest
import dev.oltersdorf.racketclash.client.request.PlayerRequest
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

class RacketClashClient(
    host: String
) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    val clubRequest = ClubRequest(client, host)
    val playerRequest = PlayerRequest(client, host)
}