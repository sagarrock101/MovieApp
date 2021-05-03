package com.sagaRock101.tmdb.ui.fragments

import android.os.Handler
import android.os.Looper
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.sagaRock101.tmdb.R
import com.sagaRock101.tmdb.ui.activity.MainActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class MovieNavigationTest {


    @Test
    fun testNavigation() {
        val activityScenario: ActivityScenario<MainActivity> = ActivityScenario.launch(MainActivity::class.java)
        onView(withId(R.id.cl_splash_screen)).check(matches(isDisplayed()))
    }


    @Test
    fun testNavigation2() {
        val activityScenario: ActivityScenario<MainActivity> = ActivityScenario.launch(MainActivity::class.java)
        Handler(Looper.getMainLooper()).postDelayed({
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
        }, 1000)
    }
}