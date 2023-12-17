package by.bsuir.myapplication.navigation

import androidx.compose.material.ScaffoldState
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import by.bsuir.myapplication.HomeViewModel
import by.bsuir.myapplication.screens.AboutScreen
import by.bsuir.myapplication.screens.AddScreen
import by.bsuir.myapplication.screens.EditScreen
import by.bsuir.myapplication.screens.HomeScreen
import by.bsuir.myapplication.screens.WeatherScreen
import by.bsuir.myapplication.screens.WowScreen
import kotlinx.coroutines.CoroutineScope

@Composable
fun Navigation(navController: NavController, scaffoldState: ScaffoldState, coroutineScope: CoroutineScope/*, viewModel: HomeViewModel*/){
    NavHost(
        navController = navController as NavHostController,
        startDestination = Screen.MainScreen.route
    ) {
        composable(route = Screen.MainScreen.route) {
            HomeScreen(navController = navController/*, viewModel*/)
        }
        composable(
            route = Screen.WowScreen.route + "/{name}", arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "Vova"
                    nullable = true
                })
        ) { entry ->
            WowScreen(name = entry.arguments?.getString("name"))
        }
        composable(route = Screen.About.route) {
            AboutScreen()
        }
        composable(route = Screen.WeatherScreen.route) {
            WeatherScreen(navController = navController)
        }
        composable(route = Screen.AddScreen.route) {
            AddScreen(navController = navController, scaffoldState = scaffoldState, coroutineScope = coroutineScope)
        }
        composable(
            route = Screen.EditScreen.route + "/{name}", arguments = listOf(
                navArgument("name") {
                    type = NavType.StringType
                    defaultValue = "-1"
                    nullable = true
                })
        ) { entry ->
            EditScreen(id = entry.arguments?.getString("name"),navController = navController, scaffoldState = scaffoldState, coroutineScope = coroutineScope)
        }
    }
}


