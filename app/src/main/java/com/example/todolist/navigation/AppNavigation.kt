package com.example.todolist.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.todolist.presentation.ui.screen.TodoDetailScreen
import com.example.todolist.presentation.ui.screen.TodoListScreen
import com.example.todolist.presentation.viewmodel.TodoViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            TodoListScreen(
                onItemClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }
        composable("detail/{id}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("id")?.toIntOrNull()
            TodoDetailScreen(
                todoId = todoId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}