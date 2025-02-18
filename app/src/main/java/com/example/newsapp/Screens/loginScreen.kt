package com.example.newsapp.Screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.newsapp.topNewsScreen
import com.example.newsapp.ui.theme.NewsAppTheme

@Composable
fun loginScreen(
    navController: NavController
){
    var loginText by rememberSaveable { mutableStateOf("")}
    var passwordText by rememberSaveable { mutableStateOf("")}
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = loginText,
                onValueChange = {loginText = it},
                label = {
                    Text("Login")
                        },
                maxLines = 1,
                modifier = Modifier
                    .size(256.dp,61.dp),
            )
            OutlinedTextField(
                value = passwordText,
                onValueChange = {passwordText = it},
                visualTransformation = PasswordVisualTransformation(),
                label = {
                    Text("Password")
                        },
                maxLines = 1,
                modifier = Modifier.size(256.dp,61.dp),
            )
            Row(
                Modifier.padding(top=14.dp)
            ) {
                Button(
                    modifier = Modifier
                        .size(143.dp,51.dp),
                    shape = RectangleShape,
                    onClick = { }
                ) {
                    Text("To Register")
                }
                Spacer(Modifier.size(18.dp))
                Button(
                    modifier = Modifier
                        .size(143.dp,51.dp),
                    shape = RectangleShape,
                    onClick = {
                        navController.navigate(topNewsScreen)
                    }
                ) {
                    Text("Login")
             }
            }

        }
}

