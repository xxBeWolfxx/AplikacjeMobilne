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
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.asystentoro.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel


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


        val btnDay: Button = view.findViewById(R.id.btnDay)
        btnDay.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_nav_myday))

        val btnTasks: Button = view.findViewById(R.id.btnTasks)
        btnTasks.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_nav_task))

        val btnPomodoro: Button = view.findViewById(R.id.btnPomodoro)
        btnPomodoro.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_nav_home_to_nav_pomodoro))


    }
}