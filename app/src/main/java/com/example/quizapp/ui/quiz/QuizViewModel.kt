package com.example.quizapp.ui.quiz

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.UserAnswer
import com.example.quizapp.data.repository.QuizRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private val questionsPerQuiz = 5

class QuizViewModel(
    private val repository: QuizRepository = QuizRepository()
) : ViewModel() {

    var questions by mutableStateOf<List<Question>>(emptyList())
        private set

    var currentQuestionIndex by mutableStateOf(0)
        private set

    var score by mutableStateOf(0)
        private set

    var timeLeft by mutableStateOf(60)
        private set

    var isQuizFinished by mutableStateOf(false)
        private set

    private val userAnswers = mutableListOf<UserAnswer>()

    private var timerJob: Job? = null

    // --- INIT ---

    fun loadQuestions() {
        viewModelScope.launch {
            val allQuestions = repository.getQuestions()

            questions = allQuestions
                .shuffled()
                .take(questionsPerQuiz)

            currentQuestionIndex = 0
            score = 0
            timeLeft = 60
            isQuizFinished = false
            userAnswers.clear()

            startTimer()
        }
    }

    // --- TIMER ---

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timeLeft > 0 && !isQuizFinished) {
                delay(1_000)
                timeLeft--
            }
            if (timeLeft <= 0) {
                finishQuiz()
            }
        }
    }

    // --- ANSWER LOGIC ---

    fun answerCurrentQuestion(selectedIndex: Int) {
        if (isQuizFinished) return

        val question = questions[currentQuestionIndex]

        userAnswers.add(
            UserAnswer(
                questionId = question.id,
                selectedIndex = selectedIndex
            )
        )

        if (selectedIndex == question.correctIndex) {
            score += question.points
            timeLeft += question.timeBonus
        }

        goToNextQuestion()
    }

    private fun goToNextQuestion() {
        if (currentQuestionIndex < questions.lastIndex) {
            currentQuestionIndex++
        } else {
            finishQuiz()
        }
    }

    // --- FINISH ---

    private fun finishQuiz() {
        isQuizFinished = true
        timerJob?.cancel()

        viewModelScope.launch {
            repository.saveResult(
                score = score,
                duration = 60 - timeLeft,
                answers = userAnswers
            )
        }
    }

    // --- CLEANUP ---

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }

    val finalScore: Int
        get() = score

    val finalTimeLeft: Int
        get() = timeLeft
}
