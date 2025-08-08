package com.coderbdk.lokdictionary.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.NavigateBefore
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Bookmarks
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.coderbdk.lokdictionary.R
import com.coderbdk.lokdictionary.ui.navigation.LoKDictionaryNavActions
import com.coderbdk.lokdictionary.ui.navigation.LoKDictionaryNavGraph
import com.coderbdk.lokdictionary.ui.navigation.Screen
import com.coderbdk.lokdictionary.ui.theme.LoKDictionaryTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LoKDictionaryApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoKDictionaryApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val navActions = LoKDictionaryNavActions(navController)
    val viewModel = hiltViewModel<MainViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val bottomNavItems = remember {
        listOf(
            BottomNavItem(
                Screen.Bookmark,
                R.string.app_bottom_menu_bookmark,
                Icons.Filled.Bookmarks,
                Icons.Outlined.Bookmarks,
            ),
            BottomNavItem(
                Screen.Home,
                R.string.app_bottom_menu_home,
                Icons.Filled.Home,
                Icons.Outlined.Home
            ),

            BottomNavItem(
                Screen.Settings,
                R.string.app_bottom_menu_settings,
                Icons.Filled.Settings,
                Icons.Outlined.Settings
            ),
        )
    }
    LoKDictionaryTheme(
        darkTheme = uiState.isDarkTheme
    ) {
        Surface {
            Scaffold(
                topBar = {
                    if (currentDestination?.hasRoute(Screen.WordDetail::class) == true) {
                        TopAppBar(
                            title = {
                                Text("Word Detail")
                            },
                            navigationIcon = {
                                IconButton(
                                    onClick = {
                                        navController.navigateUp()
                                    }
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.NavigateBefore, "navigate_up")
                                }
                            }
                        )
                    }
                },
                bottomBar = {
                    AnimatedVisibility(
                        visible = currentDestination?.hasRoute(Screen.WordDetail::class) != true,
                        enter = slideInVertically(
                            initialOffsetY = {
                                it
                            }
                        ), exit = slideOutVertically(
                            targetOffsetY = {
                                it
                            }
                        )
                    ) {
                        LoKDictionaryBottomNavigation(
                            currentDestination,
                            bottomNavItems,
                            onNavigate = {
                                navController.navigate(it.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            })
                    }
                },
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->
                LoKDictionaryNavGraph(
                    navController = navController,
                    navActions = navActions,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
private fun LoKDictionaryBottomNavigation(
    currentDestination: NavDestination?,
    bottomNavItems: List<BottomNavItem<out Screen>>,
    onNavigate: (BottomNavItem<out Screen>) -> Unit
) {
    NavigationBar(
        containerColor = Color.Transparent
    ) {
        bottomNavItems.forEach { item ->
            val isSelected =
                currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
            NavigationBarItem(
                icon = {
                    Icon(
                        if (isSelected) item.iconFilled else item.iconOutlined,
                        contentDescription = "menu_icon",
                        modifier = Modifier.size(if (isSelected) 24.dp else 22.dp),
                    )
                },
                label = { Text(stringResource(item.labelResId)) },
                selected = isSelected,
                onClick = {
                    onNavigate(item)
                },
            )
        }
    }
}

private data class BottomNavItem<T : Any>(
    val route: T,
    @param:StringRes
    val labelResId: Int,
    val iconFilled: ImageVector,
    val iconOutlined: ImageVector
)

@Preview(showBackground = true)
@Composable
fun LoKDictionaryAppPreview() {
    LoKDictionaryTheme {
        LoKDictionaryApp()
    }
}