package com.walkmansit.realworld

import com.walkmansit.realworld.RwScreens.LOGIN
import com.walkmansit.realworld.RwScreens.REGISTRATION
import com.walkmansit.realworld.RwScreens.SPLASH_SCREEN

object RwDestinations {
    const val SPLASH_ROUTE = SPLASH_SCREEN
    const val REGISTRATION_ROUTE = REGISTRATION
    const val LOGIN_ROUTE = LOGIN
//    const val STATISTICS_ROUTE = STATISTICS_SCREEN
//    const val TASK_DETAIL_ROUTE = "$TASK_DETAIL_SCREEN/{$TASK_ID_ARG}"
//    const val ADD_EDIT_TASK_ROUTE = "$ADD_EDIT_TASK_SCREEN/{$TITLE_ARG}?$TASK_ID_ARG={$TASK_ID_ARG}"
}

private object RwScreens {
    const val SPLASH_SCREEN = "splash"
    const val REGISTRATION = "registration"
    const val LOGIN = "login"
}