package pl.gwsh.movietracker.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pl.gwsh.movietracker.ui.screens.DetailScreen
import pl.gwsh.movietracker.ui.screens.FormScreen
import pl.gwsh.movietracker.ui.screens.HomeScreen
import pl.gwsh.movietracker.ui.screens.ListScreen
import pl.gwsh.movietracker.viewmodel.MovieViewModel

object Routes {
    const val HOME = "home"
    const val LIST = "list"
    const val DETAIL = "detail"
    const val FORM = "form"
}


@Composable
fun FilmApp(vm: MovieViewModel = viewModel()) {
    val nav = rememberNavController()
    val backStack by nav.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute == Routes.HOME || currentRoute == Routes.LIST) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.HOME,
                        onClick = { nav.navigate(Routes.HOME) { launchSingleTop = true } },
                        icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                        label = { Text("Start") }
                    )
                    NavigationBarItem(
                        selected = currentRoute == Routes.LIST,
                        onClick = { nav.navigate(Routes.LIST) { launchSingleTop = true } },
                        icon = { Icon(Icons.Filled.List, contentDescription = null) },
                        label = { Text("Katalog") }
                    )
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = nav,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(padding)
        ) {
            composable(Routes.HOME) { HomeScreen(vm, nav) }
            composable(Routes.LIST) { ListScreen(vm, nav) }
            composable("${Routes.DETAIL}/{id}") { entry ->
                val id = entry.arguments?.getString("id")?.toIntOrNull() ?: 0
                DetailScreen(vm, nav, id)
            }
            composable("${Routes.FORM}/{id}") { entry ->
                val id = entry.arguments?.getString("id")?.toIntOrNull() ?: 0
                FormScreen(vm, nav, id)
            }
        }
    }
}
