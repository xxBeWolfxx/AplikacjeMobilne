package com.example.asystentoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.responseObject
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.fragment_nav_myday.*
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ResponseModel(
    @SerializedName("title")
    val title: String,
    @SerializedName("consolidated_weather")
    val forecasts: List<Weather>
)

class Weather(
    @SerializedName("weather_state_abbr")
    val code: String,
    @SerializedName("min_temp")
    val minTemp: Double,
    @SerializedName("max_temp")
    val maxTemp: Double,
    @SerializedName("wind_speed")
    val windSpeed: Double,
    @SerializedName("wind_direction")
    val windDirection: Double,
    @SerializedName("air_pressure")
    val airPressure: Double
)

class WeatherViewModel() : ViewModel() {
    val isProgress = MutableLiveData(false)
    val response = MutableLiveData<ResponseModel>()
    val error = MutableLiveData<Throwable>()
    init {
        val url = "https://www.metaweather.com/api/location/523920/"

        // latt: 52.4144
        //long: 16.9211
        ///api/location/search/?lattlong=36.96,-122.02

        isProgress.value = true
        Fuel.get(url)
            .responseObject<ResponseModel> { _, _, result ->
                isProgress.value = false
                result.fold({
                    response.value = it
                }, {
                    error.value = it.exception
                })
            }
    }
}
/**
 * A simple [Fragment] subclass.
 * Use the [nav_mydayFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class nav_mydayFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private val viewModel by lazy {
        ViewModelProvider(this).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        viewModel.isProgress.observe(this, Observer { show ->
            progressView.visibility = if (show)
                View.VISIBLE
            else
                View.GONE
        })
        viewModel.isProgress.observe(this, Observer {
            showProgress(it)
        })
        viewModel.response.observe(this, Observer {
            showResult(it)
        })
        viewModel.error.observe(this, Observer {
            if (it == null)
                return@Observer
            showError(it)
            viewModel.error.value = null
        })

    }




private fun showError(error: Throwable) {
    containerView.visibility = View.GONE
    Snackbar.make(
        mainView,
        "Error: ${error.message}",
        Snackbar.LENGTH_SHORT
    ).show()
}

private fun showProgress(show: Boolean) {
    progressView.visibility = if (show)
        View.VISIBLE
    else
        View.GONE
}

private fun showResult(model: ResponseModel) {
    containerView.visibility = View.VISIBLE
    val weather = model.forecasts.first()
    val iconUrl =
        "https://www.metaweather.com/static/img/weather/png/${weather.code}.png"
    Glide.with(this)
        .load(iconUrl)
        .into(iconWeather)
    textLocation.text = model.title
    textTempMax.text = "${weather.maxTemp.roundToInt()}°"
    textTempMin.text = "${weather.minTemp.roundToInt()}°"
    textAirPressure.text = "${weather.airPressure.roundToInt()} hPa"
    textWindSpeed.text = "${(weather.windSpeed * 2.6).roundToInt()} km/h"
    iconWind.rotation = weather.windDirection.toFloat()
}


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nav_myday, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment nav_mydayFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            nav_mydayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}