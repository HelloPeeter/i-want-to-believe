package com.peter.iwanttobelieve.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peter.iwanttobelieve.ui.screens.feed.FeedScreen
import com.peter.iwanttobelieve.ui.screens.post.PostScreen
import com.peter.iwanttobelieve.ui.screens.profile.ProfileScreen
import com.peter.iwanttobelieve.ui.screens.signin.SignInScreen
import com.peter.iwanttobelieve.ui.screens.signup.SignUpScreen
import com.peter.iwanttobelieve.ui.screens.splash.SplashScreen
import com.peter.iwanttobelieve.ui.screens.update.UpdateScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.id
    ) {
        composable(Routes.Splash.id) {
            SplashScreen(
                onNavigateToSignIn = {
                    navController.navigate(Routes.SignIn.id) {
                        popUpTo(Routes.Splash.id) { inclusive = true }
                    }
                },
                onNavigateToFeed = {
                    navController.navigate(Routes.Feed.id) {
                        popUpTo(Routes.Splash.id) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.SignIn.id) {
            SignInScreen(
                onSignInSuccess = {
                    navController.navigate(Routes.Feed.id) {
                        popUpTo(Routes.SignIn.id) { inclusive = true }
                    }
                },
                onNavigateToSignUp = {
                    navController.navigate(Routes.SignUp.id)
                }
            )
        }
        composable(Routes.SignUp.id) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Routes.Feed.id) {
                        popUpTo(Routes.SignIn.id) { inclusive = true }
                    }
                },
                onNavigateToSignIn = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.Feed.id) {
            FeedScreen(
                onNavigateToPost = {
                    navController.navigate(Routes.Post.id) {
                        popUpTo(Routes.Feed.id) { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.Profile.id) {
                        popUpTo(Routes.Feed.id) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Post.id) {
            PostScreen(
                onNavigateToFeed = {
                    navController.navigate(Routes.Feed.id) {
                        popUpTo(Routes.Post.id) { inclusive = true }
                    }
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.Profile.id) {
                        popUpTo(Routes.Post.id) { inclusive = true }
                    }
                }

            )
        }
        composable(Routes.Profile.id) {
            ProfileScreen(
                onNavigateToFeed = {
                    navController.navigate(Routes.Feed.id) {
                        popUpTo(Routes.Profile.id) { inclusive = true }
                    }
                },
                onNavigateToPost = {
                    navController.navigate(Routes.Post.id) {
                        popUpTo(Routes.Profile.id) { inclusive = true }
                    }
                },
                onSignOutSuccess = {
                    navController.navigate(Routes.SignIn.id) {
                        popUpTo(Routes.Profile.id) { inclusive = true }
                    }
                },
                onNavigateToUpdate = {
                    navController.navigate(Routes.Update.id) {
                        popUpTo(Routes.Profile.id) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Update.id) {
            UpdateScreen(
                onNavigateToProfile = {
                    navController.navigate(Routes.Profile.id) {
                        popUpTo(Routes.Update.id) { inclusive = true }
                    }
                }
            )
        }
    }
}