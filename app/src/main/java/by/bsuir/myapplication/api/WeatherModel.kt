package by.bsuir.myapplication.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Location(
    val region: String
)

@Serializable
data class Current(

    @SerialName("last_updated")
    val lastUpdated: String,
    @SerialName("temp_c")
    val temperatureCelsius: Double,
    val condition: Condition,
)

@Serializable
data class Condition(
    val text: String
)

@Serializable
data class Forecast(
    @SerialName("forecastday")
    val forecastDay: List<ForecastDay>
)

@Serializable
data class ForecastDay(
    val date: String,
    @SerialName("date_epoch")
    val dateEpoch: Long,
    val day: Day,
    val astro: Astro
)

@Serializable
data class Day(
    @SerialName("maxtemp_c")
    val maxTemperatureCelsius: Double,
    @SerialName("maxtemp_f")
    val maxTemperatureFahrenheit: Double,
    @SerialName("mintemp_c")
    val minTemperatureCelsius: Double,
    @SerialName("mintemp_f")
    val minTemperatureFahrenheit: Double,
    @SerialName("avgtemp_c")
    val averageTemperatureCelsius: Double,
    @SerialName("avgtemp_f")
    val averageTemperatureFahrenheit: Double,
    @SerialName("maxwind_mph")
    val maxWindMph: Double,
    @SerialName("maxwind_kph")
    val maxWindKph: Double,
    @SerialName("totalprecip_mm")
    val totalPrecipitationMm: Double,
    @SerialName("totalprecip_in")
    val totalPrecipitationIn: Double,
    @SerialName("totalsnow_cm")
    val totalSnowCm: Double,
    @SerialName("avgvis_km")
    val averageVisibilityKm: Double,
    @SerialName("avgvis_miles")
    val averageVisibilityMiles: Double,
    @SerialName("avghumidity")
    val averageHumidity: Double,
    @SerialName("daily_will_it_rain")
    val willItRain: Int,
    @SerialName("daily_chance_of_rain")
    val chanceOfRain: Int,
    @SerialName("daily_will_it_snow")
    val willItSnow: Int,
    @SerialName("daily_chance_of_snow")
    val chanceOfSnow: Int,
    val condition: Condition
)

@Serializable
data class Astro(
    val sunrise: String,
    val sunset: String,
    val moonrise: String,
    val moonset: String,
    @SerialName("moon_phase")
    val moonPhase: String,
    @SerialName("moon_illumination")
    val moonillumination: Int,
    @SerialName("is_moon_up")
    val isMoonUp: Int,
    @SerialName("is_sun_up")
    val isSunUp: Int
)

@Serializable
data class WeatherData(
    val location: Location,
    val current: Current,
    val forecast: Forecast
)
