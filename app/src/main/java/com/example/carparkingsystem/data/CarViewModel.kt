package com.example.carparkingsystem.data

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.carparkingsystem.models.CarModel
import com.example.carparkingsystem.navigation.ROUTE_DASHBOARD
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class CarViewModel: ViewModel() {
    private val cloudinaryUrl = "https://api.cloudinary.com/v1_1/dtyzhif3b/image/upload"
    private val uploadPreset = "pics_folder"
    private val client = OkHttpClient()
    private val database = FirebaseDatabase.getInstance().getReference("Cars")

    var cars: SnapshotStateList<CarModel> = mutableStateListOf()

    fun uploadCar(
        imageUri: Uri?,
        plateNumber: String,
        carType: String,
        ownerName: String,
        phoneNumber: String,
        colorType: String = "",
        entryTime: String = "",
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val ref = database.push()
                val carData = mapOf(
                    "id" to ref.key,
                    "ownerName" to ownerName,
                    "plateNumber" to plateNumber,
                    "phoneNumber" to phoneNumber,
                    "carType" to carType,
                    "colorType" to colorType,
                    "entryTime" to entryTime,
                    "imageUrl" to imageUrl
                )
                ref.setValue(carData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Car saved Successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_DASHBOARD)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Car not saved: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun updateCar(
        carId: String,
        imageUri: Uri?,
        plateNumber: String,
        carType: String,
        ownerName: String,
        phoneNumber: String,
        colorType: String,
        entryTime: String,
        context: Context,
        navController: NavController
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUrl = imageUri?.let { uploadToCloudinary(context, it) }
                val ref = database.child(carId)
                
                val updates = mutableMapOf<String, Any?>(
                    "plateNumber" to plateNumber,
                    "carType" to carType,
                    "ownerName" to ownerName,
                    "phoneNumber" to phoneNumber,
                    "colorType" to colorType,
                    "entryTime" to entryTime
                )
                
                if (imageUrl != null) {
                    updates["imageUrl"] = imageUrl
                }

                ref.updateChildren(updates).await()
                
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Car updated Successfully", Toast.LENGTH_LONG).show()
                    navController.navigate(ROUTE_DASHBOARD)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun fetchcar(context: Context) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cars.clear()
                for (carSnapshot in snapshot.children) {
                    val car = carSnapshot.getValue(CarModel::class.java)
                    car?.let { cars.add(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun deleteCar(carId: String, context: Context) {
        database.child(carId).removeValue().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "Car deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Failed to delete car", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadToCloudinary(context: Context, uri: Uri): String {
        val fileBytes = context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
            ?: throw Exception("Image read failed")
        
        val requestBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", "image.jpg",
                fileBytes.toRequestBody("image/*".toMediaTypeOrNull()))
            .addFormDataPart("upload_preset", uploadPreset).build()
        
        val request = Request.Builder().url(cloudinaryUrl).post(requestBody).build()
        val response = client.newCall(request).execute()
        
        if (!response.isSuccessful) throw Exception("Upload failed: ${response.message}")
        
        val responseBody = response.body?.string()
        val secureUrl = Regex("\"secure_url\":\"(.*?)\"")
            .find(responseBody ?: "")?.groupValues?.get(1)

        return secureUrl ?: throw Exception("Failed to get image URL")
    }
}
