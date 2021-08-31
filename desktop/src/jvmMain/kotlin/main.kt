import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.desktop.Window
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.ui.zIndex
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.kit.common.module.commonModule
import me.kit.common.ui.TruckList
import me.kit.commonDomain.network.TruckApis
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.OSMTileFactoryInfo
import org.jxmapviewer.viewer.*
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import java.awt.GridLayout
import javax.swing.*

fun main() = Window {
    Napier.base(DebugAntilog())
    App()
}

var map: JXMapViewer? = null

@Composable
fun App() {
    val scaffoldState = rememberScaffoldState(rememberDrawerState(DrawerValue.Open))
    val scope = rememberCoroutineScope()


    MaterialTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            content = { JMap() },
            topBar = {
                TruckAppBar(onMenuClick = {
                    Napier.d("Menu Clicked")
                    scope.toggleDrawer(scaffoldState)
                })
            },
            drawerContent = { TruckList() }
        )
    }
}

fun CoroutineScope.toggleDrawer(scaffoldState: ScaffoldState) {
    launch {
        if (scaffoldState.drawerState.isClosed) scaffoldState.drawerState.open() else scaffoldState.drawerState.close()
    }
}

@Composable
fun DrawerContent() {
    TruckList()
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
    SwingPanel(
        background = Color.White,
        modifier = Modifier.fillMaxWidth().fillMaxHeight(),
        factory = {
            JPanel().apply {
                map = createMap()
                layout = BoxLayout(this, BoxLayout.Y_AXIS)
                add(map)
            }
        }
    )
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
    truckLocation.forEach {
        val wp = Waypoint {
            GeoPosition(it.latitude.toDouble(), it.longitude.toDouble())
        }
        wpSet.add(wp)
    }
    // wpSet.add(Waypoint {GeoPosition(25.0143878,121.4798943)})
    wpp.waypoints = wpSet
    map?.overlayPainter = wpp
}

@Composable
fun TruckList() = withDI(commonModule) {
    var truckRoute by remember { mutableStateOf<List<TruckRoute>>(emptyList()) }
    var truckLocation by remember { mutableStateOf<List<TruckLocation>>(emptyList()) }

    val api by rememberInstance<TruckApis>()

    LaunchedEffect(true) {
        truckRoute = api.fetchTruckRoute()
        truckLocation = api.fetchTruckLocation()
        addWayPoints(truckLocation)
    }

    Row(modifier = Modifier.fillMaxHeight()) {
        Box(Modifier.width(200.dp).fillMaxHeight().background(color = Color(0xff100c08))) {
            TruckList(truckLocation) {
                focusOn(it.latitudeDouble, it.longitudeDouble)
            }
            Spacer(
                modifier = Modifier.width(1.dp).fillMaxHeight()
                    .background(color = MaterialTheme.colors.onSurface.copy(0.25f))
            )
        }
    }

}

