package com.example.quizapp.data.model

import com.google.firebase.Timestamp

data class QuizResult(
    val userId: String = "",
    val score: Int = 0,
    val duration: Int = 0,
    val answers: List<UserAnswer> = emptyList(),
    val timestamp: Timestamp? = null
)
