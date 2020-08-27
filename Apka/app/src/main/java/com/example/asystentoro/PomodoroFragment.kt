package com.example.asystentoro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_pomodoro.*
import androidx.appcompat.app.AppCompatActivity;

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PomodoroFragment : Fragment() {
    // TODO: Rename and change types of parameters
    public var SetCycleDuration = 3600
    public var SetCyclesSet = 5
    public var SetBreakDuration = 300
    public var SetBreakLongDuration = 900
    public var SetLongBreakCycles = 4

    private var CurCycleDuration = 0
    private var CurCycleSet = 0
    private var CurBreakDuration = 0
    private var CurBreakLongDuration = 0
    private var CurLongBreakCycles = 0

    private var PomodoroCycle = "Stopped" //Work, Break, LongBreak
    private var PomodoroStatus = "Stopped" //Start, Paused


    private var param1: String? = null
    private var param2: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }


        updatePomodoro()



//        PomodoroStartButton.setOnClickListener {v ->
//
//            if (PomodoroStatus == "Stopped")
//            {
//                PomodoroStartButtonWrapper.visibility = View.INVISIBLE
//            }
//        }


    }

    fun updatePomodoro(){
        PomodoroStatusText.text = PomodoroCycle
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pomodoro, container, false)
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PomodoroFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PomodoroFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}