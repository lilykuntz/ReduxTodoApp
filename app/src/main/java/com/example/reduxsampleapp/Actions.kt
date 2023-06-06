package com.example.reduxsampleapp

/*
 * Action types
 */
data class AddTodo(val text: String)
data class ToggleTodo(val index: Int)
data class SetVisibilityFilter(val visibilityFilter: VisibilityFilter)

/*
 * Other declarations
 */

enum class VisibilityFilter {
    SHOW_ALL,
    SHOW_COMPLETED,
    SHOW_ACTIVE
}

data class Todo(
    val text: String,
    val completed: Boolean
)

fun todosReducer(state: List<Todo>, action: Any) =
    when (action) {
        is AddTodo -> state.plus(
            Todo(
                text = action.text,
                completed = false
            )
        )
        is ToggleTodo -> state.mapIndexed { index, todo ->
            if (index == action.index) {
                todo.copy(completed = !todo.completed)
            } else {
                todo
            }
        }
        else -> state
    }

fun visibilityFilterReducer(state: VisibilityFilter, action: Any): VisibilityFilter =
    when (action) {
        is SetVisibilityFilter -> action.visibilityFilter
        else -> state
    }

fun rootReducer(state: AppState, action: Any) = AppState(
    todos = todosReducer(state.todos, action),
    visibilityFilter = visibilityFilterReducer(state.visibilityFilter, action)
)