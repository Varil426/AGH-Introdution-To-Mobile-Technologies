package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    private var score = 0
    private var gameStarted = false
    private lateinit var timer : CountDownTimer

    internal lateinit var button: Button
    internal lateinit var scoreLabel: TextView
    internal lateinit var timeLabel: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.mainButton)
        scoreLabel = findViewById(R.id.scoreLabel)
        timeLabel = findViewById(R.id.timeLeftLabel)

        initValues()

        button.setOnClickListener {_ -> buttonClicked()}
        timer = object : CountDownTimer(15000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                setTimeLabel(millisUntilFinished/1000)
            }

            override fun onFinish() {
                gameOver()
            }
        }
    }

    private fun gameOver() {
        val text = getString(R.string.gameOver, score)
        Toast.makeText(this@MainActivity, text, Toast.LENGTH_LONG).show()
        score = 0
        var buttonText = getString(R.string.buttonLabel)
        button.text = buttonText
        gameStarted = false
    }

    private fun setTimeLabel(secondsLeft: Long) {
        val timeText = getString(R.string.TimeLeftString, secondsLeft)
        timeLabel.text = timeText
    }

    private fun initValues() {
        val scoreText = getString(R.string.ScoreString, 0)
        val timeText = getString(R.string.TimeLeftString, 0)
        scoreLabel.text = scoreText
        timeLabel.text = timeText
    }

    private fun buttonClicked() {
        if (!gameStarted) {
            gameStarted = true
            button.text = "Tap me!"
            timer.start()
        } else {
            score += 1
            val text = getString(R.string.ScoreString, score)
            scoreLabel.text = text
        }
    }
}