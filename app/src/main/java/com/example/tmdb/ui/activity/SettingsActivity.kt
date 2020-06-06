package com.example.tmdb.ui.activity

import android.content.Intent
import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.example.tmdb.R
import com.example.tmdb.databinding.ActivitySettings2Binding

class SettingsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySettings2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings2)
        setSupportActionBar(findViewById(R.id.toolbar))
        setToggleListener()

    }

    private fun setToggleListener() {
        binding.layoutContent.switchTheme.setOnClickListener {
            if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}