package com.sygic.example.ipcdemo3d.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sygic.example.ipcdemo3d.domain.SdkHelper
import com.sygic.example.ipcdemo3d.navigation.NavigationRoutes
import com.sygic.example.ipcdemo3d.ui.SygicDemoTheme
import com.sygic.example.ipcdemo3d.ui.home.HomeScreen
import com.sygic.example.ipcdemo3d.ui.location.LocationScreen
import com.sygic.example.ipcdemo3d.ui.pois.PoisScreen
import com.sygic.example.ipcdemo3d.ui.poisupdate.PoisUpdateScreen
import com.sygic.example.ipcdemo3d.ui.route.RouteScreen
import com.sygic.example.ipcdemo3d.ui.routeinfo.RouteInfo
import com.sygic.example.ipcdemo3d.ui.routeinfo.RouteInfoScreen
import com.sygic.example.ipcdemo3d.ui.search.SearchScreen
import com.sygic.example.ipcdemo3d.ui.sound.SoundScreen
import com.sygic.sdk.remoteapi.events.ApiEvents
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (!isGranted) {
                Toast.makeText(this, "Permission denied. Can\'t continue", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    override fun onResume() {
        super.onResume()
        viewModel.refreshApplicationState()
    }

    fun handleEvent(event: Int) {
        val message = when (event) {
            ApiEvents.EVENT_ROUTE_COMPUTED -> "Route computed"
            else -> "Event: $event not handled"
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.initSdk(applicationContext)
        lifecycleScope.launch {
            SdkHelper.events.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                handleEvent(it)
            }
        }
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        setContent {
            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()
            val navigation = rememberNavController()
            val currentRoute = navigation.currentBackStackEntryAsState().value?.destination?.route

            SygicDemoTheme {
                ModalNavigationDrawer(
                    drawerContent = {
                        DrawerContent(
                            NavigationRoutes.Companion.getByName(currentRoute),
                            viewModel.isAppRunning.collectAsStateWithLifecycle(),
                            viewModel.isServiceConnected.collectAsStateWithLifecycle()
                        ) {
                            navigation.navigate(it)
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    },
                    drawerState = drawerState,
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = { Text("Sygic Demo App") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        scope.launch {
                                            if (drawerState.isClosed) {
                                                drawerState.open()
                                            } else {
                                                drawerState.close()
                                            }
                                        }
                                    }) {
                                        Icon(Icons.Default.Menu, contentDescription = "Menu")
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        Surface(
                            modifier = Modifier.Companion
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {
                            MainNavHost(
                                modifier = Modifier.Companion.padding(paddingValues),
                                navController = navigation
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun DrawerContent(
        selectedRoute: NavigationRoutes?,
        isAppRunning: State<Boolean>,
        isServiceConnected: State<Boolean>,
        changeRoute: (NavigationRoutes) -> Unit
    ) {
        val isEnabled = isAppRunning.value && isServiceConnected.value
        ModalDrawerSheet {
            NavigationDrawerItem(
                label = { Text(text = "Home") },
                selected = selectedRoute == NavigationRoutes.Home,
                onClick = { changeRoute.invoke(NavigationRoutes.Home) }
            )
            NavigationDrawerItem(
                label = { Text(text = "Route", modifier = Modifier.alpha(if (isEnabled) 1.0f else 0.3f)) },
                selected = selectedRoute == NavigationRoutes.Route,
                onClick = { if (isEnabled) changeRoute.invoke(NavigationRoutes.Route) }
            )
            NavigationDrawerItem(
                label = { Text(text = "Pois", modifier = Modifier.alpha(if (isEnabled) 1.0f else 0.3f)) },
                selected = selectedRoute == NavigationRoutes.Pois,
                onClick = { if (isEnabled) changeRoute.invoke(NavigationRoutes.Pois) }
            )
            NavigationDrawerItem(
                label = { Text(text = "Update Pois", modifier = Modifier.alpha(if (isEnabled) 1.0f else 0.3f)) },
                selected = selectedRoute == NavigationRoutes.UpdatePois,
                onClick = { if (isEnabled) changeRoute.invoke(NavigationRoutes.UpdatePois) }
            )
            NavigationDrawerItem(
                label = { Text(text = "Location", modifier = Modifier.alpha(if (isEnabled) 1.0f else 0.3f)) },
                selected = selectedRoute == NavigationRoutes.Location,
                onClick = { if (isEnabled) changeRoute.invoke(NavigationRoutes.Location) }
            )
            NavigationDrawerItem(
                label = { Text(text = "Route Info", modifier = Modifier.alpha(if (isEnabled) 1.0f else 0.3f)) },
                selected = selectedRoute == NavigationRoutes.RouteInfo,
                onClick = { if (isEnabled) changeRoute.invoke(NavigationRoutes.RouteInfo) }
            )
            NavigationDrawerItem(
                label = { Text(text = "Sound", modifier = Modifier.alpha(if (isEnabled) 1.0f else 0.3f)) },
                selected = selectedRoute == NavigationRoutes.Sound,
                onClick = { if (isEnabled) changeRoute.invoke(NavigationRoutes.Sound) }
            )
            NavigationDrawerItem(
                label = { Text(text = "Search", modifier = Modifier.alpha(if (isEnabled) 1.0f else 0.3f)) },
                selected = selectedRoute == NavigationRoutes.Search,
                onClick = { if (isEnabled) changeRoute.invoke(NavigationRoutes.Search) }
            )
        }
    }

    @Composable
    fun MainNavHost(
        modifier: Modifier = Modifier.Companion,
        navController: NavHostController,
    ) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = NavigationRoutes.Home,
        ) {
            composable<NavigationRoutes.Home> {
                HomeScreen(navController)
            }
            composable<NavigationRoutes.Route> {
                RouteScreen(navController)
            }
            composable<NavigationRoutes.Pois> {
                PoisScreen(navController)
            }
            composable<NavigationRoutes.UpdatePois> {
                PoisUpdateScreen(navController)
            }
            composable<NavigationRoutes.Location> {
                LocationScreen(navController)
            }
            composable<NavigationRoutes.RouteInfo> {
                RouteInfoScreen(navController)
            }
            composable<NavigationRoutes.Sound> {
                SoundScreen()
            }
            composable<NavigationRoutes.Search> {
                SearchScreen()
            }
        }
    }

}
