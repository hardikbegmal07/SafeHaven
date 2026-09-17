package com.hardik.safehaven.core.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hardik.safehaven.core.auth.SessionManager
import com.hardik.safehaven.domain.usecase.AddItemUseCase
import com.hardik.safehaven.domain.usecase.DeleteItemUseCase
import com.hardik.safehaven.domain.usecase.GetItemByIdUseCase
import com.hardik.safehaven.domain.usecase.GetItemsUseCase
import com.hardik.safehaven.domain.usecase.ValidateItemUseCase
import com.hardik.safehaven.feature_add.AddItemScreen
import com.hardik.safehaven.feature_add.AddItemViewModel
import com.hardik.safehaven.feature_home.HomeScreen
import com.hardik.safehaven.feature_home.HomeViewModel
import com.hardik.safehaven.feature_view.ViewItemScreen
import com.hardik.safehaven.feature_view.ViewItemViewModel
import com.hardik.safehaven.feature_login.LoginViewModel
import com.hardik.safehaven.feature_login.LoginScreen
import com.hardik.safehaven.feature_login.SignUpScreen

// what we did -
// I)   created screens (UI) first
// II)  Then wire navigation (co-ordinator)
// III) instead of navigation, magically created screens (separation = testing + sanity)

@Composable
fun AppNavGraph(
    navController: NavHostController,
    startDestination: String,
    addItemUseCase: AddItemUseCase,
    getItemsUseCase: GetItemsUseCase,
    getItemByIdUseCase: GetItemByIdUseCase,
    deleteItemUseCase: DeleteItemUseCase,
    validateItemUseCase: ValidateItemUseCase,
    loginViewModel: LoginViewModel,
    sessionManager: SessionManager
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        // HOME screen
        composable(Screen.Home.route) {
            val viewModel = remember {
                HomeViewModel(getItemsUseCase)
            }
            HomeScreen(
                viewModel = viewModel,
                onAddClick = { navController.navigate(Screen.AddItem.route) },
                onItemClick = { itemId ->
                        navController.navigate(Screen.ViewItem.createRoute(itemId))
                }
            )
        }

        // ADD ITEM screen
        composable(Screen.AddItem.route) {
            val viewModel = remember {
                AddItemViewModel(addItemUseCase, validateItemUseCase)
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
                    itemId,
                    getItemByIdUseCase,
                    deleteItemUseCase
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

        // Login Screen
        composable(Screen.Login.route) {

            LoginScreen(
                loginViewModel,
                {
                    navController.navigate(Screen.SignUp.route)
                },
                {
                    val expiryTime = System.currentTimeMillis() + 1 * 60 * 1000L // 1 mins

                    sessionManager.saveLogin(expiryTime)

                    navController.navigate(Screen.Home.route) {
//                        popUpTo(Screen.Login.route) {
//                            inclusive = true
//                        }
//
//                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Screen.SignUp.route) {
            SignUpScreen(loginViewModel,
                {
                    navController.navigate(Screen.Login.route)
                })
        }
    }
} // describes all the screens of our app, and how we move between them