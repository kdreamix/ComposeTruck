import me.kit.common.App
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposePanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.kit.common.module.commonModule
import me.kit.common.network.TruckApis
import me.kit.common.network.responses.TruckLocation
import me.kit.common.network.responses.TruckRoute
import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.OSMTileFactoryInfo
import org.jxmapviewer.viewer.*
import org.kodein.di.compose.instance
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import java.awt.GridLayout
import java.util.*
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

@Composable
fun BikeShareView() = withDI(commonModule) {
    var truckRoute by remember { mutableStateOf<List<TruckRoute>>(emptyList()) }
    var truckLocation by remember { mutableStateOf<List<TruckLocation>>(emptyList()) }

    val api by rememberInstance<TruckApis>()

    LaunchedEffect(true) {
        truckRoute = api.fetchTruckRoute()
        truckLocation = api.fetchTruckLocation()
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
        map?.invalidate()
    }

    MaterialTheme {
        Row {
            Box(Modifier.fillMaxWidth().fillMaxHeight().background(color = Color(0xff100c08))) {
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
                Spacer(
                    modifier = Modifier.width(1.dp).fillMaxHeight()
                        .background(color = MaterialTheme.colors.onSurface.copy(0.25f))
                )
            }
        }
    }
}