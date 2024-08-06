package com.example.quizapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quizapp.ui.theme.QuizAppTheme

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswer: String
)

@Composable
fun QuizScreen(
    questions: List<Question>,
    onQuizComplete: (Int) -> Unit,
    onRestart: () -> Unit
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var score by remember { mutableIntStateOf(0) }

    if (currentQuestionIndex >= questions.size) {
        onQuizComplete(score)
    } else {
        val question = questions[currentQuestionIndex]

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Question ${currentQuestionIndex + 1}: ${question.text}")
            Spacer(modifier = Modifier.height(16.dp))

            question.options.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    RadioButton(
                        selected = (option == selectedOption),
                        onClick = { selectedOption = option }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = option)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedOption == question.correctAnswer) {
                        score++
                    }
                    selectedOption = null
                    currentQuestionIndex++
                },
                enabled = selectedOption != null
            ) {
                Text(text = "Submit")
            }
        }
    }

    if (currentQuestionIndex >= questions.size) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Your score is: $score/${questions.size}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onRestart() }) {
                Text(text = "Restart")
            }
        }
    }
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuizAppTheme {
                val questions = listOf(
                    Question(
                        text = "What is 2 + 2?",
                        options = listOf("3", "4", "5", "6"),
                        correctAnswer = "4"
                    ),
                    Question(
                        text = "What is 5 * 6?",
                        options = listOf("30", "32", "28", "36"),
                        correctAnswer = "30"
                    ),
                    Question(
                        text = "What is 10 / 2?",
                        options = listOf("3", "4", "5", "6"),
                        correctAnswer = "5"
                    ),
                    Question(
                        text = "What is 9 - 3?",
                        options = listOf("5", "6", "7", "8"),
                        correctAnswer = "6"
                    ),
                    Question(
                        text = "What is 7 + 8?",
                        options = listOf("14", "15", "16", "17"),
                        correctAnswer = "15"
                    )
                )

                var score by remember { mutableStateOf<Int?>(null) }
                var showQuiz by remember { mutableStateOf(true) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(text = "Quiz App") }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        if (showQuiz) {
                            QuizScreen(
                                questions = questions,
                                onQuizComplete = { finalScore ->
                                    score = finalScore
                                    showQuiz = false
                                },
                                onRestart = {
                                    score = null
                                    showQuiz = true
                                }
                            )
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(text = "Your score is: $score/${questions.size}")
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = {
                                    score = null
                                    showQuiz = true
                                }) {
                                    Text(text = "Restart")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}