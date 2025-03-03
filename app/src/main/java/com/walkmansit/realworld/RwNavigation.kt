package com.walkmansit.realworld

import androidx.navigation.NavHostController
import com.walkmansit.realworld.ArticleDestinationsArgs.CAN_EDIT_ARG
import com.walkmansit.realworld.ArticleDestinationsArgs.SLUG_ARG
import com.walkmansit.realworld.FeedDestinationsArgs.USERNAME_ARG
import com.walkmansit.realworld.RwScreens.ARTICLE
import com.walkmansit.realworld.RwScreens.FEED
import com.walkmansit.realworld.RwScreens.LOGIN
import com.walkmansit.realworld.RwScreens.NEW_ARTICLE
import com.walkmansit.realworld.RwScreens.REGISTRATION
import com.walkmansit.realworld.RwScreens.SPLASH_SCREEN

object RwDestinations {
    const val SPLASH_ROUTE = SPLASH_SCREEN
    const val REGISTRATION_ROUTE = REGISTRATION
    const val LOGIN_ROUTE = LOGIN
    const val FEED_ROUTE = "$FEED$USERNAME_ARG={$USERNAME_ARG}"
    const val NEW_ARTICLE_ROUTE = NEW_ARTICLE
    const val ARTICLE_ROUTE = "$ARTICLE/{$SLUG_ARG}?$CAN_EDIT_ARG={$CAN_EDIT_ARG}"
//    const val STATISTICS_ROUTE = STATISTICS_SCREEN
//    const val TASK_DETAIL_ROUTE = "$TASK_DETAIL_SCREEN/{$TASK_ID_ARG}"
//const val ADD_EDIT_TASK_ROUTE = "$ADD_EDIT_TASK_SCREEN/{$TITLE_ARG}?$TASK_ID_ARG={$TASK_ID_ARG}"


}

object FeedDestinationsArgs {
    const val USERNAME_ARG = "username"
//    const val TOKEN_ARG = "token"
}

private object RwScreens {
    const val SPLASH_SCREEN = "splash"
    const val REGISTRATION = "registration"
    const val LOGIN = "login"
    const val FEED = "feed"
    const val NEW_ARTICLE = "new_article"
    const val ARTICLE = "article"
}

object ArticleDestinationsArgs {
    const val SLUG_ARG = "slug"
    const val CAN_EDIT_ARG = "can_edit"
}

class NavigationActions(private val navController: NavHostController) {
    fun navigateToLogin(){
        navController.navigate(RwDestinations.LOGIN_ROUTE)
    }

    fun navigateToRegistration(){
        navController.navigate(RwDestinations.REGISTRATION_ROUTE)
    }

    fun navigateToFeed(username: String){
        navController.navigate("$FEED$USERNAME_ARG={$username}")
    }

    fun navigateToArticle(slug: String, canEdit: Boolean = false){
        navController.navigate("$ARTICLE/$slug?$CAN_EDIT_ARG=$canEdit")
    }

    fun navigateToNewArticle(){
        navController.navigate(RwDestinations.NEW_ARTICLE_ROUTE)
    }

}
