package com.hardik.safehaven

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.hardik.safehaven.core.navigation.AppNavGraph
import com.hardik.safehaven.data.repository.FakeSecureItemRepository

@Composable
fun SafeHeavenApp() {

    val navController = rememberNavController() // creates navigation control
    val repository = remember {
        FakeSecureItemRepository()
    } // creates one single data source for the whole app
    // place where all the items are stores
    // Home, Add, View all use the same data
    // remember {} -> create it once, don't recreate it on every redraw
    AppNavGraph(navController = navController, repository)
    // IMP : if we will create the repository inside each screen, every screen would have its own fake data,
    // and nothing would work together ...
}