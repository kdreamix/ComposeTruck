package jxmapviewer

import me.kit.commonDomain.network.responses.TruckLocation
import org.jxmapviewer.JXMapViewer
import org.jxmapviewer.OSMTileFactoryInfo
import org.jxmapviewer.viewer.*

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

fun JXMapViewer.addWayPoints(truckLocation: List<TruckLocation>) {
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