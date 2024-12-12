package com.example.myapplication.data

data class Todo(
    val id: Int,
    val todo: String,
    val completed: Boolean,
    val userId: Int
)

data class TodoResponse(
    val todos: List<Todo>,
    val total: Int,
    val skip: Int,
    val limit: Int
)
