package me.kit.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kit.common.module.commonModule
import me.kit.commonDomain.network.TruckApis
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import org.kodein.di.compose.instance
import org.kodein.di.compose.withDI

@Composable
fun App() = withDI(commonModule) {
    var text by remember { mutableStateOf("Hello, World!") }

    var truckRoute by remember { mutableStateOf<List<TruckRoute>>(emptyList()) }
    var truckLocation by remember { mutableStateOf<List<TruckLocation>>(emptyList()) }

    val api by instance<TruckApis>()

    LaunchedEffect(true) {
        truckRoute = api.fetchTruckRoute()
        truckLocation = api.fetchTruckLocation()
    }

    MaterialTheme {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items(truckLocation) { where ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clickable{ },
                    elevation = 10.dp
                ) {
                    Text(text = where.toString())
                }
            }
        }
    }
}
