package com.example.dicegame

import android.content.Context
import android.graphics.Color
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import kotlin.random.Random

class Dice (private val context: Context, val image: ImageButton) {
    fun roll() {
        val result = Random.nextInt(1, 7)
        val drawableId = when (result) {
            1 -> R.drawable.one
            2 -> R.drawable.two
            3 -> R.drawable.three
            4 -> R.drawable.four
            5 -> R.drawable.five
            else -> R.drawable.six
        }

        val animation = AnimationUtils.loadAnimation(context,R.anim.rotation);
        image.setImageResource(drawableId)
        image.contentDescription = result.toString()
        image.scaleType = ImageView.ScaleType.FIT_CENTER
        image.startAnimation(animation)
    }
}