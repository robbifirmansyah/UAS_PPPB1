package com.example.uas_pppb1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uas_pppb1.data.model.User
import com.example.uas_pppb1.network.ApiClient
import com.example.uas_pppb1.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID
import org.bson.types.ObjectId // Import ObjectId


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val etUsername = findViewById<EditText>(R.id.etUsernameRegister)
        val etPassword = findViewById<EditText>(R.id.etPasswordRegister)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proses registrasi
            registerUser(username, password)
        }
    }

    private fun registerUser(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)
                val newUser = User(
                    _id = ObjectId().toHexString(), // Menghasilkan ObjectId yang valid
                    username = username,
                    password = password,
                    role = "user"
                )


                val response = apiService.registerUser("vIJIF", "users", newUser)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@RegisterActivity, "Successfully registered!", Toast.LENGTH_SHORT).show()
                        // Langsung login setelah registrasi berhasil
                        loginUser(username, password)
                    } else {
                        // Tambahkan log untuk melihat error body
                        Toast.makeText(
                            this@RegisterActivity,
                            "Failed to register: ${response.message()} - ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun loginUser(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)
                val response = apiService.getAllUsers("vIJIF", "users")
                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!
                    val user = users.find { it.username == username && it.password == password }

                    withContext(Dispatchers.Main) {
                        if (user != null) {
                            // Simpan status login dan role ke Shared Preferences
                            saveLoginStatus(user.role)
                            // Navigasi langsung ke halaman Home
                            val intent = Intent(this@RegisterActivity, UserActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity, "Login failed after registration", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@RegisterActivity, "Error fetching users", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveLoginStatus(role: String) {
        val sharedPreferences = getSharedPreferences("AppPrefs", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("role", role)
            putBoolean("isLoggedIn", true)
            apply()
        }
    }
}
