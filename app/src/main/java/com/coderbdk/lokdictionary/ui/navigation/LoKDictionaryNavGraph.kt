package com.coderbdk.lokdictionary.ui.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.coderbdk.lokdictionary.ui.bookmark.BookmarkRoute
import com.coderbdk.lokdictionary.ui.home.HomeRoute
import com.coderbdk.lokdictionary.ui.settings.SettingsRoute
import com.coderbdk.lokdictionary.ui.wordDetail.WordDetailRoute

@Composable
fun LoKDictionaryNavGraph(
    navController: NavHostController,
    navActions: LoKDictionaryNavActions,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home,
        modifier = modifier,
        enterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
        exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() },
    ) {
        composable<Screen.Home> {
            HomeRoute(onNavigateToWordDetail = navActions::navigateToWordDetail)
        }
        composable<Screen.Bookmark> {
            BookmarkRoute()
        }
        composable<Screen.Settings> {
            SettingsRoute()
        }
        composable<Screen.WordDetail> {
            WordDetailRoute()
        }
    }
}