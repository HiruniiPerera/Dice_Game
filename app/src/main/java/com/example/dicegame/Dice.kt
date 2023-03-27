package com.example.dicegame

import android.content.Context
import android.view.animation.AnimationUtils
import android.widget.ImageButton
import android.widget.ImageView
import kotlin.random.Random

/**
 * This class represents a dice
 *
 * @property context The context in which the dice is used
 * @property image The image button that displays the dice
 */
class Dice(private val context: Context, val image: ImageButton) {
    //Boolean variable to check if the dice is clicked or not
    private var isClicked = false

    /**
     * Function to access the value of isClicked
     *
     * @return true if the dice is clicked or false if it's not clicked
     */
    fun getIsClicked(): Boolean {
        return isClicked
    }

    /**
     * Function that sets the state of the button
     *
     * @param state true if it is clicked or false if it's not
     */
    fun setIsClicked(state: Boolean) {
        isClicked = state
    }

    /**
     * Function that will change the state of the dice when clicked
     *
     */
    fun changeToggle() {
        isClicked = !isClicked
    }

    /**
     * Function that rolls the dice and shows the result
     *
     */
    fun roll() {
        //generates a random number from 1 to 6
        val result = Random.nextInt(1, 7)
        val drawableId = when (result) {
            1 -> R.drawable.one
            2 -> R.drawable.two
            3 -> R.drawable.three
            4 -> R.drawable.four
            5 -> R.drawable.five
            else -> R.drawable.six
        }

        //applies a rolling animation to the dice
        val animation = AnimationUtils.loadAnimation(context, R.anim.rotation)

        /**
         * Reference : Android Developers.(n.d.). Animation Resources.
         * Available at: https://developer.android.com/guide/topics/resources/animation-resource#Rotate
         * [Accessed 7 March 2023]
         *
         * for the rotating animation of the dice
         */

        //sets the image resource based on the random integer generated
        image.setImageResource(drawableId)
        image.contentDescription = result.toString()
        image.scaleType = ImageView.ScaleType.FIT_CENTER
        image.startAnimation(animation)
    }
}