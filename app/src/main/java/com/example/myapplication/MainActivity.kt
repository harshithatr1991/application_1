@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.api_service.RetrofitInstance
import com.example.myapplication.data.Todo
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TodoApp()
        }
    }
}

@ExperimentalMaterial3Api
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun TodoApp() {
    val scope = rememberCoroutineScope()
    var todos by remember {
             mutableStateOf<List<Todo>>(emptyList())
    }

    var errorMessage by remember { mutableStateOf("")}

    Scaffold(
        topBar = {
            TopAppBar(title = {Text("Todo App")})
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    scope.launch {
                        try {
                            todos = RetrofitInstance.api.getTodos().todos
                        } catch (e: Exception) {
                            errorMessage = e.message ?: "Unknown Error"
                        }
                    }
                }
            ) {
                Text("Fetch Todos")
            }

            Spacer(modifier = Modifier .height(16.dp))

            Button(onClick = {
                todos = todos.filter{!it.completed}
            }) {
                Text("Pending Todos")
            }
            Spacer(modifier = Modifier .height(16.dp))
            Button(onClick = {
                todos = todos + Todo (
                    id = todos.size + 1,
                    todo = "New Task",
                    completed = false,
                    userId = 1
                )
            }) {
                Text("Add Todo")
            }
            Spacer(modifier = Modifier .height(16.dp))

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = MaterialTheme.colorScheme.error)
            } else {
                LazyColumn {
                    items(todos) { todo ->
                        Text("${todo.id}. ${todo.todo} - Completed: ${todo.completed}")

                    }
                }
            }
        }
    }
}

