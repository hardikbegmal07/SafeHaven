package com.hardik.safehaven.core.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddItem : Screen("add_item")

    object Login: Screen("login")

    object SignUp: Screen("sign_up")

    object ViewItem : Screen("view_item/{itemId}") {
        fun createRoute(itemId: String) = "view_item/$itemId"
    }
    object Settings : Screen("settings")
}

// why sealed ??
//  compiler knows all screens
//  when we add one -> IDE helps everywhere