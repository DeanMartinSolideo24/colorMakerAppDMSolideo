package com.example.colormakerapp

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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    // Declare the views
    private lateinit var displayColorView: View
    private lateinit var redSeekBar: SeekBar
    private lateinit var greenSeekBar: SeekBar
    private lateinit var blueSeekBar: SeekBar
    private lateinit var redSwitch: Switch
    private lateinit var greenSwitch: Switch
    private lateinit var blueSwitch: Switch
    private lateinit var resetButton: Button
    private lateinit var redValueTextView: TextView
    private lateinit var greenValueTextView: TextView
    private lateinit var blueValueTextView: TextView

    // Define the default starting values
    private val defaultRedValue = 0f
    private val defaultGreenValue = 0f
    private val defaultBlueValue = 0f

    private lateinit var viewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize UI components
        initializeViews()

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        // Observe and restore persisted values
        observePersistedValues()

        // Setup listeners
        setupSeekBarListeners()
        setupSwitchListeners()
        setupResetButton()

        // Observe color changes
        viewModel.color.observe(this) { newColor ->
            displayColorView.setBackgroundColor(newColor)
        }

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Helper functions for setting up UI components
    private fun initializeViews() {
        displayColorView = findViewById(R.id.displayColor)
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
    }

    private fun observePersistedValues() { // Observe persisted values
        lifecycleScope.launch {
            viewModel.redValue.collect { red ->
                redSeekBar.progress = (red * 100).toInt()
            }
            viewModel.greenValue.collect { green ->
                greenSeekBar.progress = (green * 100).toInt()
            }
            viewModel.blueValue.collect { blue ->
                blueSeekBar.progress = (blue * 100).toInt()
            }
            viewModel.redSwitchState.collect { checked ->
                redSwitch.isChecked = checked
            }
            viewModel.greenSwitchState.collect { checked ->
                greenSwitch.isChecked = checked
            }
            viewModel.blueSwitchState.collect { checked ->
                blueSwitch.isChecked = checked
            }
        }
    }

    private fun setupSeekBarListeners() { // SeekBar listeners.
        val seekBarChangeListener = object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateColorValues()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }

        redSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        greenSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
        blueSeekBar.setOnSeekBarChangeListener(seekBarChangeListener)
    }

    // Switch listeners.
    private fun setupSwitchListeners() {
        val switchChangeListener = CompoundButton.OnCheckedChangeListener { _, _ ->
            updateColorValues()
        }

        redSwitch.setOnCheckedChangeListener(switchChangeListener)
        greenSwitch.setOnCheckedChangeListener(switchChangeListener)
        blueSwitch.setOnCheckedChangeListener(switchChangeListener)
    }

    // Reset button listener.
    private fun setupResetButton() {
        resetButton.setOnClickListener {
            resetToDefault()
        }
    }

    // Update color values based on seek bar and switch values.
    private fun updateColorValues() {
        val redValue = redSeekBar.progress / 100f
        val greenValue = greenSeekBar.progress / 100f
        val blueValue = blueSeekBar.progress / 100f

        viewModel.updateColor(
            redValue,
            greenValue,
            blueValue,
            redSwitch.isChecked,
            greenSwitch.isChecked,
            blueSwitch.isChecked
        )

        updateValueTextViews(redValue, greenValue, blueValue)
    }

    private fun resetToDefault() { // Reset to default values.
        redSwitch.isChecked = true
        greenSwitch.isChecked = true
        blueSwitch.isChecked = true

        redSeekBar.progress = (defaultRedValue * 100).toInt()
        greenSeekBar.progress = (defaultGreenValue * 100).toInt()
        blueSeekBar.progress = (defaultBlueValue * 100).toInt()

        updateColorValues()
    }

    // Update the text views with the current color values.
    private fun updateValueTextViews(red: Float, green: Float, blue: Float) {
        redValueTextView.text = String.format("%.2f", red)
        greenValueTextView.text = String.format("%.2f", green)
        blueValueTextView.text = String.format("%.2f", blue)
    }
}