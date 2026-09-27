package com.hardik.safehaven.core.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object AddItem : Screen("add_item")

    object EditItem : Screen("edit_item/{itemId}") {

        fun createRoute(itemId: String): String {
            return "edit_item/$itemId"
        }
    }

    object ViewItem : Screen("view_item/{itemId}") {
        fun createRoute(itemId: String) = "view_item/$itemId"
    }

    object Login: Screen("login")

    object SignUp: Screen("sign_up")

    object Settings : Screen("settings")
}

// why sealed ??
//  compiler knows all screens
//  when we add one -> IDE helps everywhere