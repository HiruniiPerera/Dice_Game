package com.example.dicegame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //creating a button that will allow the user to start a new game
        val new_btn = findViewById<Button>(R.id.newgame)
        new_btn.setOnClickListener {

            //using an intent to navigate to another activity
            val intent_contact = Intent(this, newgame::class.java)
            startActivity(intent_contact)
        }

        //creating a button that will display a pop up window with details of the author
        val about_btn = findViewById<Button>(R.id.about)
        about_btn.setOnClickListener {
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
}