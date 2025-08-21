package com.walkmansit.realworld

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.walkmansit.realworld.ArticleDestinationsArgs.CAN_EDIT_ARG
import com.walkmansit.realworld.ArticleDestinationsArgs.SLUG_ARG
import com.walkmansit.realworld.FeedDestinationsArgs.USERNAME_ARG
import com.walkmansit.realworld.ui.article.ArticleView
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
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
//    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        composable(route = startDestination) {
            SplashScreen(
                modifier,
                navigateLogin = navActions::navigateToLogin,
                navigateRegistration = navActions::navigateToRegistration,
                navigateFeed = navActions::navigateToFeed,
            )
        }

        composable(route = RwDestinations.REGISTRATION_ROUTE) {
            RegistrationView(
                modifier,
                navigateLogin = navActions::navigateToLogin,
                navigateFeed = navActions::navigateToFeed,
                toast = navActions::toast,
            )
        }

        composable(route = RwDestinations.LOGIN_ROUTE) {
            LoginView(
                modifier,
                navigateRegistration = navActions::navigateToRegistration,
                navigateFeed = navActions::navigateToFeed,
                toast = navActions::toast
                )
        }

        composable(
            route = RwDestinations.FEED_ROUTE,
            arguments = listOf(
                navArgument(USERNAME_ARG) { type = NavType.StringType },
            )
        ) {
            FeedView(
                modifier,
                navigateArticle = navActions::navigateToArticle,
                navigateNewArticle = navActions::navigateToNewArticle,
                navigateLogin = navActions::navigateToLogin,
            )
        }

        composable(route = RwDestinations.NEW_ARTICLE_ROUTE) {
            NewArticleView(modifier, navController)
        }

        composable(
            route = RwDestinations.ARTICLE_ROUTE,
            arguments = listOf(
                navArgument(SLUG_ARG) { type = NavType.StringType },
                navArgument(CAN_EDIT_ARG) { type = NavType.BoolType },
            )
        ) {
            ArticleView(modifier, navController)
        }
    }
}