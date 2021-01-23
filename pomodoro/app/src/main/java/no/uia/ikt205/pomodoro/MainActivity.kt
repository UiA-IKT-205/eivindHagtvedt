package no.uia.ikt205.pomodoro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity() {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button

    lateinit var button_30:Button
    lateinit var button_60:Button
    lateinit var button_90:Button
    lateinit var button_120:Button
    lateinit var resetButton:Button

    lateinit var coutdownDisplay:TextView

    var timeToCountDownInMs = 1L
    val timeTicks = 1000L
    var clicked = false
    var reset = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       button_30 = findViewById<Button>(R.id.button_30)
       button_60 = findViewById<Button>(R.id.button_60)
       button_90 = findViewById<Button>(R.id.button_90)
       button_120 = findViewById<Button>(R.id.button_120)

       startButton = findViewById<Button>(R.id.startCountdownButton)

       startButton.setOnClickListener() {
           if (!clicked) {
               startCountDown(it)
               clicked = true
           }
       }

        button_30.setOnClickListener(){
            timeToCountDownInMs = 1800000L
            if (!clicked) {
                updateCountDownDisplay(1800000L)
            }
        }
        button_60.setOnClickListener(){
            timeToCountDownInMs = 3600000L
            if (!clicked) {
                updateCountDownDisplay(3600000L)
            }
        }
        button_90.setOnClickListener(){
            timeToCountDownInMs = 5400000L
            if (!clicked) {
                updateCountDownDisplay(5400000L)
            }
        }
        button_120.setOnClickListener(){
            timeToCountDownInMs = 7200000L
            if (!clicked) {
                updateCountDownDisplay(7200000L)
            }
        }

        resetButton.setOnClickListener(){
            if (clicked){
                reset = true
            }
        }

       coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }

    fun startCountDown(v: View){

        timer = object : CountDownTimer(timeToCountDownInMs,timeTicks) {
            override fun onFinish() {
                clicked = false
                reset = false
                Toast.makeText(this@MainActivity,"Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()

            }

            override fun onTick(millisUntilFinished: Long) {
               updateCountDownDisplay(millisUntilFinished)
            }
        }

        timer.start()
        }


    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }

}