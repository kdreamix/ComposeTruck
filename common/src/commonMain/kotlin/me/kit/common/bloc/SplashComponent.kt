package me.kit.common.bloc

import kotlinx.coroutines.delay

class SplashComponent {
    var onLoadingDone: (() -> Unit)? = null

    suspend fun load(){
        delay(3000L)
        onLoadingDone?.let { it() }
    }
}