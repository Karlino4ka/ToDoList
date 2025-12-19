package com.example.todolist

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test

class TodoUiTests {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private fun waitForText(text: String, timeout: Long = 5000) {
        composeTestRule.waitUntil(timeout) {
            composeTestRule.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }

    @Test
    fun allTasksFromJsonAreDisplayed() {
        // Ждем загрузки данных из JSON
        waitForText("Купить молоко")
        
        composeTestRule.onNodeWithText("Купить молоко").assertIsDisplayed()
        composeTestRule.onNodeWithText("Позвонить маме").assertIsDisplayed()
        composeTestRule.onNodeWithText("Сделать ДЗ по Android").assertIsDisplayed()
    }

    @Test
    fun checkboxTogglesStatus() {
        waitForText("Купить молоко")

        // Используем testTag, который мы добавили в TodoListScreen
        val checkbox = composeTestRule.onNodeWithTag("checkbox_1")

        // Проверяем начальное состояние (false в JSON)
        checkbox.assertIsOff()
        
        // Кликаем по чекбоксу
        checkbox.performClick()
        
        // Проверяем, что состояние изменилось
        checkbox.assertIsOn()
    }

    @Test
    fun navigationListToDetailAndBack() {
        waitForText("Купить молоко")

        // 1. Переход в детали
        composeTestRule.onNodeWithText("Купить молоко").performClick()
        
        // Ожидаем отрисовки экрана деталей
        waitForText("Детали задачи")
        waitForText("2 литра, обезжиренное")

        composeTestRule.onNodeWithText("Детали задачи").assertExists()
        
        // 2. Возврат назад
        composeTestRule.onNodeWithContentDescription("Назад").performClick()
        
        // Ожидаем возврата к списку
        waitForText("Список задач")
        composeTestRule.onNodeWithText("Список задач").assertIsDisplayed()
    }
}
