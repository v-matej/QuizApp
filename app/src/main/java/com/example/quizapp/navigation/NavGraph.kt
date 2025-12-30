package com.example.quizapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.quizapp.ui.auth.AuthRoute
import com.example.quizapp.ui.auth.LoginScreen
import com.example.quizapp.ui.auth.RegisterScreen
import com.example.quizapp.ui.home.HomeScreen
import com.example.quizapp.ui.quiz.QuizScreen
import com.example.quizapp.ui.result.ResultScreen
import com.example.quizapp.ui.results.ResultsScreen
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

        // ---------------- AUTH ----------------

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

        // ---------------- HOME ----------------

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

        // ---------------- QUIZ ----------------

        composable(AuthRoute.Quiz.route) {
            QuizScreen(
                onQuizFinished = { score: Int, timeLeft: Int ->
                    navController.navigate(
                        "${AuthRoute.Result.route}/$score/$timeLeft"
                    ) {
                        popUpTo(AuthRoute.Quiz.route) { inclusive = true }
                    }
                }
            )
        }

        // ---------------- CELEBRATION RESULT ----------------

        composable(
            route = "${AuthRoute.Result.route}/{score}/{timeLeft}"
        ) { backStackEntry ->
            val score =
                backStackEntry.arguments?.getString("score")?.toInt() ?: 0
            val timeLeft =
                backStackEntry.arguments?.getString("timeLeft")?.toInt() ?: 0

            ResultScreen(
                score = score,
                timeLeft = timeLeft,
                onBackToHome = {
                    navController.navigate(AuthRoute.Home.route) {
                        popUpTo(AuthRoute.Result.route) { inclusive = true }
                    }
                }
            )
        }

        // ---------------- MY RESULTS / TOP 10 ----------------

        composable(AuthRoute.Results.route) {
            ResultsScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }

    }
}
