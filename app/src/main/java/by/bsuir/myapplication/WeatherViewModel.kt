package by.bsuir.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bsuir.myapplication.api.ApiService
import by.bsuir.myapplication.api.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

class WeatherViewModel(private val apiService: ApiService, private val dataSource: NotesDataSource) : ViewModel() {
    val weatherData = mutableStateOf<WeatherData?>(null)

    fun fetchWeather() {
        viewModelScope.launch {
            try {
                weatherData.value = apiService.getWeather()
            } catch (e: Exception) {
                // Обработка ошибки
                e.printStackTrace()
            }
        }
    }

    fun saveNote(id: Int, uuid: UUID) {
        viewModelScope.launch {

            try {

                    dataSource.upsert(
                        Note(
                            id = uuid,
                            goal = "None",
                            date = weatherData.value?.forecast?.forecastDay?.get(id)!!.date,
                            temp = weatherData.value?.forecast?.forecastDay?.get(id)!!.day.averageTemperatureCelsius.toString(),
                            maxwind = weatherData.value?.forecast?.forecastDay?.get(id)!!.day.maxWindKph.toString(),
                            condition = weatherData.value?.forecast?.forecastDay?.get(id)!!.day.condition.text
                        ))

            } catch (e: Exception){

            }
        }


    }
}