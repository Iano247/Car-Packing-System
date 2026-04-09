package com.example.carparkingsystem.ui.theme.screens.register

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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
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
import com.example.carparkingsystem.navigation.ROUTE_LOGIN
import com.example.carparkingsystem.ui.theme.CarParkingSystemTheme

@Composable
fun RegisterScreen(navController: NavController) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
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
                .border(1.dp, Color.White)
        )

        Text(
            text = "Register Here",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )

        OutlinedTextField(
            value = username,
            label = { Text(text = "Enter Username") },
            onValueChange = { username = it },
            placeholder = { Text(text = "Please Enter Username") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            modifier = Modifier.padding(bottom = 8.dp)
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
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = confirmpassword,
            onValueChange = { confirmpassword = it },
            placeholder = { Text(text = "Confirm Password") },
            label = { Text(text = "confirm Password") },
            leadingIcon = { Icon(Icons.Default.Check, contentDescription = null) },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Button(onClick = {
            authViewModel.signup(
                username = username,
                email = email,
                password = password,
                confirmpassword = confirmpassword,
                navController = navController,
                context = context
            )
        }) { Text(text = "Register") }
        Row {
            Text(text = "Already Registered? ", color = Color.Blue)
            Text(
                text = "Login Here",
                color = Color.Red,
                modifier = Modifier.clickable {
                    navController.navigate(ROUTE_LOGIN)
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    CarParkingSystemTheme {
        RegisterScreen(rememberNavController())
    }
}
