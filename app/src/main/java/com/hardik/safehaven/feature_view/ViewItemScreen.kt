package com.hardik.safehaven.feature_view

import android.graphics.BitmapFactory
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hardik.safehaven.core.ui.state.UiState
import com.hardik.safehaven.domain.model.SecureItem

@Composable
fun ViewItemScreen(
    viewModel: ViewItemViewModel,
    onDelete: () -> Unit,
    onEdit: (SecureItem) -> Unit
    //itemId: String
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // if(item == null) return

//    LaunchedEffect(key1 = itemId) {
//        viewModel.loadItemData(itemId)
//    }

    var showDeleteDialog by remember { mutableStateOf(false) }

    when (val state = uiState) {

        UiState.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator()
            }
        }

        UiState.Empty -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text("Item not found")
            }

        }

        is UiState.Error -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Text(state.message)
            }

        }

        is UiState.Success -> {

            val item = state.data

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp, vertical = 50.dp)
            ) {
                Text("Title: " + item.title, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Content: " + item.content, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("Type: ${item.type}")

                item.imageData?.let { imageData ->

                    Spacer(Modifier.height(16.dp))

                    Image(
                        bitmap = BitmapFactory
                            .decodeByteArray(
                                imageData,
                                0,
                                imageData.size
                            )
                            .asImageBitmap(),
                        contentDescription = "Selected document",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .clip(
                                RoundedCornerShape(12.dp)
                            ),
                        contentScale = ContentScale.Crop
                    )

                }

                Spacer(Modifier.height(24.dp))

                Row {

                    Button(
                        onClick = {
                            // viewModel.deleteItem()
                            // onDelete()
                            showDeleteDialog = true
                        },
                        modifier = Modifier.width(90.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Delete")
                    }

                    Spacer(Modifier.width(20.dp))

                    Button(
                        onClick = {
                            onEdit(item)
                        },
                        modifier = Modifier.width(90.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFCBD5F0), contentColor = Color.Black)
                    ) {
                        Text("Edit")
                    }
                }

            }

            if (showDeleteDialog) {

                AlertDialog(

                    onDismissRequest = {
                        showDeleteDialog = false
                    },

                    title = {

                        Text(
                            text = "Delete Item?",
                            color = Color(0xFF0E207E),
                            fontWeight = FontWeight.Bold
                        )
                    },

                    text = {

                        Text(
                            text = "Are you sure you want to delete this item?",
                            color = Color(0xFF0E207E)
                        )
                    },

                    confirmButton = {

                        TextButton(
                            onClick = {

                                showDeleteDialog = false

                                /**
                                 * NOW actually delete.
                                 */
                                viewModel.deleteItem()

                                onDelete()
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

                                showDeleteDialog = false
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