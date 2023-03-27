package com.example.dicegame

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import kotlin.random.Random

/**
 * Displays the game board where the user can play against the computer
 *
 */
class newgame : AppCompatActivity() {
    //throw counter
    private var throwCount = 0

    private var playerScore = 0 //player's score
    private var compScore = 0 //computer's score

    //Lists that holds the dices selected by the user to reroll
    private var rerollList1 = mutableListOf<Dice>()
    private var rerollList2 = mutableListOf<Dice>()

    //Text view that displays the score of the users
    private lateinit var scoreView: TextView

    //player's dices
    private lateinit var playerDice1: Dice
    private lateinit var playerDice2: Dice
    private lateinit var playerDice3: Dice
    private lateinit var playerDice4: Dice
    private lateinit var playerDice5: Dice

    //computer's dices
    private lateinit var computerDice1: Dice
    private lateinit var computerDice2: Dice
    private lateinit var computerDice3: Dice
    private lateinit var computerDice4: Dice
    private lateinit var computerDice5: Dice

    //adding the dices to lists
    private lateinit var playerDice: List<Dice>
    private lateinit var compDice: List<Dice>

    private var numberOfRerolls = Random.nextInt(1, 5)//number of dices the computer will reroll
    //this list holds the dices that the computer will reroll in the random strategy
    private lateinit var computerRerolls: List<Dice>

    private var diceValue = 0//current value of the dice
    private var rerollBoolean = false

    private var targetScore = 101//default target

    //score button
    private lateinit var scoreButton: Button
    //throw button
    private lateinit var throwButton: Button

    private var updatedScore = 0

