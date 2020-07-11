package com.example.tmdb.ui.activity

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.tmdb.MyApplication
import com.example.tmdb.R
import com.example.tmdb.Utils.showToast
import com.example.tmdb.broadcastReciever.NetworkBroadcastReceiver
import com.example.tmdb.databinding.ActivityMainBinding
import com.example.tmdb.paging.MovieDataSource
import com.example.tmdb.ui.activity.MainActivity.Values.DARK_THEME
import com.example.tmdb.ui.activity.MainActivity.Values.LIGHT_THEME
import com.example.tmdb.ui.activity.MainActivity.Values.THEME_SELECTED
import com.example.tmdb.ui.fragments.MovieFragment
import com.example.tmdb.ui.interfaces.InternetChecker
import com.example.tmdb.ui.interfaces.OnPageLoading
import com.example.tmdb.viewmodel.MoviesViewModel
import java.lang.Exception
import javax.inject.Inject
import kotlin.properties.Delegates


class MainActivity : AppCompatActivity(), InternetChecker, OnPageLoading {

    private lateinit var binding: ActivityMainBinding

    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var sharedPrefFile: String

    private lateinit var broadcastReceiver: NetworkBroadcastReceiver

    private var themeSelected by Delegates.notNull<Int>()

    private var currentPage = 1

    @Inject
    lateinit var viewModel: MoviesViewModel

    object Values {
        const val LIGHT_THEME = R.id.rb_light_theme
        const val DARK_THEME = R.id.rb_dartk_theme
        const val RIPPLE_DELAY = 500L
        const val THEME_SELECTED = "theme_selected"
        val TAG = this.javaClass.name
    }

    companion object {
        var firstTime: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSharedPreferences()
        setupTheme()
        (this.application as MyApplication).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        initBroadcastReceiver()
        MovieDataSource.setPageListener(this)
    }

    private fun initBroadcastReceiver() {
        broadcastReceiver = NetworkBroadcastReceiver()
        broadcastReceiver.setListener(this)
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }
        registerReceiver(broadcastReceiver, filter)
    }

    private fun setupTheme() {
        if(themeSelected == DARK_THEME) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
    }

    private fun setupSharedPreferences() {
        sharedPrefFile = applicationContext.packageName
        sharedPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        themeSelected = sharedPreferences.getInt(THEME_SELECTED, LIGHT_THEME)
    }

    override fun onPause() {
        var preferenceEditor = sharedPreferences.edit()
        preferenceEditor.putInt(THEME_SELECTED, themeSelected)
        preferenceEditor.apply()
        removeReceiver()
        super.onPause()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return navController.navigateUp()
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
        var dialog: AlertDialog.Builder?
        dialog = getDialog()
        var view = getView(R.layout.dilaog_thems)
        dialog.setView(view)
        var themeRadioGroup = view.findViewById<RadioGroup>(R.id.rg_themes)
        showThemeFromSharedPref(themeRadioGroup)
        setThemeClickListener(themeRadioGroup)
        dialog.show()
    }

    private fun getDialog(): AlertDialog.Builder {
        return if(themeSelected == DARK_THEME) {
            AlertDialog.Builder(this, R.style.DarkThemeDialog)

        } else {
            AlertDialog.Builder(this, R.style.LightThemeDialog)
        }
    }

    private fun getView(layoutRes: Int) = layoutInflater.inflate(layoutRes, null)

    private fun showThemeFromSharedPref(themeRadioGroup: RadioGroup?) {
        when(themeSelected) {
            LIGHT_THEME -> {
                highLightRadioButton(themeRadioGroup, LIGHT_THEME)
            }
           DARK_THEME -> {
                highLightRadioButton(themeRadioGroup, DARK_THEME)
            }
        }
    }

    private fun highLightRadioButton(
        themeRadioGroup: RadioGroup?,
        rbId: Int
    ) {
        var radioButton = themeRadioGroup?.findViewById<RadioButton>(rbId)
        radioButton?.isChecked = true
    }

    private fun setThemeClickListener(themeRadioGroup: RadioGroup) {
        themeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                LIGHT_THEME -> {
                    changeTheme(AppCompatDelegate.MODE_NIGHT_NO)
                    themeSelected = LIGHT_THEME
                }
                DARK_THEME -> {
                    changeTheme(AppCompatDelegate.MODE_NIGHT_YES)
                    themeSelected = DARK_THEME
                }
            }
        }
    }

    private fun restartActivity() {
        finish()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun changeTheme(themeId: Int) {
        Handler().postDelayed(
            {AppCompatDelegate.setDefaultNightMode(themeId)
            restartActivity()}, Values.RIPPLE_DELAY)
    }
    
    override fun isInternetAvailable(boolean: Boolean) {
        if(boolean) {
          try {
              var navHost = supportFragmentManager.findFragmentById(R.id.myNavHostFragment)
              var movieFragment = navHost?.childFragmentManager?.fragments?.get(0) as MovieFragment

//              movieFragment.scrollAndFetch(currentPage)
          } catch (e: Exception) {

          }
        } else {
            Handler().postDelayed({
                showToast(this, "please check your internet connection")
            }, 10)
        }
    }

    override fun onDestroy() {
        removeReceiver()
        super.onDestroy()
    }

    private fun removeReceiver() {
        try {
            unregisterReceiver(broadcastReceiver)
        } catch (e: Exception) {
            Log.e(Values.TAG, "${e.message}")
        }
    }

    override fun getPageLoading(page: Int) {
        currentPage = page
    }
}
