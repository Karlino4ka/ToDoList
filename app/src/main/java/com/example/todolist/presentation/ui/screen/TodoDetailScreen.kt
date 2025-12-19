package com.example.todolist.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.presentation.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailScreen(
    todoId: Int?,
    viewModel: TodoViewModel = viewModel(factory = TodoViewModel.Factory),
    onBack: () -> Unit
) {
    val todo by viewModel.selectedTodo.collectAsState()

    LaunchedEffect(todoId) {
        if (todoId != null) {
            viewModel.getTodoById(todoId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали задачи") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (todo == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("Задача не найдена")
            }
        } else {
            val currentTodo = todo!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = currentTodo.title,
                    style = MaterialTheme.typography.headlineMedium
                )
                Text(
                    text = currentTodo.description,
                    style = MaterialTheme.typography.bodyLarge
                )
                Row {
                    Text("Статус: ", style = MaterialTheme.typography.bodyLarge)
                    Text(
                        text = if (currentTodo.isCompleted) "Выполнена" else "Не выполнена",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (currentTodo.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                    )
                }
                
                Button(
                    onClick = { viewModel.toggleTodo(currentTodo.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (currentTodo.isCompleted) "Отметить как невыполненную" else "Отметить как выполненную")
                }
            }
        }
    }
}