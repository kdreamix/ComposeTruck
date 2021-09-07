import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.lifecycle.LifecycleController
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import me.kit.common.bloc.RootComponent
import me.kit.common.bloc.RootUi
import me.kit.common.module.commonModule
import me.kit.common.ui.*
import me.kit.commonDomain.TruckResult
import me.kit.commonDomain.network.responses.TruckLocation
import me.kit.commonDomain.network.responses.TruckRoute
import me.kit.commonDomain.repo.TruckRepo
import org.jxmapviewer.JXMapViewer
import org.kodein.di.compose.rememberInstance
import org.kodein.di.compose.withDI
import javax.swing.BoxLayout
import javax.swing.JPanel

@ExperimentalAnimationApi
@OptIn(ExperimentalComposeUiApi::class)
fun main() = application {
    withDI(commonModule) {
        Napier.base(DebugAntilog())

        val lifecycle = LifecycleRegistry()
        val windowState = rememberWindowState()
        val truckRepo by rememberInstance<TruckRepo>()

        val root by rememberInstance<RootComponent>()

        LifecycleController(lifecycle, windowState)

        Window(
            onCloseRequest = ::exitApplication,
            title = "Truck App",
            state = windowState
        ) {
            RootUi(root)
        }
    }
}



