package com.example.asystentoro.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.example.asystentoro.MyApplication
import com.example.asystentoro.R

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    var view_weather: ImageView? = null
    var view_city: TextView? = null
    var view_temp: TextView? = null
    var view_press: TextView? = null
    var view_hum: TextView? = null


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        view_weather = view.findViewById(R.id.wheather_image)
        view_city = view.findViewById(R.id.town)
        view_temp = view.findViewById(R.id.temp)
        view_press = view.findViewById(R.id.press)
        view_hum = view.findViewById(R.id.hum)

        if(MyApplication.city==null) {
        }else {
            view_city?.let {
                if (MyApplication.city != null) {
                    setText(it, MyApplication.city!!)

                }
            }
            val temps =
                MyApplication.temp?.let { Math.round(it).toString() } + "°C"
            view_temp?.let { setText(it, temps) }
            val press = "Pressure: " +
                    MyApplication.press?.let { Math.round(it).toString() } + " hPa"
            view_press?.let { setText(it, press) }
            val humid = "Humidity: " +
                    MyApplication.hum?.let { Math.round(it).toString() } + " %"
            view_hum?.let { setText(it, humid) }
            view_weather?.let { MyApplication.weather?.let { it1 -> setImage(it, it1) } }

        }
        val btnDay: Button = view.findViewById(R.id.btnDay)
        btnDay.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_nav_myday))

        val btnTasks: Button = view.findViewById(R.id.btnTasks)
        btnTasks.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_nav_task))

        val btnPomodoro: Button = view.findViewById(R.id.btnPomodoro)
        btnPomodoro.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_nav_pomodoro))


    }

    fun setText(text: TextView, value: String) {
        this.activity?.runOnUiThread(Runnable { text.text = value })
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setImage(
        imageView: ImageView,
        value: String
    ) {
        this.activity?.runOnUiThread(Runnable { //paste switch
            when (value) {
                "01d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d01d))
                "01n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d01d))
                "02d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d02d))
                "02n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d02d))
                "03d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d03d))
                "03n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d03d))
                "04d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d04d))
                "04n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d04d))
                "09d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d09d))
                "09n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d09d))
                "10d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d10d))
                "10n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d10d))
                "11d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d11d))
                "11n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d11d))
                "13d" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d13d))
                "13n" -> imageView.setImageDrawable(resources.getDrawable(R.drawable.d13d))
                else -> imageView.setImageDrawable(resources.getDrawable(R.drawable.wheather))
            }
        })
    }
}