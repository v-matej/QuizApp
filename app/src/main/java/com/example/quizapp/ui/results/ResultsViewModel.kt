package com.example.quizapp.ui.results

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.model.QuizResult
import com.example.quizapp.data.repository.QuizRepository
import kotlinx.coroutines.launch

class ResultsViewModel(
    private val repository: QuizRepository = QuizRepository()
) : ViewModel() {

    var results by mutableStateOf<List<QuizResult>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun loadUserResults() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                results = repository.getUserResults()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }
}
