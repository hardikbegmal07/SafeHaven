package com.hardik.safehaven.feature_home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hardik.safehaven.domain.model.SecureItem

@Composable
fun SecureItemRow(
    item: SecureItem,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.fillMaxWidth().padding(8.dp).clickable { onClick() },
        // modifier must be passed down and applied to the actual UI element
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFDDE3F6)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.title, fontWeight = FontWeight.Bold)
            Text(item.type)
        }
    }
}