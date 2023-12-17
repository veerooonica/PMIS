package by.bsuir.myapplication.api

import com.ramcosta.composedestinations.BuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json


interface ApiService {

    suspend fun getWeather(): WeatherData

    companion object {
        fun create(): ApiService {
            return ApiServiceImpl(
                client = HttpClient(CIO) {
                    // Logging
                    install(Logging) {
                        level = LogLevel.ALL
                    }
                    // JSON
                    install(ContentNegotiation) {
                        json(json)
                    }
                    // Timeout
                    install(HttpTimeout) {
                        requestTimeoutMillis = 15000L
                        connectTimeoutMillis = 15000L
                        socketTimeoutMillis = 15000L
                    }
                }
            )
        }

        private val json = kotlinx.serialization.json.Json {
            explicitNulls = false
            ignoreUnknownKeys = true
            encodeDefaults = true
            prettyPrint = BuildConfig.DEBUG
            coerceInputValues = true
        }
    }
}