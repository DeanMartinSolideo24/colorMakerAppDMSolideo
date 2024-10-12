package com.example.colormakerapp

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CompoundButton
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // Declare the views
    lateinit var displayColorView: View
    lateinit var redSeekBar: SeekBar
    lateinit var greenSeekBar: SeekBar
    lateinit var blueSeekBar: SeekBar
    lateinit var redSwitch: Switch
    lateinit var greenSwitch: Switch
    lateinit var blueSwitch: Switch
    lateinit var resetButton: Button
    lateinit var redValueTextView: TextView
    lateinit var greenValueTextView: TextView
    lateinit var blueValueTextView: TextView

    // Define the default starting values
    private val defaultRedValue = 0f
    private val defaultGreenValue = 0f
    private val defaultBlueValue = 0f


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayColorView = findViewById(R.id.displayColor)         // Initialize Components
        redSeekBar = findViewById(R.id.seekbarRed)
        greenSeekBar = findViewById(R.id.seekbarGreen)
        blueSeekBar = findViewById(R.id.seekbarBlue)
        redSwitch = findViewById(R.id.redSwitch)
        greenSwitch = findViewById(R.id.greenSwitch)
        blueSwitch = findViewById(R.id.blueSwitch)
        resetButton = findViewById(R.id.resetBtn)
        redValueTextView = findViewById(R.id.redValue)
        greenValueTextView = findViewById(R.id.greenValue)
        blueValueTextView = findViewById(R.id.blueValue)

        setupSeekBarListeners()
        setupSwitchListeners()

        resetButton.setOnClickListener {         // Reset button function
            resetToDefault()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupSeekBarListeners() {
        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val redValue = if (redSwitch.isChecked) redSeekBar.progress / 100f else 0f
                val greenValue = if (greenSwitch.isChecked) greenSeekBar.progress / 100f else 0f
                val blueValue = if (blueSwitch.isChecked) blueSeekBar.progress / 100f else 0f

                updateDisplayColor(redValue, greenValue, blueValue)
                updateValueTextViews(redValue, greenValue, blueValue)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    private fun setupSwitchListeners() {
        val switchChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
            val redValue = if (redSwitch.isChecked) redSeekBar.progress / 100f else 0f
            val greenValue = if (greenSwitch.isChecked) greenSeekBar.progress / 100f else 0f
            val blueValue = if (blueSwitch.isChecked) blueSeekBar.progress / 100f else 0f

            updateDisplayColor(redValue, greenValue, blueValue)
            updateValueTextViews(redValue, greenValue, blueValue)
        }

        redSwitch.setOnCheckedChangeListener(switchChangeListener)
        greenSwitch.setOnCheckedChangeListener(switchChangeListener)
        blueSwitch.setOnCheckedChangeListener(switchChangeListener)
    }

    // Function to reset to default color values and switch states
    private fun resetToDefault() {
        // Reset the switches
        redSwitch.isChecked = true
        greenSwitch.isChecked = true
        blueSwitch.isChecked = true

        // Reset the seekbars to default values
        redSeekBar.progress = (defaultRedValue).toInt()
        greenSeekBar.progress = (defaultGreenValue).toInt()
        blueSeekBar.progress = (defaultBlueValue).toInt()

        // Re-enable the seekbars
        redSeekBar.isEnabled = true
        greenSeekBar.isEnabled = true
        blueSeekBar.isEnabled = true

        // Reset the display color to default
        updateDisplayColor(defaultRedValue, defaultGreenValue, defaultBlueValue)
        updateValueTextViews(defaultRedValue, defaultGreenValue, defaultBlueValue)
    }

    // Function to update the display color
    private fun updateDisplayColor(red: Float, green: Float, blue: Float) {
        val color = Color.rgb((red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt())
        displayColorView.setBackgroundColor(color)
    }

    private fun updateValueTextViews(red: Float, green: Float, blue: Float) {
        redValueTextView.text = String.format("%.2f", red)
        greenValueTextView.text = String.format("%.2f", green)
        blueValueTextView.text = String.format("%.2f", blue)
    }
}
