package com.example.tmdb.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tmdb.R
import com.example.tmdb.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    val LIGHT_THEME = 0
    val DARK_THEME = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
    }

    companion object {
        var firstTime: Boolean = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_theme -> {
               setDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDialog() {
        var arr = arrayOf("Light Theme", "Dark Theme")
        var dialog: AlertDialog.Builder?
        dialog = if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            AlertDialog.Builder(this, R.style.DarkThemeDialog)

        } else {
            AlertDialog.Builder(this, R.style.LightThemeDialog)
        }
        dialog.setTitle("Choose Theme")
        dialog.setItems(arr) { dialog, which ->
            when(which) {
                LIGHT_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    restartActivity()
                }
                DARK_THEME -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    restartActivity()
                }
            }
        }
        dialog.show()
    }

    fun restartActivity() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }
}
