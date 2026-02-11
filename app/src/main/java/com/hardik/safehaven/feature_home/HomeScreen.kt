package com.hardik.safehaven.feature_home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hardik.safehaven.core.ui.state.UiState

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

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
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