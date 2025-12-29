package com.example.quizapp.data.model

data class Question(
    val id: String = "",
    val text: String = "",
    val answers: List<String> = emptyList(),
    val correctIndex: Int = 0,
    val points: Int = 10,
    val timeBonus: Int = 0
)
