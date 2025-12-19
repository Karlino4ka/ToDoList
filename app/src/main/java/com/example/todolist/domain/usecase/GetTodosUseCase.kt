package com.example.todolist.domain.usecase

import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow

class GetTodosUseCase(private val repository: TodoRepository) {
    suspend operator fun invoke(): List<TodoItem> = repository.getTodos()
}