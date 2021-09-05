package me.kit.common.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kit.commonDomain.network.responses.TruckLocation

@Composable
fun TruckLoading() {
    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator()

    }
}

@Composable
fun TruckError(message: String) {
    Box(contentAlignment = Alignment.Center) {
        Text(text = message)

    }
}

@Composable
fun TruckLazyColumn(truckLocation: List<TruckLocation>, state: LazyListState, onClick: (TruckLocation) -> Unit) {
    if (truckLocation.isEmpty()){
        EmptyTruckList()
    } else {
        LazyColumn(
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = state
        ) {
            items(truckLocation) { where ->
                TruckCard(where) { onClick(where) }
            }
        }
    }

}

@Composable
fun TruckCard(truckLocation: TruckLocation, onClick: (TruckLocation) -> Unit) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .wrapContentHeight()
            .clickable { onClick(truckLocation) },
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "清運路線編號: ${truckLocation.lineId}")
            Text(text = "車牌號碼: ${truckLocation.car}")
            Text(text = "行政區名稱: ${truckLocation.cityName}")
            Text(text = "所在位址: ${truckLocation.location}")
            Text(text = "回報時間: ${truckLocation.time}")
        }
    }
}