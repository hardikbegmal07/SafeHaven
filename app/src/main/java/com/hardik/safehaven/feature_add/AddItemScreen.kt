package com.hardik.safehaven.feature_add

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hardik.safehaven.domain.model.ItemType

@Composable
fun AddItemScreen(
    viewModel: AddItemViewModel,
    onSave: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(30.dp)) {
        TextField(
            value = viewModel.title,
            onValueChange = { viewModel.title = it },
            label = { Text("Title") }
        )

        Spacer(Modifier.height(8.dp))

        TextField(
            value = viewModel.content,
            onValueChange = { viewModel.content = it },
            label = { Text("Content") }
        )

        Spacer(Modifier.height(8.dp))

        ItemTypeDropdown(
            selected = viewModel.type,
            onSelected = { viewModel.type = it }
        )

        Spacer(Modifier.height(16.dp))

        Button(onClick = {
            viewModel.saveItem()
            onSave()
        }) {
            Text("Save")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ItemTypeDropdown(
    selected: ItemType,
    onSelected: (ItemType) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

//    Box {
//        Button(onClick = { expanded = true }) {
//            Text(selected)
//        }
//
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            ItemType.entries.forEach {
//                DropdownMenuItem(
//                    text = { Text(it.name) },
//                    onClick = {
//                        onSelected(it.name)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        TextField(
            value = selected.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Item Type") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded)
            },
            modifier = Modifier.menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            ItemType.entries.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type.name) },
                    onClick = {
                        onSelected(type)
                        expanded = false
                    }
                )
            }
        }
    }
}
