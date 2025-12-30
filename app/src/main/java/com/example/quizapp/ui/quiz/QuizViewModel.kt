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

    private val questionsPerQuiz = 5

    private val shuffleAnswers = true

    private var elapsedTime = 0

    private var correctStreak = 0

    private val maxTimePerQuestion = 10

    // --- INIT ---

    fun loadQuestions() {
        viewModelScope.launch {
            val allQuestions = repository.getQuestions()

            questions = allQuestions
                .shuffled()
                .take(questionsPerQuiz)
                .map { question ->
                    if (!shuffleAnswers) {
                        question
                    } else {
                        val indexed = question.answers.mapIndexed { index, answer ->
                            index to answer
                        }.shuffled()

                        question.copy(
                            answers = indexed.map { it.second },
                            correctIndex = indexed.indexOfFirst { it.first == question.correctIndex }
                        )
                    }
                }

            currentQuestionIndex = 0
            score = 0
            elapsedTime = 0
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
                elapsedTime++
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
                selectedIndex = selectedIndex,
                correctIndex = question.correctIndex
            )
        )

        if (selectedIndex == question.correctIndex) {
            correctStreak++

            val timeMultiplier =
                1f + (timeLeft.coerceAtMost(maxTimePerQuestion) / maxTimePerQuestion.toFloat())

            val streakBonus = correctStreak * 2
            val earnedPoints = (question.points * timeMultiplier).toInt() + streakBonus

            score += earnedPoints
            timeLeft += question.timeBonus
        } else {
            correctStreak = 0
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
                duration = elapsedTime,
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
