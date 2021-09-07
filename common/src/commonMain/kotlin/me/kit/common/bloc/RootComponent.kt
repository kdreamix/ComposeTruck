package me.kit.common.bloc

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.push
import me.kit.common.routing.Configuration
import me.kit.common.ui.expect.MainUi
import me.kit.common.ui.expect.SplashUi

class RootComponent(
    componentContext: ComponentContext, // In Decompose each component has its own ComponentContext
    private val splashComponent: SplashComponent,
    private val mainComponent: MainComponent,
) : ComponentContext by componentContext {

    private val router =
        router(
            configurationClass = Configuration::class,
            initialConfiguration = { Configuration.Splash }, // Starting with List
            childFactory = ::createChild // The Router calls this function, providing the child Configuration and ComponentContext
        )

    val routerState = router.state

    private fun createChild(configuration: Configuration, context: ComponentContext): Content =
        when (configuration) {
            Configuration.Main -> main()
            Configuration.Splash -> splash()
        } // Configurations are handled exhaustively

    private fun splash(): Content {
        splashComponent.onLoadingDone = { router.push(Configuration.Main) }
        return splashComponent
            .asContent { SplashUi() }
    }

    private fun main(): Content =
        mainComponent.asContent { MainUi() }
}

@Composable
fun RootUi(rootComponent: RootComponent) {
    Children(rootComponent.routerState) { child ->
        child.instance()
    }
}


