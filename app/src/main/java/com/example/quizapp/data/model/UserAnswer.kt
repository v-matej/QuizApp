package com.example.quizapp.data.model

data class UserAnswer(
    val questionId: String = "",
    val selectedIndex: Int = -1,
    val correctIndex: Int = -1
)
