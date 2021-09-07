package me.kit.common.ui.expect

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.kit.common.bloc.SplashComponent
import me.kit.common.ui.TruckLoading
import org.kodein.di.compose.rememberInstance

@Composable
actual fun SplashUi() {
    val component by rememberInstance<SplashComponent>()

    LaunchedEffect(true) {
        component.load()
    }
    Box(modifier = Modifier.height(300.dp).width(300.dp)) {
        TruckLoading()
    }
}