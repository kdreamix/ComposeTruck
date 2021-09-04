import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.ui.window.Window
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import me.kit.common.bloc.RootComponent
import me.kit.common.module.commonModule
import me.kit.common.ui.EmptyTruckList
import me.kit.common.ui.SearchView
import me.kit.common.ui.TruckLazyColumn
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.OSMTileFactoryInfo
import org.jxmapviewer.viewer.*
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import javax.swing.*

@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    Napier.base(DebugAntilog())

    val lifecycle = LifecycleRegistry()

    val windowState = rememberWindowState()

    LifecycleController(lifecycle, windowState)

    Window(
        onCloseRequest = ::exitApplication,
        title = "Truck App",
        state = windowState
    ) {
        App()
    }
}


@Composable
@ExperimentalAnimationApi
fun App() = withDI(commonModule) {
    val rootComponent by rememberInstance<RootComponent>()
    val state by rootComponent.state.subscribeAsState()
    val map: JXMapViewer by remember { mutableStateOf(createMap()) }

    LaunchedEffect(true) {
        rootComponent.startLoading()
        map.addWayPoints(state.truckLocation)
    }


    DesktopMaterialTheme {
        Scaffold(
            content = {
                Content(
                    map = map,
                    truckListVisible = state.truckListVisible,
                    searchTextState = state.searchText,
                    truckRoute = state.truckRoute,
                    truckLocation = state.truckLocation,
                    onTruckClick = { lat, long ->
                        map.focusOn(lat, long)
                    }
                )
            },
            topBar = {
                TruckAppBar(
                    onMenuClick = { rootComponent.onMenuClick() },
                    onSearchTextChange = { rootComponent.onSearch(it) },
                    searchTextState = state.searchText
                )
            },
        )
    }
}

@Composable
@ExperimentalAnimationApi
fun Content(
    map: JXMapViewer,
    truckListVisible: Boolean,
    searchTextState: TextFieldValue,
    truckRoute: List<TruckRoute>,
    truckLocation: List<TruckLocation>,
    onTruckClick: (Double, Double) -> Unit
) {
    Row {
        Drawer(truckListVisible, searchTextState, truckRoute, truckLocation, onTruckClick)
        JMap(map)
    }
}

@Composable
fun TruckAppBar(
    onMenuClick: () -> Unit,
    onSearchTextChange: (TextFieldValue) -> Unit,
    searchTextState: TextFieldValue
) {

    TopAppBar(
        title = { SearchView(searchTextState, onSearchTextChange) },
        navigationIcon = {
            IconButton(onClick = { onMenuClick() }) {
                Icon(Icons.Filled.Menu, null)
            }
        },
        actions = {
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Share, null)
            }
            IconButton(onClick = {/* Do Something*/ }) {
                Icon(Icons.Filled.Settings, null)
            }
        })
}

@Composable
fun JMap(map: JXMapViewer) {
    Box {
        SwingPanel(
            background = Color.White,
            modifier = Modifier.fillMaxWidth().fillMaxHeight(),
            factory = {
                JPanel().apply {
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

fun JXMapViewer.focusOn(latitude: Double, longitude: Double) {
    addressLocation = GeoPosition(latitude, longitude)
}

private fun JXMapViewer.addWayPoints(truckLocation: List<TruckLocation>) {
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
    overlayPainter = wpp
}

@Composable
@ExperimentalAnimationApi
fun Drawer(
    visible: Boolean,
    searchTextState: TextFieldValue,
    truckRoute: List<TruckRoute>,
    truckLocation: List<TruckLocation>,
    onTruckClick: (Double, Double) -> Unit
) {

    val state = rememberLazyListState()

    val density = LocalDensity.current

    val filteredList =
        truckLocation.filter { it.cityName?.lowercase()?.contains(searchTextState.text.lowercase()) ?: true }

    AnimatedVisibility(visible, enter = slideInHorizontally(
        initialOffsetX = { with(density) { -40.dp.roundToPx() } }
    )) {
        if (filteredList.isEmpty()) {
            EmptyTruckList()
        } else {
            Box(
                Modifier
                    .defaultMinSize(minWidth = 180.dp)
                    .wrapContentWidth()
                    .fillMaxHeight()
            ) {
                TruckLazyColumn(filteredList, state) { location ->
                    location.latitude?.let { latitude ->
                        location.longitude?.let { longitude ->
                            onTruckClick(latitude.toDouble(), longitude.toDouble())
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

}



