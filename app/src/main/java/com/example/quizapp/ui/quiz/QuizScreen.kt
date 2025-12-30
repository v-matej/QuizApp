package com.example.quizapp.ui.quiz

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun QuizScreen(
    onQuizFinished: (score: Int, timeLeft: Int) -> Unit,
    quizViewModel: QuizViewModel = viewModel()
) {

    // Učitaj pitanja samo jednom
    LaunchedEffect(Unit) {
        quizViewModel.loadQuestions()
    }

    val questions = quizViewModel.questions
    val index = quizViewModel.currentQuestionIndex
    val timeLeft = quizViewModel.timeLeft
    val score = quizViewModel.score
    val isFinished = quizViewModel.isQuizFinished

    if (isFinished) {
        onQuizFinished(
            quizViewModel.finalScore,
            quizViewModel.finalTimeLeft
        )
        return
    }


    if (questions.isEmpty()) {
            // Loading state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return
        }

    val question = questions[index]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // --- HEADER ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Time: $timeLeft",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.bodyLarge
            )
        }

        LinearProgressIndicator(
            progress = timeLeft / 60f,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- QUESTION ---
        Text(
            text = "Question ${index + 1} of ${questions.size}",
            style = MaterialTheme.typography.labelLarge
        )

        Text(
            text = question.text,
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- ANSWERS ---
        question.answers.forEachIndexed { answerIndex, answerText ->
            Button(
                onClick = {
                    quizViewModel.answerCurrentQuestion(answerIndex)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(answerText)
            }
        }
    }
}