package com.hardik.safehaven.feature_home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hardik.safehaven.core.ui.state.UiState
import com.hardik.safehaven.feature_login.HeaderTextView

// UI should reflect CURRENT STATE
// LOGIC should live OUTSIDE UI
// STATE CHANGES should be PREDICTABLE

@Composable
fun HomeScreen(
    onAddClick: () -> Unit,
    onItemClick: (String) -> Unit,
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle() // subscribe to state changes and Keep UI in sync.

    val activity = LocalContext.current as? Activity

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        showExitDialog = true
        // activity?.finishAffinity()
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = {
                showExitDialog = false
            },

            title = {
                Text(
                    text = "Exit SafeHaven?",
                    color = Color(0xFF0E207E),
                    fontWeight = FontWeight.Bold
                )
            },

            text = {
                Text(
                    text = "Are you sure you want to exit the application?",
                    color = Color(0xFF0E207E)
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                        activity?.finishAffinity()
                    },
                    modifier = Modifier.background(
                        Color(0xFFCBD5F0),
                        shape = RoundedCornerShape(5.dp)
                    )
                ) {
                    Text(
                        text = "YES",
                        color = Color(0xFF0E207E),
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        showExitDialog = false
                    },
                    modifier = Modifier.background(
                        Color(0xFFCBD5F0),
                        shape = RoundedCornerShape(5.dp)
                    )
                ) {
                    Text(
                        text = "NO",
                        color = Color(0xFF0E207E),
                        fontWeight = FontWeight.Bold
                    )
                }
            },

            containerColor = Color.White
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF0E207E),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState.state) {
                is UiState.Loading -> {
                    CircularProgressIndicator()
                }

                is UiState.Empty -> {
                    Text("No secure Items yet")
                }

                is UiState.Error -> {
                    Text(state.message)
                }

                is UiState.Success -> {

                    Column {

                        HeaderTextView("All Docs")

                        Spacer(Modifier.height(8.dp))

                        LazyColumn {
                            items(state.data, key = { it.id }) { item ->
                                SecureItemRow(
                                    item = item,
                                    onClick = { onItemClick(item.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
//        if(uiState.items.isEmpty()) {
//            Box(modifier = Modifier.fillMaxSize().padding(padding),
//                contentAlignment = Alignment.Center) {
//                Text("No Secure Items yet")
//            }
//        } else {
//            LazyColumn(modifier = Modifier.padding(padding)) {
//                items(uiState.items) { item ->
//                    SecureItemRow(item, onClick = { onItemClick(item.id) })
//                }
//            }
//        }
    }
    // why collectAsStateWithLifecycle() ?
    //  Stops collecting when screen is not visible
    //  Prevents memory leaks
    //  handles lifecycle automatically

//    LaunchedEffect(Unit) {
//        viewModel.loadItems()
//    } // LaunchedEffect tell that run this once when the screen enters composition
    // composables can recompose multiple times
    // you do not want to call loadItems(), every recompose


//    Column {
//        Text("Items: ${state.items.size}")
//        Button(onClick = onAddClick) { Text("Add Item") }
//        Button(onClick = onViewClick) { Text("View Item") }
//    }
}