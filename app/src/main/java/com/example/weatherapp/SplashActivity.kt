package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_splash)

    imgSplash.alpha=0f
    imgSplash.animate().setDuration(3000).alpha(1f).withEndAction{
        val intent= Intent(this@SplashActivity,MainActivity::class.java)
        startActivity(intent)

        /*To set alpha or transparency for Image in Android Compose,
               assign required alpha value for alpha parameter of Image.
               alpha parameter of Image accepts a float value between 0.0 and 1.0
               corresponding to full transparency to no transparency, respectively.
               https://www.tutorialkart.com/android-jetpack-compose/image-alpha-transparency/
               */

    }
}
}