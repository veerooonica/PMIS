package by.bsuir.myapplication.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.url


class ApiServiceImpl(
    private val client: HttpClient
) : ApiService {
    override suspend fun getWeather(): WeatherData {
        return client.get{ url(ApiRoutes.BASE_URL) }.body<WeatherData>();
    }
}