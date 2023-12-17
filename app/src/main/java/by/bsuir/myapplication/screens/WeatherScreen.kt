package by.bsuir.myapplication.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.IconButton
import by.bsuir.myapplication.api.Day
import by.bsuir.myapplication.api.ForecastDay

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import by.bsuir.myapplication.HomeViewModel
import by.bsuir.myapplication.WeatherViewModel
import by.bsuir.myapplication.api.ApiService
import by.bsuir.myapplication.api.WeatherData
import by.bsuir.myapplication.navigation.Screen
import by.bsuir.vitaliybaranov.myapplication.R
import org.koin.androidx.compose.koinViewModel
import java.util.UUID

@Composable
fun WeatherScreen(navController: NavController) {

    val weatherViewModel: WeatherViewModel = koinViewModel<WeatherViewModel>()
    val weatherDataState = rememberUpdatedState(weatherViewModel.weatherData.value)
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather()
    }

    if (weatherDataState.value == null) {
        Image(
            painter = painterResource(id = R.drawable.clouds),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Loading",
                    fontSize = 60.sp, color = MaterialTheme.colorScheme.primary)
            }
        }
    } else {
        MaterialTheme {
            Image(
                painter = painterResource(id = R.drawable.clouds),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    elevation = 0.dp,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                                text = weatherDataState.value?.current?.lastUpdated.toString(),
                                fontSize = 20.sp, color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                modifier = Modifier.padding(top = 8.dp, end = 8.dp),
                                text = weatherDataState.value?.current?.condition?.text.toString(),
                                fontSize = 20.sp, color = MaterialTheme.colorScheme.primary
                            )

                        }
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = weatherDataState.value?.location?.region.toString(),
                            fontSize = 30.sp, color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = weatherDataState.value?.current?.temperatureCelsius.toString() + "°C",
                            fontSize = 50.sp, color = MaterialTheme.colorScheme.primary
                        )

                    }
                }
                if (weatherDataState.value?.forecast?.forecastDay?.size != 0) {
                    LazyColumn(modifier = Modifier.padding(bottom = 50.dp)) {
                        weatherDataState.value?.forecast?.let {
                            itemsIndexed(items = it.forecastDay) { index, item ->
                                Item(item = item, index = index, weatherViewModel, navController = navController)
                            }
                        }
                    }
                } else {
                    Text(
                        text = "Упс... Ошибочка",
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 15.dp),
                        fontSize = 60.sp, color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}


    @Composable
    fun Item(item: ForecastDay, index: Int, viewModel: WeatherViewModel, navController: NavController) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 3.dp), backgroundColor = MaterialTheme.colorScheme.surface,
            elevation = 0.dp,
            shape = RoundedCornerShape(5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = item.date,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        fontSize = 20.sp, color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = item.day.condition.text,
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        fontSize = 15.sp, color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "max wind: " + item.day.maxWindKph.toString() + "kph",
                        modifier = Modifier.padding(top = 8.dp, start = 8.dp),
                        fontSize = 15.sp, color = MaterialTheme.colorScheme.primary
                    )
                }
                Text(
                    text = "${item.day.minTemperatureCelsius}°C/${item.day.maxTemperatureCelsius}°C",
                    modifier = Modifier.padding(top = 8.dp, start = 8.dp, bottom = 15.dp),
                    fontSize = 30.sp, color = MaterialTheme.colorScheme.primary
                )
                IconButton(
                    onClick =
                    {
                        var uuid: UUID = UUID.randomUUID()
                        viewModel.saveNote(index, uuid)
                        navController.navigate(Screen.EditScreen.withArgs(uuid.toString()))
                    }
                )
                {
                    Image(
                        painter = painterResource(id = R.drawable.edit_icon),
                        contentDescription = "",
                        modifier = Modifier
                            .size(40.dp)
                            .padding(vertical = 6.dp, horizontal = 3.dp)
                    )
                }
            }
        }
    }

