package com.example.reduxsampleapp

import org.reduxkotlin.createThreadSafeStore

data class AppState(
    val visibilityFilter: VisibilityFilter = VisibilityFilter.SHOW_ALL,
    val todos: List<Todo> = listOf(
        Todo(text = "Consider using Redux",
            completed = true),
        Todo(text = "Keep all state in a single tree",
            completed = false)
    ),
)

val store = createThreadSafeStore(::rootReducer, AppState())