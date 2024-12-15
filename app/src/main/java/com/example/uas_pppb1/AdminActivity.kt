package com.example.uas_pppb1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.uas_pppb1.fragments.AdminHomeFragment
import com.example.uas_pppb1.fragments.AdminProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.adminBottomNav)

        // Load default fragment
        loadFragment(AdminHomeFragment())

        // Handle navigation item clicks
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.adminHome -> loadFragment(AdminHomeFragment())
                R.id.adminProfile -> loadFragment(AdminProfileFragment())
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment): Boolean {
        supportFragmentManager.beginTransaction()
            .replace(R.id.adminFragmentContainer, fragment)
            .commit()
        return true
    }
}
