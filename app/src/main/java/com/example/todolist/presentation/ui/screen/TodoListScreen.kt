package com.example.todolist.presentation.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.todolist.presentation.viewmodel.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoListScreen(
    viewModel: TodoViewModel = viewModel(factory = TodoViewModel.Factory),
    onItemClick: (Int) -> Unit
) {
    val todos by viewModel.todos.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Список задач") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(todos) { todo ->
                Card(
                    onClick = { onItemClick(todo.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = todo.title,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = todo.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Checkbox(
                            checked = todo.isCompleted,
                            onCheckedChange = { viewModel.toggleTodo(todo.id) }
                        )
                    }
                }
            }
        }
    }
}