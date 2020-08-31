package com.example.asystentoro

import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_pomodoro.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PomodoroFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var param1: String? = null
    private var param2: String? = null


    private val POM_PREFS = "pomPrefs"
    private val START_MILLIS = "startTimeInMillis"
    private val MILLIS_LEFT = "millisLeft"
    private val TIMER_RUNNING = "timerRunning"
    private val END_TIME = "endTime"


    private var countdownTimer: CountDownTimer? = null

    private var isTimerRunning = false
    private var isSecondCycle = false

    private var startTimeInMillis: Long = 0
    private var timeLeftInMillis = startTimeInMillis
    private var endTime: Long = 0
    private var startCycles: Long = 0
    private var cyclesLeft = startCycles
    private var millisBreakTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pomodoro, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        PomodoroSetButton.setOnClickListener{
            val workTime = editText_pomodoro?.text.toString()
            val breakTime = editText_break?.text.toString()
            if (workTime.isEmpty() || breakTime.isEmpty()) {
                makeToast("Field cannot be empty")
            }else{
                if (workTime.toInt() <= 0 || breakTime.toInt() <= 0){
                    makeToast("Only positive numbers!")
                }else{
                    val millisWorkTime = workTime.toLong() * 60000

                    setTime(millisWorkTime, 4L)
                    millisBreakTime = breakTime.toLong() * 60000

                }
            }

        }

        PomodoroStartPauseButton.setOnClickListener {
            if (isTimerRunning) {
                pauseTimer()
            } else {
                startTimer()
            }
        }

        PomodoroStopButton.setOnClickListener {
            resetTimer()
        }
    }
    private fun setTime(millisecs: Long, c: Long) {
        startTimeInMillis = millisecs
        startCycles = c
        resetTimer()
    }

    private fun startTimer() {
        endTime = System.currentTimeMillis() + timeLeftInMillis
        countdownTimer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updatePomodoroText()
            }

            override fun onFinish() {
                if (cyclesLeft > 0) {
                    val t = startTimeInMillis
                    if (isSecondCycle) {
                        cyclesLeft -= 1
                    }
                    isSecondCycle = !isSecondCycle
                    setTime(millisBreakTime, cyclesLeft)
                    millisBreakTime = t
                    PomodoroCyclesLeft.text = "Cycles Left: $cyclesLeft"
                    if (cyclesLeft > 0) {
                        startTimer()
                    } else {
                        onFinish()
                    }
                } else {
                    isTimerRunning = false
                    updateInterface()
                }
            }
        }.start()
        isTimerRunning = true
        updateInterface()
    }

    private fun pauseTimer() {
        countdownTimer!!.cancel()
        isTimerRunning = false
        updateInterface()
    }

    private fun resetTimer() {
        timeLeftInMillis = startTimeInMillis
        cyclesLeft = startCycles
        updatePomodoroText()
        updateInterface()
    }

    private fun updatePomodoroText() {
        val hours = (startTimeInMillis / 1000).toInt() / 3600
        val mins = (timeLeftInMillis / 1000 % 3600).toInt() / 60
        val secs = (timeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String
        timeLeftFormatted = if (hours > 0) {
            String.format(
                Locale.getDefault(),
                "%d:%02d:%02d",
                hours,
                mins,
                secs
            )
        } else {
            String.format(Locale.getDefault(), "%02d:%02d", mins, secs)
        }
        PomodoroCounter.text = timeLeftFormatted
        PomodoroProgress.progress = 100
        PomodoroCyclesLeft.text = "Cycles Left: $cyclesLeft"
    }

    private fun updateInterface() {
        if (isTimerRunning) {
            editText_pomodoro.isEnabled = false
            editText_break.isEnabled = false
            PomodoroSetButton.visibility = View.INVISIBLE

            PomodoroStartPauseButton.setImageResource(R.drawable.ic_pause)

            PomodoroCyclesLeft.visibility = View.VISIBLE

            PomodoroStopButton.isEnabled = false

        } else {
            editText_pomodoro.isEnabled = true
            editText_break.isEnabled = true
            PomodoroSetButton.visibility = View.VISIBLE

            PomodoroStartPauseButton.setImageResource(R.drawable.ic_play)

            PomodoroCyclesLeft.visibility = View.INVISIBLE

            PomodoroStopButton.isEnabled = true

//            if (timeLeftInMillis < 1000) {
//                buttonStartPause.visibility = View.INVISIBLE
//            } else {
//                buttonStartPause.visibility = View.VISIBLE
//            }
//            if (timeLeftInMillis < startTimeInMillis) {
//                PomodoroStopButton.visibility = View.VISIBLE
//            } else {
//                buttonReset!!.visibility = View.INVISIBLE
//            }
        }
    }




    private fun makeToast(message: String) {
        val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.TOP, 0, 200)
        toast.show()
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