    /**
     * sets the content view for the game board. Allows the user to throw the dice and then allows 2 rerolls.
     * The computer will either use random strategy or optimised strategy
     *
     * @param savedInstanceState
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newgame)
        //updates the total wins
        val wins = findViewById<TextView>(R.id.wins)
        wins.text = "H:" + (WinData.humanWins/2).toString() + "/C:" + (WinData.computerWins/2).toString()
        val intent = intent
        val target = intent.getStringExtra("Target")
        val hardMode = intent.getBooleanExtra("Mode", false)
        scoreView = findViewById<TextView>(R.id.scoreview)
        playerDice1 = Dice(this, findViewById(R.id.player_dice1)) //Initializing the dices
        playerDice2 = Dice(this, findViewById(R.id.player_dice2))
        playerDice3 = Dice(this, findViewById(R.id.player_dice3))
        playerDice4 = Dice(this, findViewById(R.id.player_dice4))
        playerDice5 = Dice(this, findViewById(R.id.player_dice5))

        computerDice1 = Dice(this, findViewById(R.id.computer_dice1))
        computerDice2 = Dice(this, findViewById(R.id.computer_dice2))
        computerDice3 = Dice(this, findViewById(R.id.computer_dice3))
        computerDice4 = Dice(this, findViewById(R.id.computer_dice4))
        computerDice5 = Dice(this, findViewById(R.id.computer_dice5))

        //adding dices to a list
        playerDice = listOf(playerDice1, playerDice2, playerDice3, playerDice4, playerDice5)
        compDice = listOf(computerDice1, computerDice2, computerDice3, computerDice4, computerDice5)

        //initializing buttons
        scoreButton = findViewById<Button>(R.id.score_button)
        throwButton = findViewById<Button>(R.id.throw_button)

        //disables score button
        scoreButton.isEnabled = false
        //action listener for throw button
        throwButton.setOnClickListener {
            if (throwCount == 0) {
                scoreButton.isEnabled = true         //when throw is pressed once, both computer dices and human dices are rolled
                playerDice.forEach { dice ->
                    dice.roll()
                }

                compDice.forEach() { dice ->
                    dice.roll()
                }

                playerDice.forEach { dice ->
                    val imageButton = dice.image                        // allows the user to select the dices that they want to keep
                    imageButton.setOnClickListener {
                        dice.changeToggle()
                        imageButton.setBackgroundColor(Color.RED)
                    }
                }
                throwCount++
            } else if (throwCount == 1) {                    // on the second roll, human dices selected are kept and others are rolled
                scoreButton.isEnabled = true
                playerDice.forEach { dice ->
                    if (!dice.getIsClicked()) {
                        rerollList1.add(dice)
                    }
                }
                rerollList1.forEach { dice ->
                    dice.roll()
                }
                playerDice.forEach { dice ->
                    dice.image.setBackgroundColor(Color.TRANSPARENT)   //changes the back ground colour once rerolled
                    if (dice.getIsClicked()) {
                        dice.changeToggle()
                    }
                }
                rerollList1.clear()
                playerDice.forEach { dice ->
                    val imagebtn = dice.image
                    imagebtn.setOnClickListener {
                        dice.changeToggle()
                        imagebtn.setBackgroundColor(Color.RED)
                    }
                }
                throwCount++
            } else if (throwCount == 2) {         // again the above meachanism of the second roll

                playerDice.forEach { dice ->
                    if (!dice.getIsClicked()) {
                        rerollList2.add(dice)
                    }
                }
                rerollList2.forEach { dice ->
                    dice.roll()
                }
                playerDice.forEach { dice ->
                    dice.image.setBackgroundColor(Color.TRANSPARENT)
                    if (dice.getIsClicked()) {
                        dice.changeToggle()
                    }
                }
                rerollList2.clear()             //selects the correct strategy
                if (hardMode) {
                    hardStrategy(compDice)
                    hardStrategy(compDice)
                } else if (!hardMode) {
                    easyStrategy(compDice)
                    easyStrategy(compDice)
                }
                playerScore = score(playerScore, playerDice)   //updates the scores
                compScore = score(compScore, compDice)
                updateScoreAndCheck(playerScore, compScore, target,wins)
                scoreButton.isEnabled = false                   //disables the score button
            }
        }
        //action listener for the score button
        scoreButton.setOnClickListener {


            playerScore = score(playerScore, playerDice)
            if (hardMode) {            //calls the correct strategy
                hardStrategy(compDice)
                hardStrategy(compDice)
            } else if (!hardMode) {
                easyStrategy(compDice)
                easyStrategy(compDice)
            }
            compScore = score(compScore, compDice) //updates the scores
            updateScoreAndCheck(playerScore, compScore, target,wins)

            scoreButton.isEnabled = false   //disables score button
            playerDice.forEach { dice ->
                dice.image.setBackgroundColor(Color.TRANSPARENT)
            }

        }
    }

    /**
     * This function is responsible for displaying the correct popups and the mechanism after it according to who wins the game
     * or if it's a tie
     *
     * @param wins TextView that displays the wins
     */
    private fun winOrLose(wins: TextView) {
        if (playerScore == targetScore && compScore == targetScore){
            tie(scoreButton,throwButton,wins)
        }else if(playerScore==compScore && playerScore>targetScore){
            tie(scoreButton,throwButton,wins)
        }
        else if (playerScore>= targetScore && compScore>= targetScore){
            if (playerScore>compScore){
                win(scoreButton,throwButton,wins)
            }else if(compScore>playerScore){
                lose(scoreButton,throwButton,wins)
            }
        }
        else if (playerScore>= targetScore && compScore< targetScore){
            win(scoreButton,throwButton,wins)
        }
        else if (playerScore< targetScore && compScore>= targetScore){
            lose(scoreButton,throwButton,wins)
        }
    }

    /**
     * This function updates the scores of both the player and computer.
     *  Also calls the method to check if the user won or not
     *
     * @param playerScore  player's current score
     * @param compScore computer's current score
     * @param target Target to achieve in order to win
     * @param wins TextView that displays the total wins of the human and computer
     */
    private fun updateScoreAndCheck(
        playerScore: Int,
        compScore: Int,
        target: String?,
        wins: TextView
    ) {

        scoreView.text = playerScore.toString() + "/" + compScore.toString()
        val targetToAchieve = target.toString().toIntOrNull()
        if (targetToAchieve != null) {
            targetScore = targetToAchieve
            winOrLose(wins)
        } else {
            winOrLose(wins)
        }
        winOrLose(wins)
        throwCount = 0
    }

    /**
     * This method gets the content description and adds it to the total, to get the total score
     *
     * @param score current score
     * @param diceset list containing dices
     * @return final score of the player or computer
     */
    private fun score(score: Int, diceset: List<Dice>): Int {
        val scorelist = HashMap<String, Int>()
        scorelist.put("1", 1)
        scorelist.put("2", 2)
        scorelist.put("3", 3)
        scorelist.put("4", 4)
        scorelist.put("5", 5)
        scorelist.put("6", 6)
        updatedScore = score
        diceset.forEach { dice ->
            updatedScore += scorelist.get(dice.image.contentDescription.toString()) ?: 0
        }
        return updatedScore
    }

