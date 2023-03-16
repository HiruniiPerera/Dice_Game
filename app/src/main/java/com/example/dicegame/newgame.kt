package com.example.dicegame

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class newgame : AppCompatActivity() {
    var throwcount = 0
    var playerscore = 0
    var compscore = 0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newgame)

        val intent = intent
        val target = intent.getStringExtra("Target")
        val scoreview = findViewById<TextView>(R.id.scoreview)
      //  val targetscoreview = findViewById<EditText>(R.id.edittarget)

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
            if (throwcount == 0) {
                playerdice.forEach { dice ->
                    dice.roll()
                }

                comp_dice.forEach() { dice ->
                    dice.roll()
                }
            }else if (throwcount == 1) {
                var rerolllist = mutableListOf<Dice>()
                playerdice.forEach { dice ->
                    val imagebtn = dice.image
                    var isclicked = false
                    imagebtn.setOnClickListener {
                        var isclicked = true
                        imagebtn.setBackgroundColor(Color.RED)
                    }
                    if(isclicked==false){
                        rerolllist.add(dice)
                    }
                }
                rerolllist.forEach{dice ->
                    dice.roll()
                }
            }else if (throwcount == 2) {
                playerdice.forEach { dice ->
                    //TODO
                    //Add selection for buttons

                    dice.roll()
                }
                throwcount == 0
                playerscore = score(playerscore, playerdice)
                compscore = score(compscore, comp_dice)
                updateScoreAndCheck(playerscore,compscore,playerdice,scoreview,target,comp_dice,scorebutton,throwbutton)

                //Method for computer to play rerolls based on its strategy

            }


            throwcount++
        }


        scorebutton.setOnClickListener {
            throwcount == 0
            playerscore = score(playerscore, playerdice)
            compscore = score(compscore, comp_dice)
           updateScoreAndCheck(playerscore,compscore,playerdice,scoreview,target,comp_dice,scorebutton,throwbutton)
            //Method for computer to play rerolls based on its strategy
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

    private fun updateScoreAndCheck(playerscore:Int,compscore:Int,playerdice:List<Dice>,scoreview:TextView,target:String?,comp_dice:List<Dice>,scorebutton: Button,throwbutton: Button){

        scoreview.text = playerscore.toString() + "/" + compscore.toString()
        val targetscore = target.toString().toIntOrNull(
        )
        if (targetscore != null) {
            win_lose(playerscore, compscore, targetscore, scorebutton, throwbutton)
        } else {
            win_lose(playerscore, compscore, 101, scorebutton, throwbutton)
        }
        throwcount = 0

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

    private fun reroll(){

    }
}
