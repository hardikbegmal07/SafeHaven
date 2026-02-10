package com.hardik.safehaven.feature_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hardik.safehaven.core.ui.state.UiState

@Composable
fun ViewItemScreen(
    viewModel: ViewItemViewModel,
    onDelete: () -> Unit,
    //itemId: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // if(item == null) return

//    LaunchedEffect(key1 = itemId) {
//        viewModel.loadItemData(itemId)
//    }

    when (val state = uiState) {

        UiState.Loading -> {
            CircularProgressIndicator(modifier = Modifier.fillMaxSize())
        }

        UiState.Empty -> {  }

        is UiState.Error -> {
            Text(state.message)
        }

        is UiState.Success -> {
            val item = state.data

            Column(
                modifier = Modifier.fillMaxSize().padding(30.dp)
            ) {
                Text(item.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Type: ${item.type}")
                Spacer(Modifier.height(8.dp))
                Text(item.content)

                Spacer(Modifier.height(24.dp))

                Button(
                    onClick = {
                        viewModel.deleteItem()
                        onDelete()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Delete")
                }

            }
        }
    }

//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(30.dp)
//    ) {
//        Text(item!!.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
//        Spacer(Modifier.height(8.dp))
//        Text("Type: ${item!!.type}")
//        Spacer(Modifier.height(8.dp))
//        Text(item!!.content)
//
//        Spacer(Modifier.height(24.dp))
//
//        Button(
//            onClick = {
//                viewModel.deleteItem()
//                onDelete()
//            },
//            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
//        ) {
//            Text("Delete")
//        }
//    }
}