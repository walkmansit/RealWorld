package com.walkmansit.realworld

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.walkmansit.realworld.ui.article.NewArticleView
import com.walkmansit.realworld.ui.feed.FeedView
import com.walkmansit.realworld.ui.login.LoginView
import com.walkmansit.realworld.ui.registration.RegistrationView

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    //coroutineScope: CoroutineScope = rememberCoroutineScope(),
    //drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = RwDestinations.SPLASH_ROUTE,
//    navActions: TodoNavigationActions = remember(navController) {
//        TodoNavigationActions(navController)
//    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(route = startDestination) {
            SplashScreen(navController)
        }

        composable(route = RwDestinations.REGISTRATION_ROUTE) {
            RegistrationView(modifier, navController)
        }

        composable(route = RwDestinations.LOGIN_ROUTE) {
            LoginView(modifier, navController)
        }

        composable(route = RwDestinations.FEED_ROUTE) {
            FeedView(modifier, navController)
        }

        composable(route = RwDestinations.NEW_ARTICLE_ROUTE) {
            NewArticleView(modifier, navController)
        }
    }
}