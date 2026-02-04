package com.hardik.safehaven.core.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hardik.safehaven.data.repository.SecureItemRepository
import com.hardik.safehaven.feature_add.AddItemScreen
import com.hardik.safehaven.feature_add.AddItemViewModel
import com.hardik.safehaven.feature_home.HomeScreen
import com.hardik.safehaven.feature_home.HomeViewModel
import com.hardik.safehaven.feature_view.ViewItemScreen
import com.hardik.safehaven.feature_view.ViewItemViewModel

// what we did -
// I)   created screens (UI) first
// II)  Then wire navigation (co-ordinator)
// III) instead of navigation, magically created screens (separation = testing + sanity)

@Composable
fun AppNavGraph(navController: NavHostController, repository: SecureItemRepository) {

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {

        // HOME screen
        composable(Screen.Home.route) {
            val viewModel = remember {
                HomeViewModel(repository)
            }
            HomeScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate(Screen.AddItem.route) },
                onItemClick = { itemId ->
                    navController.navigate(Screen.ViewItem.createRoute(itemId)) }
            )
        }

        // ADD ITEM screen
        composable(Screen.AddItem.route) {
            val viewModel = remember {
                AddItemViewModel(repository)
            }
            AddItemScreen(
                viewModel,
                onSave = {
                    navController.popBackStack()
                }
            )
        }

        // VIEW ITEM screen
        composable(
            Screen.ViewItem.route,
            arguments = listOf(
                navArgument("itemId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val itemId = backStackEntry.arguments?.getString("itemId") ?: return@composable

            val viewModel = remember {
                ViewItemViewModel(
                    repository,
                    itemId
                )
            }

            ViewItemScreen(
                viewModel = viewModel,
                onDelete = {
                    navController.popBackStack()
                }
            )
        }

        // SETTINGS screen
        composable(Screen.Settings.route) {
            Text("Settings (Coming Soon)")
        }
    }
} // describes all the screens of our app, and how we move between them