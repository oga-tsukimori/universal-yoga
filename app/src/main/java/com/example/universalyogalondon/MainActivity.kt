package com.example.universalyogalondon

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.universalyogalondon.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var viewModel: SharedViewModel
    private lateinit var courseAdapter: CourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.nav_yoga_class, R.id.nav_class_calendar, R.id.nav_user_profile)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNavView.setupWithNavController(navController)

        viewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        setupCourseList()
        observeCourses()
    }

    private fun setupCourseList() {
        val recyclerView = findViewById<RecyclerView>(R.id.courseRecyclerView)
        courseAdapter = CourseAdapter(emptyList())
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = courseAdapter
        }
    }

    private fun observeCourses() {
        viewModel.courses.observe(this) { courses ->
            courseAdapter.updateCourses(courses)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
