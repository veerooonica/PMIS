package by.bsuir.myapplication

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import by.bsuir.myapplication.ui.theme.MyApplicationTheme
import androidx.activity.compose.*
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Scaffold
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import by.bsuir.myapplication.navigation.Navigation
import by.bsuir.myapplication.navigation.Screen
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import by.bsuir.myapplication.database.entity.appModule
import by.bsuir.myapplication.database.entity.databaseModule
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.java.KoinJavaComponent
import java.util.logging.Logger


class MainActivity : ComponentActivity() {
    private val logger : Logger by inject()
    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        //logger.debug("onCreate")
        super.onCreate(savedInstanceState)
        setContent {
           // val viewModel: HomeViewModel by viewModel()
            MyApplicationTheme {
               val bottomItems = listOf(Screen.MainScreen, Screen.WeatherScreen, Screen.About)
               val navController = rememberNavController()

               val scaffoldState: ScaffoldState = rememberScaffoldState()
               val coroutineScope: CoroutineScope = rememberCoroutineScope()

                Surface(
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Scaffold(scaffoldState = scaffoldState,
                        bottomBar = {
                            BottomNavigation(backgroundColor = MaterialTheme.colorScheme.background) {
                                val navBackStackEntry by navController.currentBackStackEntryAsState()
                                val currentDestination = navBackStackEntry?.destination

                                bottomItems.forEach { screen ->
                                    BottomNavigationItem(
                                        selected = currentDestination?.hierarchy?.any{it.route == screen.route} == true,
                                        onClick = {
                                            navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id){
                                                saveState = true
                                            }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        },
                                        label = { Text(stringResource(id = screen.titleResourceId),color = MaterialTheme.colorScheme.tertiary) },
                                        icon = {

                                        })
                                }
                            }
                        }, backgroundColor = MaterialTheme.colorScheme.surface
                    ) {
                        Navigation(navController = navController, coroutineScope = coroutineScope, scaffoldState = scaffoldState/*,viewModel = viewModel*/)
                    }
                }
            }
        }

    }
}
