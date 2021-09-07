package me.kit.common.ui.expect

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.SwingPanel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetbrains.subscribeAsState
import me.kit.common.addWayPoints
import me.kit.common.bloc.MainComponent
import me.kit.common.createMap
import me.kit.common.focusOn
import me.kit.common.module.commonModule
import me.kit.common.ui.*
import me.kit.commonDomain.TruckResult
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import org.jxmapviewer.JXMapViewer
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import javax.swing.BoxLayout
import javax.swing.JPanel

@Composable
@OptIn(ExperimentalAnimationApi::class)
actual fun MainUi() = withDI(commonModule) {
    val mainComponent by rememberInstance<MainComponent>()
    val state by mainComponent.state.subscribeAsState()
    val map: JXMapViewer by remember { mutableStateOf(createMap()) }

    LaunchedEffect(true) {
        mainComponent.startLoading()
        val truckLocation = state.truckLocation
        if (truckLocation is TruckResult.Success){
            map.addWayPoints(truckLocation.value)
        }
    }

    DesktopMaterialTheme {
        Scaffold (
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
                    onMenuClick = { mainComponent.onMenuClick() },
                    onSearchTextChange = { mainComponent.onSearch(it) },
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
    truckRoute: TruckResult<List<TruckRoute>>,
    truckLocation: TruckResult<List<TruckLocation>>,
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


@Composable
@ExperimentalAnimationApi
fun Drawer(
    visible: Boolean,
    searchTextState: TextFieldValue,
    truckRoute: TruckResult<List<TruckRoute>>,
    truckLocation: TruckResult<List<TruckLocation>>,
    onTruckClick: (Double, Double) -> Unit
) {

    val state = rememberLazyListState()

    val density = LocalDensity.current

    AnimatedVisibility(visible, enter = slideInHorizontally(
        initialOffsetX = { with(density) { -40.dp.roundToPx() } }
    )) {

        Box(
            Modifier
                .defaultMinSize(minWidth = 300.dp)
                .wrapContentWidth()
                .fillMaxHeight()
        ) {
            Row {
                when (truckLocation) {
                    is TruckResult.Error -> TruckError(truckLocation.message)
                    TruckResult.Loading -> TruckLoading()
                    is TruckResult.Success -> {
                        val filteredLocationList =
                            truckLocation.value.filter {
                                it.cityName?.lowercase()?.contains(searchTextState.text.lowercase()) ?: true
                            }
                        LocationLazyColumn(filteredLocationList, state) { location ->
                            location.latitude?.let { latitude ->
                                location.longitude?.let { longitude ->
                                    onTruckClick(latitude.toDouble(), longitude.toDouble())
                                }
                            }
                        }
                    }
                }
                when (truckRoute) {
                    is TruckResult.Error -> TruckError(truckRoute.message)
                    TruckResult.Loading -> TruckLoading()
                    is TruckResult.Success -> {
                        RouteLazyColumn(truckRoute.value, state) { route ->

                        }
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

