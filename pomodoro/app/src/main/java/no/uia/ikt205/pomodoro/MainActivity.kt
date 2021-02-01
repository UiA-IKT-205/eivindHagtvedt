 package no.uia.ikt205.pomodoro

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import android.widget.SeekBar.OnSeekBarChangeListener
import no.uia.ikt205.pomodoro.util.millisecondsToDescriptiveTime

class MainActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var timer:CountDownTimer
    lateinit var startButton:Button
    lateinit var resetButton:Button

    lateinit var setWorktimeDurationSeekBar:SeekBar
    lateinit var setPauseTimeDurationSeekBar:SeekBar

    lateinit var coutdownDisplay:TextView
    lateinit var workTimeDurationDisplay:TextView
    lateinit var pauseTimeDurationDisplay:TextView
    lateinit var pauseOrWorkDisplay:TextView

    lateinit var writeRepetition:EditText

    val minute = 60000L
    var timeToCountDownInMs = 0L
    var workDuration = 15 * minute
    var pauseDuration = 0L
    val timeTicks = 1000L
    var numberOfRepetitions = 1
    var timeForWork = true
    var startButtonClicked = false
    var counter = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       setWorktimeDurationSeekBar = findViewById<SeekBar>(R.id.setWorktimeDurationSeekBar)
       setPauseTimeDurationSeekBar = findViewById<SeekBar>(R.id.setPauseTimeDurationSeekBar)

       resetButton = findViewById<Button>(R.id.resetButton)
       startButton = findViewById<Button>(R.id.startCountdownButton)

       workTimeDurationDisplay = findViewById<TextView>(R.id.workTimeDuration)
       pauseTimeDurationDisplay = findViewById<TextView>(R.id.pauseTimeDuration)
       pauseOrWorkDisplay = findViewById<TextView>(R.id.pauseOrWorkDisplay)
       writeRepetition = findViewById<EditText>(R.id.writeRepetition)

        writeRepetition.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable) {

            }
            override fun beforeTextChanged(s: CharSequence, start:Int, count: Int, after: Int){

            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int){
                numberOfRepetitions = try {
                    writeRepetition.text.toString().toInt()
                } catch(nfe: NumberFormatException){
                    android.widget.Toast.makeText(this@MainActivity, "Not an integer", android.widget.Toast.LENGTH_SHORT).show()
                    0
                }
            }
        })

        //setting the work time duration
        setWorktimeDurationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (!startButtonClicked) {
                    workDuration = setWorktimeDurationSeekBar.progress.toLong() * minute
                    workTimeDurationDisplay.text = millisecondsToDescriptiveTime(workDuration)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Toast.makeText(this@MainActivity, "Smooth seekbar current progress ${setWorktimeDurationSeekBar.progress}", Toast.LENGTH_SHORT).show()
            }
        })


        //setting the pause time duration
        setPauseTimeDurationSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                if (!startButtonClicked) {
                    pauseDuration = setPauseTimeDurationSeekBar.progress.toLong() * minute
                    pauseTimeDurationDisplay.text = millisecondsToDescriptiveTime(pauseDuration)

                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {

            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Toast.makeText(this@MainActivity, "Smooth seekbar current progress ${setPauseTimeDurationSeekBar.progress}", Toast.LENGTH_SHORT).show()
            }
        })

       startButton.setOnClickListener() {
           if (!startButtonClicked) {
               startButtonClicked = true
               timeForWork = true
               counter = 1
               timeToCountDownInMs = workDuration
               pauseOrWorkDisplay.text = "Arbeidsøkt"
               startCountDown()
           }
       }

        resetButton.setOnClickListener(this)


       coutdownDisplay = findViewById<TextView>(R.id.countDownView)

    }

    fun startCountDown() {

        if (numberOfRepetitions == 0){
            startButtonClicked = false
            counter = 0
            Toast.makeText(this@MainActivity, "input invalid", Toast.LENGTH_SHORT).show()
            pauseOrWorkDisplay.text = ""

        }
        else {
            timer = object : CountDownTimer(timeToCountDownInMs, timeTicks) {
                @SuppressLint("SetTextI18n")
                override fun onFinish() {

                    if (counter == numberOfRepetitions * 2 - 1) {
                        startButtonClicked = false
                        counter = 0
                        Toast.makeText(this@MainActivity, "Siste arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                        pauseOrWorkDisplay.text = ""


                    } else if (timeForWork) {
                        Toast.makeText(this@MainActivity, "Arbeidsøkt er ferdig", Toast.LENGTH_SHORT).show()
                        timeToCountDownInMs = pauseDuration
                        timeForWork = false
                        counter += 1
                        pauseOrWorkDisplay.text = "Pause"
                        startCountDown()
                    } else {
                        Toast.makeText(this@MainActivity, "Back to work", Toast.LENGTH_SHORT).show()
                        timeToCountDownInMs = workDuration
                        timeForWork = true
                        counter += 1
                        pauseOrWorkDisplay.text = "Arbeidsøkt"
                        startCountDown()

                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    updateCountDownDisplay(millisUntilFinished)
                }
            }
            timer.start()
        }
    }

    fun ifResetButtonClicked(){
        if(startButtonClicked){
            timer.cancel()
            startButtonClicked = false
        }
    }

    override fun onClick(view:View){
        ifResetButtonClicked()
        when (view) {
            resetButton -> {
                timeToCountDownInMs = 0
            }

        }
        updateCountDownDisplay(timeToCountDownInMs)
    }

    fun updateCountDownDisplay(timeInMs:Long){
        coutdownDisplay.text = millisecondsToDescriptiveTime(timeInMs)
    }



}