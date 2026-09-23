package com.hardik.safehaven.feature_add

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.PhotoLibrary
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/*
    For Gallery, we will use Android's Phone Picker, it avoids gallery / storage permission,
    PickVisualMedia also has backward-compatible behavior through the Activity library.

    For Camera, we will use ActivityResultContracts.TakePicture() and give the camera a uri where it
    can save the full-resolution image.

    **One important point: we will keep the selected uri in the screen for now.
    Later, when we connect this to DB, we will copy / encrypt the image and persist it rather than
    storing the external Uri directly.
*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDocumentModal(
    onDismiss: () -> Unit,
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    val primaryColor = Color(0xFF0E207E)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(
            topStart = 28.dp,
            topEnd = 28.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = primaryColor.copy(alpha = 0.35f)
            )
        }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 28.dp,
                    end = 28.dp,
                    bottom = 30.dp
                )
        ) {

            Text(
                text = "Add Document",
                color = primaryColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Use Camera
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        onClick = onCameraClick
                    )
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.CameraAlt,
                    contentDescription = "Use Camera",
                    tint = primaryColor,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Use Camera",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryColor
                )
            }

            // Gallery
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember {
                            MutableInteractionSource()
                        },
                        onClick = onGalleryClick
                    )
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.PhotoLibrary,
                    contentDescription = "Choose from Gallery",
                    tint = primaryColor,
                    modifier = Modifier.size(25.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Choose from Gallery",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryColor
                )
            }
        }
    }
}