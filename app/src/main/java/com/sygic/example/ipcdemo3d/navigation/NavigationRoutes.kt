package com.sygic.example.ipcdemo3d.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavigationRoutes(val route: String = "") {

    @Serializable
    object Home: NavigationRoutes("home")

    @Serializable
    object Route: NavigationRoutes("route")

    @Serializable
    object Pois: NavigationRoutes("pois")

    @Serializable
    object UpdatePois: NavigationRoutes("updatePois")

    @Serializable
    object Location: NavigationRoutes("location")

    @Serializable
    object RouteInfo: NavigationRoutes("routeInfo")

    @Serializable
    object Sound: NavigationRoutes("sound")

    @Serializable
    object Search: NavigationRoutes("search")

    @Serializable
    object Itinerary: NavigationRoutes("itinerary")

    companion object {
        fun getByName(routes: String?): NavigationRoutes? {
            return when (routes) {
                Home.route -> Home
                Route.route -> Route
                Pois.route -> Pois
                UpdatePois.route -> UpdatePois
                Location.route -> Location
                RouteInfo.route -> RouteInfo
                Sound.route -> Sound
                else -> null
            }
        }
    }
}
