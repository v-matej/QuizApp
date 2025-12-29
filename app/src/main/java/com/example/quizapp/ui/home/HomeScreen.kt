package com.example.quizapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onStartQuiz: () -> Unit,
    onMyResults: () -> Unit,
    onLogout: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = "Quiz App",
                style = MaterialTheme.typography.headlineLarge
            )

            Button(
                onClick = onStartQuiz,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Quiz")
            }

            OutlinedButton(
                onClick = onMyResults,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("My Results")
            }

            Spacer(modifier = Modifier.height(32.dp))

            TextButton(
                onClick = onLogout
            ) {
                Text("Logout")
            }
        }
    }
}
