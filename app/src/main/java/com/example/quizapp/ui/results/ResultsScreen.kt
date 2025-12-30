package com.example.quizapp.ui.results

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsScreen(
    onBack: () -> Unit,
    viewModel: ResultsViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loadUserResults()
    }

    val results = viewModel.results
    val isLoading = viewModel.isLoading
    val error = viewModel.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Results") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Text("←")
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {

            when {
                isLoading -> {
                    CircularProgressIndicator()
                }

                error != null -> {
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                results.isEmpty() -> {
                    Text("No results yet. Play a quiz!")
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(results) { result ->
                            ResultItem(result)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResultItem(result: com.example.quizapp.data.model.QuizResult) {
    val formatter = remember {
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    }

    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "Score: ${result.score}",
                style = MaterialTheme.typography.titleMedium
            )
            Text("Duration: ${result.duration} s")
            Text(
                text = "Played: ${
                    result.timestamp?.toDate()?.let { formatter.format(it) } ?: "-"
                }",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
