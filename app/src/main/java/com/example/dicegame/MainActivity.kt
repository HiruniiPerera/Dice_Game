package com.example.dicegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog

/**
 * Main activity class that displays the main menu of the game
 * Allows the user to choose the difficulty level and also allows to set the target of the game
 *
 */
class MainActivity : AppCompatActivity() {
    //variable that decides if the computer should use the random strategy or the optimised strategy
    var hardMode = false
    //target to achieve
    private var target = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // creates a drop down menu for the user to choose the difficulty level
        val options = arrayOf("Easy", "Hard")
        val mode = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, options)
        mode.adapter = adapter
        mode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Get the index of the selected item
                val selectedIndex = position

                // setting the game mode
                if (selectedIndex == 0) {
                    hardMode = false
                } else if (selectedIndex == 1) {
                    hardMode = true
                }
            }

            //default game mode
            override fun onNothingSelected(parent: AdapterView<*>) {
                hardMode = false
            }
        }

        //creating a button that will allow the user to start a new game
        val newButton = findViewById<Button>(R.id.newgame)

        newButton.setOnClickListener {
            //gets the target chosen by the user
            target = findViewById<EditText>(R.id.edittarget).text.toString()
            var targetString = ""
            targetString = if (target == null) {
                "101"
            } else {
                target
            }
            //using an intent to navigate to another activity
            val intent = Intent(this, newgame::class.java)
            intent.putExtra("Target", targetString)
            intent.putExtra("Mode", hardMode)
            startActivity(intent)
        }

        //creating a button that will display a pop up window with details of the author
        val aboutButton = findViewById<Button>(R.id.about)
        aboutButton.setOnClickListener {
            val message = "" +
                    "Student ID: w1898953/20211379\n" +
                    "Student Name: Hiruni Perera\n" +
                    "\n" +
                    "I confirm that I understand what plagiarism is and have read and understood the section on Assessment Offences in the Essential Information for Students. The work that I have submitted is entirely my own. Any work from other authors is duly referenced and acknowledged."
            val title = "About"
            val dialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .create()
            dialog.show()
        }
    }

    /**
     *  Saves the game mode and target score when the activity is paused
     *
     * @param outState
     */
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("hardMode", hardMode)
        outState.putString("Target", target)
        super.onSaveInstanceState(outState)
    }

    /**
     * Restores the game mode and target score when the activity is resumed
     *
     * @param savedInstanceState
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        if (savedInstanceState != null) {
            super.onRestoreInstanceState(savedInstanceState)
            hardMode = savedInstanceState.getBoolean("hardMode")
            target = savedInstanceState.getString("Target").toString()
        }
    }

}