package com.peter.iwanttobelieve.navigation

sealed class Routes(val id: String) {
    object Splash : Routes("splash_screen")
    object SignIn : Routes("sign_in_screen")
    object SignUp : Routes("sign_up_screen")
    object Feed : Routes("feed_screen")
    object Post : Routes("post_screen")
    object Profile : Routes("profile_screen")
    object Update : Routes("update_screen")
}