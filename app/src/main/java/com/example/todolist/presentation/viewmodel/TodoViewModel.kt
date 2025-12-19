package com.example.todolist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.todolist.data.local.TodoJsonDataSource
import com.example.todolist.data.repository.TodoRepositoryImpl
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.usecase.GetTodoByIdUseCase
import com.example.todolist.domain.usecase.GetTodosUseCase
import com.example.todolist.domain.usecase.ToggleTodoUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TodoViewModel(
    private val getTodosUseCase: GetTodosUseCase,
    private val getTodoByIdUseCase: GetTodoByIdUseCase,
    private val toggleTodoUseCase: ToggleTodoUseCase
) : ViewModel() {
    private val _todos = MutableStateFlow<List<TodoItem>>(emptyList())
    val todos: StateFlow<List<TodoItem>> = _todos

    private val _selectedTodo = MutableStateFlow<TodoItem?>(null)
    val selectedTodo: StateFlow<TodoItem?> = _selectedTodo

    init {
        loadTodos()
    }

    private fun loadTodos() {
        viewModelScope.launch {
            _todos.value = getTodosUseCase()
        }
    }

    fun getTodoById(id: Int) {
        viewModelScope.launch {
            _selectedTodo.value = getTodoByIdUseCase(id)
        }
    }

    fun toggleTodo(id: Int) {
        viewModelScope.launch {
            toggleTodoUseCase(id)
            loadTodos()
            // Update selected todo if it's the one toggled
            if (_selectedTodo.value?.id == id) {
                getTodoById(id)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]
                    ?: throw IllegalArgumentException("Application context is missing")
                
                val dataSource = TodoJsonDataSource(application.applicationContext)
                val repository = TodoRepositoryImpl(dataSource)
                val getTodosUseCase = GetTodosUseCase(repository)
                val getTodoByIdUseCase = GetTodoByIdUseCase(repository)
                val toggleTodoUseCase = ToggleTodoUseCase(repository)
                
                TodoViewModel(
                    getTodosUseCase = getTodosUseCase,
                    getTodoByIdUseCase = getTodoByIdUseCase,
                    toggleTodoUseCase = toggleTodoUseCase
                )
            }
        }
    }
}
