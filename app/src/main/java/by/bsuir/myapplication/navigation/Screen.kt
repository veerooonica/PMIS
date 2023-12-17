package by.bsuir.myapplication.navigation

import by.bsuir.vitaliybaranov.myapplication.R

sealed class Screen(val route: String, val  titleResourceId: Int){
    object MainScreen: Screen("main_screen", R.string.title_main)
    object WeatherScreen: Screen("weather_screen", R.string.title_weather)
    object WowScreen: Screen("wow_screen", -1)
    object About: Screen("about_screen", R.string.title_about)
    object AddScreen: Screen("add_screen", -1)
    object EditScreen: Screen("edit_screen", -1)

    fun withArgs(vararg args: String) : String{
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }

}
