package me.kit.common
import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import me.kit.common.module.commonModule
import me.kit.common.network.TruckApis
import me.kit.common.network.responses.TruckRouteResponse
import org.kodein.di.compose.instance
import org.kodein.di.compose.withDI

@Composable
fun App() = withDI(commonModule) {
    var text by remember { mutableStateOf("Hello, World!") }

    var truckRoute by remember { mutableStateOf<TruckRouteResponse>() }

    val api by instance<TruckApis>()

    LaunchedEffect(true){
        truckRoute = api.fetchTruckRoute()
    }
    MaterialTheme {
        Button(onClick = {
            text = "Hello, ${getPlatformName()}"
        }) {
            Text(text)
        }
    }
}
