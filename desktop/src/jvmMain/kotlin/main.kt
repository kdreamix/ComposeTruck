import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
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
import javax.swing.JFrame
import javax.swing.SwingUtilities
import javax.swing.WindowConstants

fun main()  {
    SwingComposeWindow()
}

var map: JXMapViewer? = null

fun SwingComposeWindow() = SwingUtilities.invokeLater {
    val window = JFrame()
    window.defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
    window.title = "Truck Map"

    map = createMap()

    val composePanel = ComposePanel()
    window.layout = GridLayout(1, 2)
    window.add(composePanel)
    window.add(map)

    composePanel.setContent {
        BikeShareView()
    }

    window.setSize(900, 600)
    window.isVisible = true
}

fun createMap(): JXMapViewer {
    val mapViewer = JXMapViewer()

    val info: TileFactoryInfo = OSMTileFactoryInfo()
    val tileFactory = DefaultTileFactory(info)
    mapViewer.tileFactory = tileFactory
    mapViewer.addressLocation = GeoPosition(25.0143878,121.4798943)
    mapViewer.zoom = 3
    return mapViewer
}

fun focusOn(latitude:Double, longitude:Double){
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
fun BikeShareView() = withDI(commonModule) {
    var truckRoute by remember { mutableStateOf<List<TruckRoute>>(emptyList()) }
    var truckLocation by remember { mutableStateOf<List<TruckLocation>>(emptyList()) }

    val api by rememberInstance<TruckApis>()

    LaunchedEffect(true) {
        truckRoute = api.fetchTruckRoute()
        truckLocation = api.fetchTruckLocation()
        addWayPoints(truckLocation)
    }

    MaterialTheme {
        Row {
            Box(Modifier.fillMaxWidth().fillMaxHeight().background(color = Color(0xff100c08))) {
                TruckList(truckLocation){
                    focusOn(it.latitudeDouble,it.longitudeDouble)
                }
                Spacer(
                    modifier = Modifier.width(1.dp).fillMaxHeight()
                        .background(color = MaterialTheme.colors.onSurface.copy(0.25f))
                )
            }
        }
    }
}

