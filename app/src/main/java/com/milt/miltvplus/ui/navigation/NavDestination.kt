package com.milt.miltvplus.ui.navigation

import com.milt.miltvplus.domain.model.PlayableItem
import kotlinx.serialization.Serializable

enum class NavigationSection(val displayName: String, val isPinProtected: Boolean = false) {
    HOME("Inicio"),
    LIVE_TV("TV en vivo"),
    MOVIES("Películas"),
    SERIES("Series"),
    SPORTS("Deportes"),
    RADIOS("Radios"),
    MUSIC("Música"),
    THEATRE("Teatro"),
    ADULT("Adultos 18+", isPinProtected = true),
    FAVORITES("Favoritos"),
    SEARCH("Buscar"),
    SETTINGS("Ajustes")
}

sealed interface NavDestination {

    @Serializable
    data class Section(val section: NavigationSection) : NavDestination

    @Serializable
    data class Details(val item: PlayableItem) : NavDestination

    @Serializable
    data class Player(val item: PlayableItem) : NavDestination

    @Serializable
    data class PinVerification(val destinationSection: NavigationSection) : NavDestination
}
