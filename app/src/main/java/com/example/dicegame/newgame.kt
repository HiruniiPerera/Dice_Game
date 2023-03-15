package com.example.dicegame

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class newgame : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newgame)
        var playerscore = 0
        var compscore = 0

        val targetscoreview = findViewById<EditText>(R.id.edittarget)

        val p_dice1 = Dice(this, findViewById(R.id.player_dice1))
        val p_dice2 = Dice(this, findViewById(R.id.player_dice2))
        val p_dice3 = Dice(this, findViewById(R.id.player_dice3))
        val p_dice4 = Dice(this, findViewById(R.id.player_dice4))
        val p_dice5 = Dice(this, findViewById(R.id.player_dice5))

        val playerdice = listOf(p_dice1, p_dice2, p_dice3, p_dice4, p_dice5)

        val c_dice1 = Dice(this, findViewById(R.id.computer_dice1))
        val c_dice2 = Dice(this, findViewById(R.id.computer_dice2))
        val c_dice3 = Dice(this, findViewById(R.id.computer_dice3))
        val c_dice4 = Dice(this, findViewById(R.id.computer_dice4))
        val c_dice5 = Dice(this, findViewById(R.id.computer_dice5))

        val comp_dice = listOf(c_dice1, c_dice2, c_dice3, c_dice4, c_dice5)

        val scorebutton = findViewById<Button>(R.id.score_button)
        val throwbutton = findViewById<Button>(R.id.throw_button)

        throwbutton.setOnClickListener {
            playerdice.forEach { dice ->
                dice.roll()
            }
            comp_dice.forEach() { dice ->
                dice.roll()
            }
        }
        scorebutton.setOnClickListener {
            playerscore=score(playerscore,playerdice)
            compscore=score(compscore,comp_dice)
            val player_scoreview = findViewById<TextView>(R.id.playerscore)
            player_scoreview.text = playerscore.toString()
            val comp_scoreview= findViewById<TextView>(R.id.compscore)
            comp_scoreview.text = compscore.toString()
            val targetscorestring = targetscoreview.text.toString()
            val targetscore = targetscorestring.toIntOrNull()
            if (targetscore != null) {
                win_lose(playerscore,compscore,targetscore,scorebutton,throwbutton)
            } else {
                win_lose(playerscore,compscore,101,scorebutton,throwbutton)
            }
        }
    }


    /**
     * TODO
     *
     * @param playerscore
     * @param comp_score
     * @param targetscore
     * @param scorebutton
     * @param throwbutton
     */
    private fun win_lose(playerscore: Int,comp_score:Int, targetscore: Int,scorebutton:Button,throwbutton:Button){
        if (playerscore == targetscore && comp_score == targetscore){}
        else if (playerscore>= targetscore && comp_score>= targetscore){
            if (playerscore>comp_score){
                win(scorebutton,throwbutton)
            }else if(comp_score>playerscore){
                lose(scorebutton,throwbutton)
            }
        }
        else if (playerscore>= targetscore && comp_score< targetscore){
            win(scorebutton,throwbutton)
        }
        else if (playerscore< targetscore && comp_score>= targetscore){
            lose(scorebutton,throwbutton)
        }
    }

    private fun score(score: Int, diceset: List<Dice>): Int {
        val scorelist = HashMap<String, Int>()
        scorelist.put("1", 1)
        scorelist.put("2", 2)
        scorelist.put("3", 3)
        scorelist.put("4", 4)
        scorelist.put("5", 5)
        scorelist.put("6", 6)
        var updatedScore = score
        diceset.forEach { dice ->
            updatedScore += scorelist.get(dice.image.contentDescription.toString()) ?: 0
        }
        return updatedScore
    }

    @SuppressLint("MissingInflatedId")
    private fun win(scorebutton:Button,throwbutton:Button){
        scorebutton.isEnabled = false
        throwbutton.isEnabled = false
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.win_dialog, null)
        val message = view.findViewById<TextView>(R.id.message)
        message.text = "You win!"
        val m2 = view.findViewById<TextView>(R.id.m2)
        m2.text = "Go back to start a new game"
        builder.setView(view)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }
    @SuppressLint("MissingInflatedId")
    private fun lose(scorebutton:Button,throwbutton:Button){
        scorebutton.isEnabled = false
        throwbutton.isEnabled = false
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.lose_dialog, null)
        val message = view.findViewById<TextView>(R.id.message)
        message.text = "You lose!"
        val m2 = view.findViewById<TextView>(R.id.m2)
        m2.text = "Go back to start a new game"
        builder.setView(view)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }
}
