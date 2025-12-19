package com.example.todolist

import com.example.todolist.domain.model.TodoItem
import com.example.todolist.domain.repository.TodoRepository
import com.example.todolist.domain.usecase.GetTodosUseCase
import com.example.todolist.domain.usecase.ToggleTodoUseCase
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TodoUnitTests {

    private lateinit var repository: FakeTodoRepository
    private lateinit var getTodosUseCase: GetTodosUseCase
    private lateinit var toggleTodoUseCase: ToggleTodoUseCase

    @Before
    fun setUp() {
        // Используем Fake-репозиторий для изоляции тестов
        repository = FakeTodoRepository()
        getTodosUseCase = GetTodosUseCase(repository)
        toggleTodoUseCase = ToggleTodoUseCase(repository)
    }

    @Test
    fun `GetTodosUseCase returns 3 tasks`() = runBlocking {
        val todos = getTodosUseCase()
        assertEquals(3, todos.size)
    }

    @Test
    fun `toggleTodo changes isCompleted`() = runBlocking {
        // Берем первую задачу (id: 1, isCompleted: false)
        val id = 1
        val initialStatus = repository.getTodoById(id)?.isCompleted ?: false
        
        toggleTodoUseCase(id)
        
        val updatedStatus = repository.getTodoById(id)?.isCompleted ?: false
        assertTrue("Статус задачи должен измениться на противоположный", initialStatus != updatedStatus)
    }
}

// Вспомогательный класс для тестов
class FakeTodoRepository : TodoRepository {
    private var todos = mutableListOf(
        TodoItem(1, "Task 1", "Desc 1", false),
        TodoItem(2, "Task 2", "Desc 2", true),
        TodoItem(3, "Task 3", "Desc 3", false)
    )

    override suspend fun getTodos(): List<TodoItem> = todos
    override suspend fun getTodoById(id: Int): TodoItem? = todos.find { it.id == id }
    override suspend fun toggleTodo(id: Int) {
        val index = todos.indexOfFirst { it.id == id }
        if (index != -1) {
            val old = todos[index]
            todos[index] = old.copy(isCompleted = !old.isCompleted)
        }
    }
}
