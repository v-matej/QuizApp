package com.example.quizapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizapp.ui.auth.AuthRoute
import com.example.quizapp.ui.auth.LoginScreen
import com.example.quizapp.ui.auth.RegisterScreen
import com.example.quizapp.ui.home.HomeScreen
import com.example.quizapp.ui.quiz.QuizPlaceholder
import com.example.quizapp.ui.results.ResultsPlaceholder
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(
    navController: NavHostController
) {
    val currentUser = FirebaseAuth.getInstance().currentUser

    NavHost(
        navController = navController,
        startDestination = if (currentUser != null) {
            AuthRoute.Home.route
        } else {
            AuthRoute.Login.route
        }
    ) {

        composable(AuthRoute.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(AuthRoute.Home.route) {
                        popUpTo(AuthRoute.Login.route) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(AuthRoute.Register.route)
                }
            )
        }

        composable(AuthRoute.Register.route) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(AuthRoute.Home.route) {
                        popUpTo(AuthRoute.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable(AuthRoute.Quiz.route) {
            QuizPlaceholder()
        }

        composable(AuthRoute.Results.route) {
            ResultsPlaceholder()
        }


        composable(AuthRoute.Home.route) {
            HomeScreen(
                onStartQuiz = {
                    navController.navigate(AuthRoute.Quiz.route)
                },
                onMyResults = {
                    navController.navigate(AuthRoute.Results.route)
                },
                onLogout = {
                    navController.navigate(AuthRoute.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

    }
}
