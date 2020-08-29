package com.example.asystentoro

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random
import android.widget.AdapterView
import android.widget.ArrayAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TaskFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var myTaskFromMainActivity:ArrayList<DoTAsk> = ArrayList()
    var RecyclerTasks: RecyclerView? = null
    lateinit var exampleList: ArrayList<ItemCardView>
    var Spinner: Spinner? = null
    var formate = SimpleDateFormat("dd MMM, YYYY", Locale.UK)
    var timeFormat = SimpleDateFormat("hh:mm a", Locale.UK)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val let = arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val ace: MainActivity? = activity as MainActivity?
        myTaskFromMainActivity = ace?.getTasks()!!
        RecyclerTasks = view.findViewById(R.id.RycyclerTask)
        exampleList = DoTAsk().generateTaskList(myTaskFromMainActivity)
        val adapter = MyAdapter(exampleList)

//       RecyclerTasks?.adapter = MyAdapter(exampleList)
        RecyclerTasks?.adapter = adapter
        RecyclerTasks?.layoutManager = LinearLayoutManager(view.context)
        RecyclerTasks?.setHasFixedSize(true)

        var btn:ImageButton = view.findViewById(R.id.Adder)
        btn.setOnClickListener{
            val newItem = ItemCardView(R.drawable.circle, "kolo","YOLO")
            exampleList.add(exampleList.size,newItem)
            adapter.notifyItemChanged(exampleList.size)
        }


      /////////////////////     SPINER - TYPE        //////////////////

        val type = resources.getStringArray(R.array.type)
        Spinner = view.findViewById(R.id.typeTask)
        val aadapter = context?.let {
            ArrayAdapter(
                it, // Context
                android.R.layout.simple_spinner_item, // Layout
                type // Array
            )
        }
        // Set the drop down view resource
            aadapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Finally, data bind the spinner object with dapter
        Spinner?.adapter  = aadapter;

        // Set an on item selected listener for spinner object
        Spinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view

                //    text_view3?.text = "Spinner selected : ${parent.getItemAtPosition(position).toString()}"
            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////     DATE AND TIME        //////////////////

        val button_date:Button = view.findViewById(R.id.button_date)
        val button_time:Button = view.findViewById(R.id.button_time)
        val dateTask:TextView = view.findViewById(R.id.dateTask)
        val timerTask:TextView = view.findViewById(R.id.timerTask)

        val now = Calendar.getInstance()

        button_date.setOnClickListener {
            val datePicker = context?.let { it1 ->
                DatePickerDialog(
                    it1, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        dateTask.text = formate.format(selectedDate.time)
                    },
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                )
            }

            datePicker!!.show()
            try {
                if (button_date.text != "Show Dialog") {
                    val date = timeFormat.parse(button_date.text.toString())
                    now.time = date
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        button_time.setOnClickListener {
        val timePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)
                timerTask.text = timeFormat.format(selectedTime.time)
            },
                now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),false)
            timePicker.show()

        }


        //////////////////////////////////////////////////////////////////////////////////////////////


    }

    fun insertItem(view: View)
    {
        val index: Int = Random.nextInt(8)
        val newItem = ItemCardView(
            R.drawable.ic_arrow_upward_24,
            "Kupa",
            "Frajer"
        )


    }

    fun removeItem(view: View)
    {

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment TaskFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TaskFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }




}