package me.kit.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kit.commonDomain.network.responses.TruckRoute

@Composable
fun RouteLazyColumn(
    truckRoute: List<TruckRoute>,
    state: LazyListState,
    onClick: (TruckRoute) -> Unit
) {
    if (truckRoute.isEmpty()){
        EmptyTruckList()
    } else {
        LazyColumn(
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = state
        ) {
            items(truckRoute) { route ->
                RouteCard(route) { onClick(route) }
            }
        }
    }
}

@Composable
fun RouteCard(truckRoute: TruckRoute, onClick: (TruckRoute) -> Unit) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .wrapContentHeight()
            .clickable { onClick(truckRoute) },
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "清運路線編號: ${truckRoute.lineId}")
        }
    }
}