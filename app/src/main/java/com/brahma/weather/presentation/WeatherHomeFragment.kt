package com.brahma.weather.presentation

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.brahma.weather.R
import com.brahma.weather.data.model.ForecastDataReportModel
import com.brahma.weather.data.model.currentWeather.Weather
import com.brahma.weather.data.model.currentWeather.WeatherCurrentModel
import com.brahma.weather.data.model.forecastWeather.FCList
import com.brahma.weather.data.model.forecastWeather.FweatherReport
import com.brahma.weather.data.util.Resource
import com.brahma.weather.databinding.FragmentWeatherHomeBinding
import com.brahma.weather.presentation.adapter.WeatherHomeDatesAdapter
import com.brahma.weather.presentation.viewModel.WeatherHomeViewModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class WeatherHomeFragment : Fragment() {


    private lateinit var binding: FragmentWeatherHomeBinding
    private var lat: String? = null
    private var lng: String? = null

    @Inject
    lateinit var viewModel: WeatherHomeViewModel

    private val foreCastWeatherMap: MutableMap<String, MutableMap<String, FCList>> = mutableMapOf()

    private var forecastRCVList: MutableList<ForecastDataReportModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherHomeBinding.bind(view)
        binding.root

        viewModel = (activity as MainActivity).viewModel

        binding.ivMap.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_weather_home_fragment_to_map_fragment)
        }

        binding.ivLocations.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_weather_home_fragment_to_locations_fragment)
        }

        val sp = this.requireActivity().getSharedPreferences("pref", MODE_PRIVATE)
        lat = sp.getString("latitude", null)
        lng = sp.getString("longitude", null)

        if (lat != null && lng != null) {
            weatherDetails(lat!!, lng!!)
            forecastWeatherDetails(lat!!, lng!!)
        } else {
            view.let {
                Navigation.findNavController(it)
                    .navigate(R.id.action_weather_home_fragment_to_map_fragment)
            }
        }

    }

    private fun weatherDetails(latitude: String, longitude: String) {
        showProgress()
        viewModel.getCurrentWeather(latitude, longitude)
        viewModel.currentWeather.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.i("WeatherData===>", "Loading...")
                }
                is Resource.Success -> {
                    response.data?.let {
                        updateUI(it)
                        dismissProgress()
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(activity, "error occurred $it", Toast.LENGTH_LONG).show()
                        dismissProgress()
                    }
                }
            }
        }
    }

    private fun showProgress() {
        if (!isProgress()) {
            binding.progressbar.visibility = View.VISIBLE
        }
    }

    private fun dismissProgress() {
        if (isProgress()) {
            binding.progressbar.visibility = View.INVISIBLE
        }
    }

    private fun isProgress(): Boolean {
        return binding.progressbar.isShown
    }


    private fun forecastWeatherDetails(latitude: String, longitude: String) {
        showProgress()
        viewModel.getForecastWeather(latitude, longitude)
        viewModel.forecastWeather.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    Log.i("ForecastWeatherData===>", "Loading...")
                }
                is Resource.Success -> {
                    response.data?.let {
                        setData(it)
                        dismissProgress()
                    }
                }
                is Resource.Error -> {
                    response.message?.let {
                        Toast.makeText(activity, "error occurred $it", Toast.LENGTH_LONG).show()
                        dismissProgress()
                    }
                }
            }
        }
    }

    private fun setData(forecastWeatherReport: FweatherReport) {
        foreCastWeatherMap.clear()
        for (forecastList in forecastWeatherReport.list) {
            val date: String = forecastList.dtTxt.split(" ")[0]

            if (foreCastWeatherMap.containsKey(date)) {
                foreCastWeatherMap[date]?.put(forecastList.dtTxt, forecastList)
            } else {
                val mutableSubMap: MutableMap<String, FCList> = mutableMapOf()
                mutableSubMap[forecastList.dtTxt] = forecastList
                foreCastWeatherMap[date] = mutableSubMap
            }
        }
        forecastRCVList.clear()
        if (foreCastWeatherMap.isNotEmpty()) {
            for (key in foreCastWeatherMap.keys) {
                val date: String = key
                val dat = foreCastWeatherMap[key]
                val fcModel = dat?.let { ForecastDataReportModel(date, it) }
                fcModel?.let { forecastRCVList.add(it) }
            }
        }

        updateRecyclerViewUI(forecastRCVList)
    }

    private fun updateRecyclerViewUI(forecastWeatherReport: MutableList<ForecastDataReportModel>) {
        binding.foreCastRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = WeatherHomeDatesAdapter(forecastWeatherReport) { forecastDataReportModel ->
                viewModel.setDataForDetailsFrag(forecastDataReportModel)
                view?.let {
                    Navigation.findNavController(it)
                        .navigate(R.id.action_weather_home_fragment_to_weather_details_fragment)
                }
            }
        }
    }


    private fun updateUI(weatherData: WeatherCurrentModel) {
        val weather: Weather = weatherData.weather[0]
        binding.apply {
            weatherLocationName.text = weatherData.name
            weatherCurrentTemp.text = "${weatherData.main.temp}°"
            weatherMinMaxTemp.text = "${weatherData.main.tempMin}°/${weatherData.main.tempMax}°"

            weatherCloudsDescription.text = weather.description
            val imageUrl: String =
                "https://api.openweathermap.org/img/w/${weather.icon}"
            Glide.with(this@WeatherHomeFragment)
                .load(imageUrl)
                .into(weatherCloudIv)

            weatherCloudsNumber.text = weatherData.clouds.all.toString()
            weatherWindDirection.text = viewModel.getWindDirection(weatherData.wind.deg)
            weatherWindSpeed.text = weatherData.wind.speed.toString()
            weatherHumidity.text = "${weatherData.main.humidity} %"
            weatherPressure.text = "${weatherData.main.pressure}  hpa"
            val dateFormat = SimpleDateFormat("h:m:s a")
            weatherSunrise.text = "${dateFormat.format(Date(weatherData.sys.sunrise * 1000))}"
            weatherSunset.text = "${dateFormat.format(Date(weatherData.sys.sunset * 1000))}"

        }
    }
}