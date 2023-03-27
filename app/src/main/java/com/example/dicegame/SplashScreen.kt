package com.example.dicegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.core.os.postDelayed

/**
 * Splash screen activity class
 * This activity will be displayed first when the application opens
 */

//How long this activity will display for
private val time: Long = 3000


class SplashScreen : AppCompatActivity() {

    /**
     * reference: GeeksforGeeks. (2020). How to Create a Splash Screen in Android using Kotlin?
     * Available at: https://www.geeksforgeeks.org/how-to-create-a-splash-screen-in-android-using-kotlin/
     * [Accessed 18 Mar. 2023].
     */
    /**
     * Main method called when the activity is created
     *
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //displays the activity in full screen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash_screen)

        //using a handler to delay moving to the next activity via using an intent
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, time)

    }

}