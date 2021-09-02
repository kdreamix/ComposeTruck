import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.unit.dp
import androidx.compose.desktop.Window
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.kit.common.module.commonModule
import me.kit.common.ui.TruckLazyColumn
import me.kit.commonDomain.network.TruckApis
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.OSMTileFactoryInfo
import org.jxmapviewer.viewer.*
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import javax.swing.*

@ExperimentalAnimationApi
fun main() = Window {
    Napier.base(DebugAntilog())
    App()
}

var map: JXMapViewer? = null

@Composable
@ExperimentalAnimationApi
fun App() {
    val scope = rememberCoroutineScope()
    var truckListVisible by remember { mutableStateOf(true) }

    MaterialTheme {
        Scaffold(
            content = { Content(truckListVisible) },
            topBar = {
                TruckAppBar(onMenuClick = {
                    Napier.d("Menu Clicked")
                    truckListVisible = !truckListVisible
                })
            },
        )
    }
}

@Composable
@ExperimentalAnimationApi
fun Content(truckListVisible: Boolean) {
    Row {
        Drawer(truckListVisible)
        JMap()
    }
}

@Composable
fun TruckAppBar(onMenuClick: () -> Unit) {
    TopAppBar(title = { Text("Truck Map") }, navigationIcon = {
        IconButton(onClick = { onMenuClick() }) {
            Icon(Icons.Filled.Menu, null)
        }
    }, actions = {
        IconButton(onClick = {/* Do Something*/ }) {
            Icon(Icons.Filled.Share, null)
        }
        IconButton(onClick = {/* Do Something*/ }) {
            Icon(Icons.Filled.Settings, null)
        }
    })
}

@Composable
fun JMap() {
    Box {
        SwingPanel(
            background = Color.White,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            factory = {
                JPanel().apply {
                    map = createMap()
                    layout = BoxLayout(this, BoxLayout.Y_AXIS)
                    add(map)
                    setComponentZOrder(map, 0)
                }
            }
        )
    }

}

fun createMap(): JXMapViewer {
    val mapViewer = JXMapViewer()

    val info: TileFactoryInfo = OSMTileFactoryInfo()
    val tileFactory = DefaultTileFactory(info)
    mapViewer.tileFactory = tileFactory
    mapViewer.addressLocation = GeoPosition(25.0143878, 121.4798943)
    mapViewer.zoom = 3
    return mapViewer
}

fun focusOn(latitude: Double, longitude: Double) {
    map?.addressLocation = GeoPosition(latitude, longitude)
}

private fun addWayPoints(truckLocation: List<TruckLocation>) {
    val wpp = WaypointPainter<Waypoint>()
    val wpSet = mutableSetOf<Waypoint>()
    truckLocation.forEach { location ->
        val wp = Waypoint {
            location.latitude?.let { latitude ->
                location.longitude?.let { longitude ->
                    GeoPosition(latitude.toDouble(), longitude.toDouble())

                }
            }
        }
        wpSet.add(wp)
    }
    // wpSet.add(Waypoint {GeoPosition(25.0143878,121.4798943)})
    wpp.waypoints = wpSet
    map?.overlayPainter = wpp
}

@Composable
@ExperimentalAnimationApi
fun Drawer(visible: Boolean) = withDI(commonModule) {
    var truckRoute by remember { mutableStateOf<List<TruckRoute>>(emptyList()) }
    var truckLocation by remember { mutableStateOf<List<TruckLocation>>(emptyList()) }
    val state = rememberLazyListState()

    val density = LocalDensity.current

    val api by rememberInstance<TruckApis>()

    LaunchedEffect(true) {
        truckRoute = api.fetchTruckRoute()
        truckLocation = api.fetchTruckLocation()
        addWayPoints(truckLocation)
    }

    AnimatedVisibility(visible, enter = slideInHorizontally(
        initialOffsetX = { with(density) { -40.dp.roundToPx() } }
    )) {
        Box(Modifier.wrapContentWidth().fillMaxHeight()) {
            TruckLazyColumn(truckLocation,state) { location ->
                location.latitude?.let { latitude ->
                    location.longitude?.let { longitude ->
                        focusOn(latitude.toDouble(), longitude.toDouble())
                    }
                }
            }
            VerticalScrollbar(
                modifier = Modifier.align(Alignment.CenterEnd).fillMaxHeight(),
                adapter = rememberScrollbarAdapter(
                    scrollState = state
                )
            )
        }

    }

}



