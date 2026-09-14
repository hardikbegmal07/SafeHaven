package com.hardik.safehaven

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import androidx.navigation.compose.rememberNavController
import com.hardik.safehaven.core.auth.BiometricHelper
import com.hardik.safehaven.core.navigation.AppNavGraph
import com.hardik.safehaven.core.navigation.Screen
import com.hardik.safehaven.data.local.SafeHavenDatabase
import com.hardik.safehaven.data.repository.SecureItemRepositoryImpl
import com.hardik.safehaven.data.security.CryptoManager
import com.hardik.safehaven.domain.usecase.AddItemUseCase
import com.hardik.safehaven.domain.usecase.DeleteItemUseCase
import com.hardik.safehaven.domain.usecase.GetItemByIdUseCase
import com.hardik.safehaven.domain.usecase.GetItemsUseCase
import com.hardik.safehaven.domain.usecase.ValidateItemUseCase
import com.hardik.safehaven.feature_auth.AuthViewModel
import com.hardik.safehaven.feature_auth.LockScreen
import com.hardik.safehaven.feature_login.LoginViewModel
import com.hardik.safehaven.feature_auth.auth.AuthState

/*
    Our APP is a factory.
    Data enters raw, gets cleaned, shaped. decided upon, and finally shown to the user.

    Room -> Entity -> Mapper -> Domain -> UseCase -> ViewModel -> UI

    Entity   : "How data is stored"
    Mapper   : "Translator between worlds"
    Domain   : "The truth of our app"
    Use-Case : “What the app can do”

    SecureItemEntity <-> SecureItem
 */


@Composable
fun SafeHeavenApp(authViewModel: AuthViewModel, loginViewModel: LoginViewModel, activity: FragmentActivity) {

    val context = LocalContext.current
    val database = remember { SafeHavenDatabase.getDatabase(context)
        /* DatabaseProvider.getDatabase(context) */
    }
    val cryptoManager = remember { CryptoManager() }
    // why remember ?? - so that DB is NOT recreated on recomposition
    val repository = remember { SecureItemRepositoryImpl(database.secureItemDao(), cryptoManager) }
    val addItemUseCase = remember { AddItemUseCase(repository) }
    val getItemsUseCase = remember { GetItemsUseCase(repository) }
    val getItemByIdUseCase = remember { GetItemByIdUseCase(repository) }
    val deleteItemUseCase = remember { DeleteItemUseCase(repository) }
    val validateItemUseCase = remember { ValidateItemUseCase() }
    val navController = rememberNavController() // creates navigation control

//    val repository = remember {
//        FakeSecureItemRepository()
//    } // creates one single data source for the whole app
    // place where all the items are stores
    // Home, Add, View all use the same data
    // remember {} -> create it once, don't recreate it on every redraw


    // adding BIOMETRIC AUTHENTICATION into our app
    //val authViewModel = remember { AuthViewModel() }
   // val activity = LocalContext.current as? FragmentActivity
    val biometricHelper = remember {
        BiometricHelper(activity)
    }
    val authState by authViewModel.authState.collectAsState()
    val authTrigger by authViewModel.authTrigger.collectAsState()

    when (authState) {
        is AuthState.Locked -> {
            LockScreen(
                authTrigger = authTrigger,
                onAuthenticated = { authViewModel.unlock() },
                onAuthenticationFailed = { authViewModel.requireLogin() },
                biometricHelper = biometricHelper
            )

        }

        is AuthState.Unlocked -> {
            AppNavGraph(
                navController = navController,
                startDestination = Screen.Home.route,
                addItemUseCase = addItemUseCase,
                getItemsUseCase = getItemsUseCase,
                getItemByIdUseCase,
                deleteItemUseCase = deleteItemUseCase,
                validateItemUseCase = validateItemUseCase,
                loginViewModel
            )
        }

        is AuthState.LoginRequired -> {
            AppNavGraph(
                navController = navController,
                startDestination = Screen.Login.route,
                addItemUseCase = addItemUseCase,
                getItemsUseCase = getItemsUseCase,
                getItemByIdUseCase = getItemByIdUseCase,
                deleteItemUseCase = deleteItemUseCase,
                validateItemUseCase = validateItemUseCase,
                loginViewModel = loginViewModel
            )
        }
    }
    // IMP : if we will create the repository inside each screen, every screen would have its own fake data,
    // and nothing would work together ...
}

// we have separated Authentication state from UI
// viewmodel controls lock state