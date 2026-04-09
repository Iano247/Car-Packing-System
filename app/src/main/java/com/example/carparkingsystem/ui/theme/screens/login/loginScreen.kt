package com.example.carparkingsystem.ui.theme.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.carparkingsystem.R
import com.example.carparkingsystem.data.AuthViewModel
import com.example.carparkingsystem.navigation.ROUTE_REGISTER
import com.example.carparkingsystem.ui.theme.CarParkingSystemTheme


@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(110.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black)
                .shadow(4.dp, CircleShape)
        )

        Text(
            text = "Please Login Here",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
        Text(
            text = "Input Your Details Down Here",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = "Enter your Email") },
            label = { Text(text = "Enter your email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "Enter your Password") },
            label = { Text(text = "Enter your Password") },
            leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = {
            authViewModel.login(email, password, navController, context)
        }) { Text(text = "login") }
        Row {
            Text(text = "Don't have an account?", color = Color.Blue)
            Text(
                text = "Register Here",
                color = Color.Red,
                modifier = Modifier.clickable {
                    navController.navigate(ROUTE_REGISTER)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    CarParkingSystemTheme {
        LoginScreen(rememberNavController())
    }
}
