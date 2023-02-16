package com.mdev.week3_weeklyactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity(), Chronometer.OnChronometerTickListener {

    lateinit var stopwatch : Chronometer // The stopwatch
    var running = false                  // Is the stopwatch running?
    var offset : Long = 0                // The base offset for the stopwatch

    // Key string for use with the Bundle
    val OFFSET_KEY = "offset"
    val RUNNING_KEY = "running"
    val BASE_KEY = "base"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Get a reference to the stopwatch
        stopwatch = findViewById<Chronometer>(R.id.stopwatch)
        stopwatch.onChronometerTickListener = this

//         Restore the previous state
        if(savedInstanceState != null) {
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if (running) {
                stopwatch.setCountDown(true)
                stopwatch.base = savedInstanceState.getLong((BASE_KEY))
                stopwatch.start()
            }
        }

        // Define what each button does
        // Start Button
        val startButton = findViewById<Button>(R.id.start_button)
        startButton.setOnClickListener {
            if(!running) {
                stopwatch.setCountDown(true)
                stopwatch.start()
                running = true
            }
        }

        // Pause Button
        val pauseButton = findViewById<Button>(R.id.pause_button)
        pauseButton.setOnClickListener {
            if (running) {
                saveOffset()
                stopwatch.stop()
                running = false
            }
        }

        // Reset Button
        val resetButton = findViewById<Button>(R.id.reset_button)
        resetButton.setOnClickListener {
            setBaseTime(0)
        }

        val plusOne = findViewById<Button>(R.id.increment_one)
        plusOne.setOnClickListener {
            // Increament the number by one
            setBaseTime(1)
        }

        val plusFive = findViewById<Button>(R.id.increment_five)
        plusFive.setOnClickListener {
            // Increament the number by one
            setBaseTime(5)
        }

        val plusTen = findViewById<Button>(R.id.increment_ten)
        plusTen.setOnClickListener {
            // Increament the number by one
            setBaseTime(10)
        }

    }

    override fun onChronometerTick(chronometer: Chronometer) {
        // Do whatever when the chronometer achieves some time:
        when (chronometer.text) {
            "00:00" -> {
                stopwatch.stop()
                running = false
            }
        }
        println(chronometer.text)
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putLong(OFFSET_KEY, offset)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(BASE_KEY, stopwatch.base)

        super.onSaveInstanceState(outState)
    }

    override fun onPause() {
        super.onPause()
        if(running) {
            saveOffset()
            stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()

        if (running) {
            resetBaseTime()
            stopwatch.start()
            offset = 0
        }
    }

    fun resetBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() + offset
    }

    // Update the stopwatch.base time, allowing for any offset
    fun setBaseTime(count: Int) {
        stopwatch.base = SystemClock.elapsedRealtime() + count * 60 * 1000
        println(stopwatch.base)
    }

    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() + stopwatch.base
    }
 }