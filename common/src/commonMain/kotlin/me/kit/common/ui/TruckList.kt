package me.kit.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kit.commonDomain.network.responses.TruckLocation

@Composable
fun TruckList(truckLocation: List<TruckLocation>, onClick: (TruckLocation) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        items(truckLocation) { where ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
                    .clickable { onClick(where) },
                elevation = 10.dp
            ) {
                Text(text = where.toString())
            }
        }
    }
}