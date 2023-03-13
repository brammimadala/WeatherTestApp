package com.brahma.weather.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.brahma.weather.R
import com.brahma.weather.data.model.ForeCastTimeModel
import com.brahma.weather.data.model.forecastWeather.FCList
import com.brahma.weather.databinding.FragmentWeatherDetailsBinding
import com.brahma.weather.presentation.adapter.ForecastWeatherDetailAdapter
import com.brahma.weather.presentation.viewModel.WeatherHomeViewModel
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import javax.inject.Inject


class WeatherDetailsFragment : Fragment() {

    private lateinit var binding: FragmentWeatherDetailsBinding

    @Inject
    lateinit var viewModel: WeatherHomeViewModel

    private var forecastTimeRCVList: MutableList<ForeCastTimeModel> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weather_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherDetailsBinding.bind(view)
        binding.root

        viewModel = (activity as MainActivity).viewModel

        viewModel.forecastDataReportModel.observe(viewLifecycleOwner, Observer { foreCastData ->

            val data: Map<String, FCList> = foreCastData.timeWeatherDataMap
            setData(data)
        })
    }

    private fun setData(data: Map<String, FCList>) {
        forecastTimeRCVList.clear()
        for (item in data) {
            val time: String = item.value.dtTxt.split(" ")[1]
            forecastTimeRCVList.add(ForeCastTimeModel(time, item.value))
        }

        forecastTimeRCVList.let {
            updateUI(forecastTimeRCVList[0])
            updateRecyclerViewUI(it)
        }

    }


    private fun updateRecyclerViewUI(forecastTimeRCVList: MutableList<ForeCastTimeModel>) {

        binding.foreCastTimeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter =
                ForecastWeatherDetailAdapter(context, forecastTimeRCVList) { foreCastTimeModel ->
                    updateUI(foreCastTimeModel)
                }
        }
    }

    private fun updateUI(fcm: ForeCastTimeModel) {
        val weather: com.brahma.weather.data.model.forecastWeather.Weather = fcm.FcData.weather[0]
        binding.apply {
            weatherCurrentTemp.text = "${fcm.FcData.main.temp}°"
            weatherMinMaxTemp.text = "${fcm.FcData.main.tempMin}°/${fcm.FcData.main.tempMax}°"

            weatherCloudsDescription.text = weather.description
            val imageUrl: String =
                "https://api.openweathermap.org/img/w/${weather.icon}"
            Glide.with(this@WeatherDetailsFragment)
                .load(imageUrl)
                .into(weatherCloudIv)

            weatherCloudsNumber.text = fcm.FcData.clouds.all.toString()
            weatherWindDirection.text = viewModel.getWindDirection(fcm.FcData.wind.deg)
            weatherWindSpeed.text = fcm.FcData.wind.speed.toString()
            weatherHumidity.text = "${fcm.FcData.main.humidity.toString()} %"
            weatherPressure.text = "${fcm.FcData.main.pressure}  hpa"
            weatherDate.text = getDateWithFormat(fcm.FcData.dtTxt)

        }
    }

    fun getDateWithFormat(date: String): String? {
        try {
            val dateFrmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val dateFrmt1 = SimpleDateFormat("E, dd MMM")
            val d1 = dateFrmt.parse(date)
            return dateFrmt1.format(d1)
        } catch (e: Exception) {
            return null
        }
    }

}