    /**
     * This method shows a pop up if the user wins and disables the buttons which urges the user to go back to start a new game
     *
     * @param scorebutton
     * @param throwbutton
     * @param wins
     */
    @SuppressLint("MissingInflatedId")
    private fun win(scorebutton: Button, throwbutton: Button,wins:TextView) {
        WinData.humanWins++
        wins.text = "H:" + (WinData.humanWins/2).toString() + "/C:" + (WinData.computerWins/2).toString()
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

    /**
     * This method shows a pop up if the user loses and disables the buttons which urges the user to go back to start a new game
     *
     * @param scorebutton
     * @param throwbutton
     * @param wins
     */
    @SuppressLint("MissingInflatedId")
    private fun lose(scorebutton: Button, throwbutton: Button,wins: TextView) {
        WinData.computerWins++
        wins.text = "H:" + (WinData.humanWins/2).toString() + "/C:" + (WinData.computerWins/2).toString()
        scorebutton.isEnabled = false
        throwbutton.isEnabled = false
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.lose_dialog, null)
        val message = view.findViewById<TextView>(R.id.message)
        message.text = "You lose"
        val m2 = view.findViewById<TextView>(R.id.m2)
        m2.text = "Go back to start a new game"
        builder.setView(view)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
    }

    /**
     * This method shows a pop up if it's a tie and disables the score button as rerolls are not considered in this part
     * it allows the user to reroll until the tie is broken
     *
     * @param scorebutton
     * @param throwbutton
     * @param wins
     */
    private fun tie(scorebutton: Button, throwbutton: Button,wins: TextView) {
        throwButton.isEnabled = true
        scorebutton.isEnabled = false
        throwCount == 0
        val builder = AlertDialog.Builder(this)
        val view = LayoutInflater.from(this).inflate(R.layout.tie_dialog, null)
        val message = view.findViewById<TextView>(R.id.message)
        message.text = "It's a tie."
        val tie = view.findViewById<TextView>(R.id.tie)
        tie.text = "You have another chance to break the tie"
        builder.setView(view)
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val dialog = builder.create()
        dialog.show()
        throwbutton.setOnClickListener {
            playerDice.forEach { dice ->
                dice.roll()
            }

            compDice.forEach() { dice ->
                dice.roll()
            }
            //updating scores
            playerScore = score(playerScore, playerDice)
            compScore = score(compScore, compDice)

            if (playerScore > compScore) { //checking who wins
                win(scorebutton, throwbutton,wins)
            } else if (compScore > playerScore) {
                lose(scorebutton, throwbutton,wins)
            } else if (compScore == playerScore) {
                tie(scorebutton, throwbutton,wins)
            }
        }

    }

    /**
     * This is the random strategy.
     * computer decides randomly if they want to reroll or not
     * if they do randomly decides how many dices to roll again
     *
     * @param comp_dice List of computer dices
     */
    private fun easyStrategy(comp_dice: List<Dice>) {
        rerollBoolean = listOf(true, false).random()
        if (rerollBoolean) {
            computerRerolls= compDice.shuffled().take(numberOfRerolls)
            computerRerolls.forEach { dice ->
                dice.roll()
            }
        }
        throwCount = 0
    }

    /**
     * This is the optimised strategy.
     * This strategy checks the value of each dice,and if the value is less than 4 (can be 1,2,3), the dice will reroll.
     * The goal of this strategy is to increase the chances of getting higher values on the dice and
     *  any dice that has less than 4 is more likely to result in a total low score.
     *
     *  here is an example:
     *
     *  In the first throw for the computer,these are the values of the dice-
     *  Dice 1: 2
     *  Dice 2: 5
     *  Dice 3: 4
     *  Dice 4: 1
     *  Dice 5: 6
     *
     *  According to the optimised strategy, Dice 1 and Dice 3 will be rerolled
     *
     *  After the 1st reroll-
     *  Dice 1: 1
     *  Dice 2: 5
     *  Dice 3: 4
     *  Dice 4: 5
     *  Dice 5: 6
     *
     *  The above shows how the reroll may result in a lower or higher value. But overall there is a higher probability
     *  of getting a higher score while maintaining the current score
     *
     * Advantages
     * - The strategy is very easy to understand and implement,
     * - It is a fair strategy for the player, as it does not involve cheating or exploiting any loopholes.
     * - The strategy is relatively low risk, as it prioritizes keeping higher values and only rerolling lower values, reducing the chances of losing points.
     *
     * Disadvantages
     * - Even though it has a relatively low risk, there is still a risk of getting a lower value after rerolling
     * - If all the dices have a value of 4 or greater, then there will be no reroll
     *
     * @param computerDices List of computer dices
     */
    private fun hardStrategy(computerDices: List<Dice>) {
        computerDices.forEach { dice ->
            diceValue = dice.image.contentDescription.toString().toIntOrNull()!!
            if (diceValue == null) {
                diceValue = 0
            }
            if (diceValue < 4) {
                dice.roll()
            }
        }
        throwCount = 0
    }

