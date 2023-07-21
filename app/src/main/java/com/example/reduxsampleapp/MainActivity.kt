package com.example.reduxsampleapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.reduxsampleapp.ui.theme.ReduxSampleAppTheme
import com.example.remidnerslibrary.tbx_toOneDecimal
import java.sql.Time

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReduxSampleAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TodoList()
                }
            }
        }
    }
}

@Composable
fun TodoList() {

    12.0.tbx_toOneDecimal()
    val todos = remember { mutableStateOf(store.state.todos) }
    val filter = remember { mutableStateOf(store.state.visibilityFilter) }
    store.subscribe {
        todos.value = store.state.todos
        filter.value = store.state.visibilityFilter
    }

    Column {
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.Top
        ){
            Button(onClick = { store.dispatch(SetVisibilityFilter(VisibilityFilter.SHOW_ALL)) }) {
                Text("Show All")
            }
            Button(onClick = { store.dispatch(SetVisibilityFilter(VisibilityFilter.SHOW_COMPLETED)) }) {
                Text("Show Completed")
            }
            Button(onClick = { store.dispatch(SetVisibilityFilter(VisibilityFilter.SHOW_ACTIVE)) }) {
                Text("Show Active")
            }
        }

        val filteredTodos = when(filter.value){
            VisibilityFilter.SHOW_ALL -> todos.value
            VisibilityFilter.SHOW_ACTIVE -> todos.value.filter { !it.completed }
            VisibilityFilter.SHOW_COMPLETED -> todos.value.filter { it.completed }
        }
        filteredTodos.forEach { todo ->
            LineItem(todos.value.indexOf(todo), todo)
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            var newTodo by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(""))
            }
            TextField(
                value = newTodo,
                onValueChange = {
                    newTodo = it
                },
                label = { Text(" Add new todo") }
            )
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    store.dispatch(AddTodo(newTodo.text))
                    newTodo = TextFieldValue("")
                }) {
                Text(text = "+")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LineItem(index: Int, todo: Todo) {
    Row {
        Checkbox(
            modifier = Modifier.align(Alignment.CenterVertically),
            checked = todo.completed,
            onCheckedChange = {
                store.dispatch(ToggleTodo(index))
            })
        Text(
            text = todo.text,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ReduxSampleAppTheme {
        TodoList()
    }
}