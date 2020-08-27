package com.example.asystentoro.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.asystentoro.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


    var view_weather: ImageView? = null
    var view_city: TextView? = null
    var view_temp: TextView? = null
    var view_press: TextView? = null
    var view_hum: TextView? = null
    var btnDay: Button? = null
    var btnTasks: Button? = null
    var btnPomodoro: Button? = null



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
        btnDay=view.findViewById(R.id.btnDay)
        btnTasks=view.findViewById(R.id.btnTasks)
        btnPomodoro=view.findViewById(R.id.btnPomodoro)



    }
}