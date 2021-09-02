package me.kit.common.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmptyTruckList(){
    Box(modifier = Modifier.fillMaxHeight().width(220.dp), contentAlignment = Alignment.Center) {
        Text(text = "No items available \uD83E\uDD7A")
    }
}