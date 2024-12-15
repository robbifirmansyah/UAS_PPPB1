package com.example.uas_pppb1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.uas_pppb1.network.ApiClient
import com.example.uas_pppb1.network.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etUsername = findViewById<EditText>(R.id.etUsernameLogin)
        val etPassword = findViewById<EditText>(R.id.etPasswordLogin)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvGoToSignup = findViewById<TextView>(R.id.tvGoToSignup)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Proses login menggunakan API
            loginWithApi(username, password)
        }

        tvGoToSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loginWithApi(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)
                val response = apiService.getAllUsers("vIJIF", "users") // API call

                if (response.isSuccessful && response.body() != null) {
                    val users = response.body()!!
                    val user = users.find { it.username == username && it.password == password }

                    withContext(Dispatchers.Main) {
                        if (user != null) {
                            saveLoginStatus(user.role)

                            // Navigasi berdasarkan role
                            if (user.role == "admin") {
                                navigateToAdminPage()
                            } else {
                                navigateToUserPage()
                            }
                        } else {
                            Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Failed to fetch users", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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

    private fun navigateToAdminPage() {
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToUserPage() {
        val intent = Intent(this, UserActivity::class.java)
        startActivity(intent)
        finish()
    }
}