    /**
     * Reference : GeeksforGeeks. (2021). How to Implement OnSavedInstanceState in Android?
     * Available at: https://www.geeksforgeeks.org/how-to-implement-on-saved-instance-state-in-android/
     * [Accessed 10 Mar. 2023].
     *
     */
    /**
     * saves the data when the activity is destroyed
     *
     * @param outState the bundle where the data is stored
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("throwcount", throwCount)
        outState.putInt("playerscore", playerScore)
        outState.putInt("compscore", compScore)
        outState.putString("Score", scoreView.text.toString())
        outState.putBoolean("scoreButtonState", scoreButton.isEnabled)
        outState.putBoolean("throwButtonState", throwButton.isEnabled)
        saveDiceList(outState, compDice, "computerValueList", "computerClickedState")
        saveDiceList(outState, rerollList1, "rerollList1", "reroll1states")
        saveDiceList(outState, rerollList2, "rerollList2", "reroll2states")

    }

    /**
     * restores the data when the activity is recreated
     *
     * @param savedInstanceState the bundle where the data is retrieved from
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        throwCount = savedInstanceState.getInt("throwcount")
        playerScore = savedInstanceState.getInt("playerscore")
        compScore = savedInstanceState.getInt("compscore")
        scoreView.text = savedInstanceState.get("Score") as CharSequence?
        scoreButton.isEnabled=savedInstanceState.getBoolean("scoreButtonState")
        throwButton.isEnabled= savedInstanceState.getBoolean("throwButtonState")
        throwButton.isEnabled

        restoreDiceList(savedInstanceState, playerDice, "playerValueList", "playerClickedState")
        restoreDiceList(savedInstanceState, compDice, "computerValueList", "computerClickedState")
        restoreDiceList(savedInstanceState, rerollList1, "rerollList1", "reroll1states")
        restoreDiceList(savedInstanceState, rerollList2, "rerollList2", "reroll2states")
        numberOfRerolls = Random.nextInt(1, 5)
    }

    /**
     * saves the dice list
     *
     * @param savedInstanceState bundle where the data is stored
     * @param diceList list of dices
     * @param key key string
     * @param clickedKey string key for the state
     */
    fun restoreDiceList(
        savedInstanceState: Bundle,
        diceList: List<Dice>,
        key: String,
        clickedKey: String
    ) {
        var listCount = 0
        val list = savedInstanceState.getSerializable(key) as? MutableList<Int>
        val clickedList = savedInstanceState.getSerializable(clickedKey) as? MutableList<Boolean>
        val imageResourceList = listOf<Int>(
            R.drawable.one,
            R.drawable.two,
            R.drawable.three,
            R.drawable.four,
            R.drawable.five,
            R.drawable.six
        )
        diceList.forEach { dice ->
            if (list != null) {
                dice.image.contentDescription = list.get(listCount).toString()
            }
            if (clickedList != null) {
                dice.setIsClicked(clickedList.get(listCount))
            }
            dice.image.setImageResource(imageResourceList.get(listCount))
            dice.image.scaleType = ImageView.ScaleType.FIT_CENTER
            listCount++
        }
    }

    /**
     * TODO
     *
     * @param outState bundle where data is stored
     * @param diceList list of dice
     * @param key string key
     * @param clickedKey key string for states
     */
    fun saveDiceList(outState: Bundle, diceList: List<Dice>, key: String,clickedKey:String) {
        val diceImageArray = ArrayList<Int>()
        val diceClickedState = ArrayList<Boolean>()
        diceList.forEach { dice ->
            val diceValue = dice.image?.contentDescription?.toString()?.toIntOrNull() ?: Random.nextInt(1, 7)
            diceImageArray.add(diceValue)
            diceClickedState.add(dice.getIsClicked())
        }

        outState.putSerializable(key, diceImageArray)
        outState.putSerializable(clickedKey,diceClickedState)
    }
}
