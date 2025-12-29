package com.example.quizapp.data.repository

import com.example.quizapp.data.model.Question
import com.example.quizapp.data.model.QuizResult
import com.example.quizapp.data.model.UserAnswer
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class QuizRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // --- QUESTIONS ---

    suspend fun getQuestions(): List<Question> {
        val snapshot = firestore.collection("questions").get().await()

        return snapshot.documents.map { doc ->
            doc.toObject(Question::class.java)!!
                .copy(id = doc.id)
        }
    }

    // --- RESULTS ---

    suspend fun saveResult(
        score: Int,
        duration: Int,
        answers: List<UserAnswer>
    ) {
        val userId = auth.currentUser?.uid ?: return

        val result = QuizResult(
            userId = userId,
            score = score,
            duration = duration,
            answers = answers
        )

        firestore.collection("results").add(result).await()
    }

    suspend fun getUserResults(): List<QuizResult> {
        val userId = auth.currentUser?.uid ?: return emptyList()

        val snapshot = firestore.collection("results")
            .whereEqualTo("userId", userId)
            .get()
            .await()

        return snapshot.toObjects(QuizResult::class.java)
    }

    suspend fun getTopResults(limit: Long = 10): List<QuizResult> {
        val snapshot = firestore.collection("results")
            .orderBy("score", Query.Direction.DESCENDING)
            .limit(limit)
            .get()
            .await()

        return snapshot.toObjects(QuizResult::class.java)
    }
}
