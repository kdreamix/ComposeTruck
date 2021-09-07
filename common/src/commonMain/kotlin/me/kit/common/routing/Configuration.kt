package me.kit.common.routing

import com.arkivanov.essenty.parcelable.Parcelable

sealed class Configuration : Parcelable {
    object Splash : Configuration()
    object Main : Configuration()
}

