package com.brahma.weather.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.brahma.weather.databinding.ActivityMainBinding
import com.brahma.weather.presentation.viewModel.WeatherHomeViewModel
import com.brahma.weather.presentation.viewModel.WeatherViewModelFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var factory: WeatherViewModelFactory
    lateinit var viewModel: WeatherHomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, factory)[WeatherHomeViewModel::class.java]
    }


}