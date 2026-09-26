package com.hardik.safehaven.feature_add

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.hardik.safehaven.data.security.toByteArray
import com.hardik.safehaven.domain.model.ItemType
import com.hardik.safehaven.feature_login.CommonButton
import com.hardik.safehaven.feature_login.CustomTextField
import com.hardik.safehaven.feature_login.HeaderTextView
import kotlinx.coroutines.flow.MutableStateFlow

@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel,
    onSave: () -> Unit
) {

    val context = LocalContext.current

    val error by viewModel.error.collectAsStateWithLifecycle()

    var showUploadModal by remember { mutableStateOf(false) }

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    var previewUri by remember { mutableStateOf<Uri?>(null) }

    /** When editing an existing item, show its image if available. */
    LaunchedEffect(viewModel.existingImageData) {
        viewModel.existingImageData?.let {

        }
    }

    /** GALLERY / PHOTO Picker */
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->

        if (uri != null) {

            previewUri = uri

            val imageBytes = uri.toByteArray(context)

            val mimeType = context.contentResolver.getType(uri)

            viewModel.imageSelected(
                imageBytes = imageBytes,
                mimeType = mimeType
            )
        }
    }

    /** CAMERA */
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // viewModel.imageSelected(cameraImageUri)

            cameraImageUri?.let { uri ->

                previewUri = uri

                val imageBytes = uri.toByteArray(context)

                val mimeType = context.contentResolver.getType(uri)

                viewModel.imageSelected(
                    imageBytes = imageBytes,
                    mimeType = mimeType
                )
            }
        }
        else
            cameraImageUri = null
    }

    /** Camera Permission */
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->

        if (granted) {

            val uri = createImageUri(context)

            cameraImageUri = uri

            cameraLauncher.launch(uri)

        }
    }

    /** Open Camera */
    fun openCamera() {

        val permissionGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (permissionGranted) {

            val uri = createImageUri(context)

            cameraImageUri = uri

            cameraLauncher.launch(uri)
        } else {

          cameraPermissionLauncher.launch(
              Manifest.permission.CAMERA
          )

        }

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 35.dp, bottom = 20.dp)
    ) {

        HeaderTextView("Add a Doc")

        Spacer(Modifier.height(8.dp))

        CustomTextField(
            "Title",
            viewModel.title,
            { viewModel.title = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.padding(horizontal = 28.dp)
        )

        CustomTextField(
            "Content",
            viewModel.content,
            { viewModel.content = it },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier.padding(horizontal = 28.dp)
        )

        Spacer(Modifier.height(8.dp))

        ItemTypeDropdown(
            selected = viewModel.type,
            onSelected = { viewModel.type = it }
        )

        /** Selected Image Preview */
        previewUri?.let { uri ->

            Spacer(modifier = Modifier.height(8.dp))

            AsyncImage(
                model = uri,
                contentDescription = "Selected document",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
                    .height(180.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    ),
                contentScale = ContentScale.Crop
            )

        }

        /** Upload Document */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, end = 30.dp)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() },
                    onClick = { showUploadModal = true }
                ),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Outlined.CloudUpload,
                contentDescription = "Upload a document",
                tint = Color(0xFF0E207E),
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            Text(
                text = "Upload a Doc",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0E207E)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        error?.let {
            Text(
                text = it,
                color = Color.Red,
                fontSize = 13.sp,
                modifier = Modifier.padding(
                    horizontal = 28.dp,
                    vertical = 4.dp
                )
            )
        }

        CommonButton("Save") {
            viewModel.saveItem {
                onSave()
            }
        }
    }

    if (showUploadModal) {
        UploadDocumentModal(
            onDismiss = {
                showUploadModal = false
            },
            onCameraClick = {
                showUploadModal = false

                openCamera()
            },
            onGalleryClick = {
                showUploadModal = false

                galleryLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts
                            .PickVisualMedia
                            .ImageOnly
                    )
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTypeDropdown(
    selected: ItemType,
    onSelected: (ItemType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val fieldShape = RoundedCornerShape(5.dp)
    val primaryColor = Color(0xFF0E207E)

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .background(Color.Transparent)
            .padding(top = 10.dp)
            .padding(horizontal = 28.dp)
    ) {
        TextField(
            value = selected.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Item Type", color = primaryColor) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
                .height(60.dp)
                .border(
                    width = 1.dp,
                    color = primaryColor,
                    shape = fieldShape
                ),
            shape = fieldShape,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledContainerColor = Color.White,

                focusedTextColor = primaryColor,
                unfocusedTextColor = primaryColor,

                focusedLabelColor = primaryColor,
                unfocusedLabelColor = primaryColor,

                focusedTrailingIconColor = primaryColor,
                unfocusedTrailingIconColor = primaryColor,

                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            ItemType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name, color = primaryColor) },
                    onClick = {
                        onSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}