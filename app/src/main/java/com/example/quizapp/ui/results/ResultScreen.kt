package com.example.quizapp.ui.result

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.dp

@Composable
fun ResultScreen(
    score: Int,
    timeLeft: Int,
    onBackToHome: () -> Unit
) {
    // Simple "celebration" pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "celebration")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scaleAnim"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            Text(
                text = "🎉 Quiz Completed!",
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.scale(scale)
            )

            Text(
                text = "Your Score: $score",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Time Left: $timeLeft seconds",
                style = MaterialTheme.typography.bodyLarge
            )

            Button(
                onClick = onBackToHome,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to Home")
            }
        }
    }
}
