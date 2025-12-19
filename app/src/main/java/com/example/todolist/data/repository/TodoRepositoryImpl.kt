package com.example.todolist.data.repository

import com.example.todolist.data.local.TodoJsonDataSource
import com.example.todolist.data.model.TodoItemDto
import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TodoRepositoryImpl(
    private val dataSource: TodoJsonDataSource
) : TodoRepository {
    private var todos = dataSource.getTodos().toMutableList()

    override suspend fun getTodos(): List<TodoItem> {
        return todos.map { it.toDomain() }
    }

    override suspend fun getTodoById(id: Int): TodoItem? {
        return todos.find { it.id == id }?.toDomain()
    }

    override suspend fun toggleTodo(id: Int) {
        val index = todos.indexOfFirst { it.id == id }
        if (index != -1) {
            val old = todos[index]
            todos[index] = old.copy(isCompleted = !old.isCompleted)
        }
    }

    private fun TodoItemDto.toDomain(): TodoItem {
        return TodoItem(
            id = this.id,
            title = this.title,
            description = this.description,
            isCompleted = this.isCompleted
        )
    }
}