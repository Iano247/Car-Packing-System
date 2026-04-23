package com.example.carparkingsystem.ui.theme.screens.car

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.carparkingsystem.data.CarViewModel
import com.example.carparkingsystem.models.CarModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

@Composable
fun UpdateCarScreen(navController: NavController, carId: String) {
    val carViewModel: CarViewModel = viewModel()
    var car by remember { mutableStateOf<CarModel?>(null) }
    
    LaunchedEffect(carId) {
        val ref = FirebaseDatabase.getInstance()
            .getReference("Cars").child(carId)
        val snapshot = ref.get().await()
        car = snapshot.getValue(CarModel::class.java)?.apply {
            id = carId
        }
    }

    if (car == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        UpdateCarContent(car!!, carViewModel, navController, carId)
    }
}

@Composable
fun UpdateCarContent(
    car: CarModel,
    carViewModel: CarViewModel,
    navController: NavController,
    carId: String
) {
    var plateNumber by remember { mutableStateOf(car.plateNumber ?: "") }
    var carType by remember { mutableStateOf(car.carType ?: "") }
    var ownerName by remember { mutableStateOf(car.ownerName ?: "") }
    var phoneNumber by remember { mutableStateOf(car.phoneNumber ?: "") }
    var colorType by remember { mutableStateOf(car.colorType ?: "") }
    var entryTime by remember { mutableStateOf(car.entryTime ?: "") }

    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri -> imageUri.value = uri }
    }

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFFCE4EC), Color(0xFFF8BBD0))
                )
            )
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Update Car",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF880E4F)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    shape = CircleShape,
                    elevation = CardDefaults.cardElevation(6.dp),
                    modifier = Modifier
                        .size(140.dp)
                        .clickable { launcher.launch("image/*") }
                        .shadow(8.dp, CircleShape)
                ) {
                    AnimatedContent(
                        targetState = imageUri.value,
                        label = "Image Picker Animation"
                    ) { targetUri ->
                        AsyncImage(
                            model = targetUri ?: car.imageUrl,
                            contentDescription = "Car Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                Text(
                    text = "Tap to change picture",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )

                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 20.dp),
                    color = Color.LightGray,
                    thickness = 1.dp
                )

                val fieldModifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)

                val fieldShape = RoundedCornerShape(14.dp)

                OutlinedTextField(
                    value = plateNumber,
                    onValueChange = { plateNumber = it },
                    label = { Text("Plate Number") },
                    placeholder = { Text("e.g., KCT 832B") },
                    modifier = fieldModifier,
                    shape = fieldShape
                )

                OutlinedTextField(
                    value = carType,
                    onValueChange = { carType = it },
                    label = { Text("Car Type") },
                    placeholder = { Text("e.g., Sedan") },
                    modifier = fieldModifier,
                    shape = fieldShape
                )

                OutlinedTextField(
                    value = ownerName,
                    onValueChange = { ownerName = it },
                    label = { Text("Owner Name") },
                    placeholder = { Text("e.g., John Doe") },
                    modifier = fieldModifier,
                    shape = fieldShape
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    placeholder = { Text("e.g., +254 712 345 678") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = fieldModifier,
                    shape = fieldShape
                )

                OutlinedTextField(
                    value = colorType,
                    onValueChange = { colorType = it },
                    label = { Text("Color") },
                    placeholder = { Text("e.g., Blue") },
                    modifier = fieldModifier,
                    shape = fieldShape
                )
                
                OutlinedTextField(
                    value = entryTime,
                    onValueChange = { entryTime = it },
                    label = { Text("Entry Time") },
                    placeholder = { Text("e.g., 10:00 AM") },
                    modifier = fieldModifier,
                    shape = fieldShape
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { navController.popBackStack() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.LightGray
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.width(140.dp)
                    ) {
                        Text("Go Back", color = Color.DarkGray)
                    }

                    Button(
                        onClick = {
                            carViewModel.updateCar(
                                carId,
                                imageUri.value,
                                plateNumber,
                                carType,
                                ownerName,
                                phoneNumber,
                                colorType,
                                entryTime,
                                context,
                                navController
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD81B60)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.width(140.dp)
                    ) {
                        Text("Update", color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
