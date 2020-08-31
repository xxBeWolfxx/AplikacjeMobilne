package com.example.asystentoro

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.toInput
import com.apollographql.apollo.coroutines.toDeferred
import com.example.CreateTaskMutation
import com.example.DeleteTaskMutation
import com.example.SaveTasksMutation
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
class TaskFragment : Fragment(), MyAdapter.OnItemClickListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var myTaskFromMainActivity:ArrayList<DoTAsk> = ArrayList()
//*****************variables to set RecyclerView**********************
    lateinit var exampleList: ArrayList<ItemCardView>
    var RecyclerTasks: RecyclerView? = null
    lateinit var adapter: MyAdapter

    var apolloclientTask: ApolloClient?=null //client to Database
//******************variables to check if buttons are clicked*************************
    var isclicked:Boolean = false
    var adding:Boolean = false

    var Spinner: Spinner? = null // spinner with types of tasks
//*******************variables to set date and time************************************
    var formate = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
    var timeFormat = SimpleDateFormat("kk:mm ", Locale.UK)

    var clickposition:Int = -1 //position of list in RecyclerView

    var titleTask:EditText? = null
    var textInput:EditText? = null
    var imageViewcardPresent: ImageView? = null
    var dateTask:TextView? = null
    var timerTask:TextView? = null
    var button_date:Button? = null
    var button_time:Button? = null


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

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ShowToast")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//#################Initialization of variables#############################
        titleTask = view.findViewById(R.id.titleTask)
        textInput = view.findViewById(R.id.infoText)
        imageViewcardPresent = view.findViewById(R.id.imageViewcardPresent)

        myTaskFromMainActivity = MyApplication.globalTask!! //assign global variables to local because tasks's information are needed in three fragments
//              Get apolloClient from MainActivity
        val ace: MainActivity? = activity as MainActivity?
        apolloclientTask = ace?.getApolloClient()


//              Set RecyclerView with list of components from tasks
        RecyclerTasks = view.findViewById(R.id.RycyclerTask)
        exampleList = DoTAsk().generateTaskList(myTaskFromMainActivity)
        adapter = MyAdapter(exampleList, this)


        RecyclerTasks?.adapter = adapter
        RecyclerTasks?.layoutManager = LinearLayoutManager(view.context)
        RecyclerTasks?.setHasFixedSize(true)




        val btnSev:ImageButton = view.findViewById(R.id.saveBtn)
        btnSev.setOnClickListener{
            var newItem:DoTAsk = DoTAsk()


            if (!isclicked)
            {
                if (titleTask?.text.toString() != "") newItem.title = titleTask?.text.toString()
                else {
                    Toast.makeText(context, "WARNING!! Title is empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                newItem.type = Spinner?.getSelectedItem().toString()
                if (textInput?.text.toString() == "" ) newItem.text = ""
                else    newItem.text = textInput?.text.toString()
                if(dateTask?.text.toString() + timerTask?.text.toString() == "DD-MM-YYYY" + "00:00")
                {
                    Toast.makeText(context, "WARNING!! You have to set time and day", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                newItem.date = dateTask?.text.toString() + "T" + timerTask?.text.toString()
                newItem = DoTAsk().dataConverter(newItem)
                newItem.number = myTaskFromMainActivity.size
                val drawable = when (newItem.type?.toLowerCase()) {
                    "meeting" -> R.drawable.meeting
                    "shop list" -> R.drawable.shoplist
                    "to do" -> R.drawable.todo
                    "other" -> R.drawable.qmark
                    else -> R.drawable.circle
            }
                val addItem = ItemCardView(drawable, newItem.title,"Date: ${newItem.day}-${newItem.month}-${newItem.year}   Time: ${newItem.hour}:${newItem.minute}", newItem.number)

                myTaskFromMainActivity.add(newItem)
                exampleList.add(addItem.ID,addItem)
               // ItemCardView(drawable)
               // CardView.setVisibility(View.GONE);
                //TextView.setTextColor(Color.parseColor("#FFD60000")) ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



                lifecycleScope.launchWhenResumed{
                    val respone = try {apolloclientTask?.mutate(CreateTaskMutation(newItem.title,newItem.text.toInput(),newItem.date,newItem.type!!))?.toDeferred()?.await()}
                    catch (e:Exception){
                        null
                    }}

            }
            else{
                if (titleTask?.text.toString() != "") myTaskFromMainActivity[clickposition].title = titleTask?.text.toString()
                else {
                    Toast.makeText(context, "WARNING!! Title is empty", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                myTaskFromMainActivity[clickposition].type = Spinner?.getSelectedItem().toString()
                if (textInput?.text.toString() == "" ) myTaskFromMainActivity[clickposition].text = ""
                else    myTaskFromMainActivity[clickposition].text = textInput?.text.toString()
                if(dateTask?.text.toString() + timerTask?.text.toString() == "DD-MM-YYYY" + "00:00")
                {
                    Toast.makeText(context, "WARNING!! You have to set time and day", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                myTaskFromMainActivity[clickposition].date = dateTask?.text.toString() + "T" + timerTask?.text.toString()
                myTaskFromMainActivity[clickposition] = DoTAsk().dataConverter(myTaskFromMainActivity[clickposition])
                val clickedItem = exampleList[clickposition]
                clickedItem.text1 = myTaskFromMainActivity[clickposition].title
                clickedItem.text2 = "Data: ${myTaskFromMainActivity[clickposition].day}-${myTaskFromMainActivity[clickposition].month}-${myTaskFromMainActivity[clickposition].year}   Time: ${myTaskFromMainActivity[clickposition].hour}:${myTaskFromMainActivity[clickposition].minute}"
                clickedItem.imageResource = when (myTaskFromMainActivity[clickposition].type?.toLowerCase()) {
                    "meeting" -> R.drawable.meeting
                    "shop list" -> R.drawable.shoplist
                    "to do" -> R.drawable.todo
                    "other" -> R.drawable.qmark
                    else -> R.drawable.circle
                }

                lifecycleScope.launchWhenResumed{ val respone = try {apolloclientTask?.mutate(SaveTasksMutation(myTaskFromMainActivity[clickposition].title,myTaskFromMainActivity[clickposition].id!!,myTaskFromMainActivity[clickposition].text.toInput(),myTaskFromMainActivity[clickposition].date.toInput(),myTaskFromMainActivity[clickposition].type.toInput()))?.toDeferred()?.await()}
                catch (e:Exception){
                    null
                }}








        }
            adapter.notifyDataSetChanged()
            adding = false
            isclicked = false
            settingCard(false)
            titleTask?.setText("")
            textInput?.setText("")
            Spinner?.setSelection(0)
            dateTask?.text = "DD-MM-YYYY"
            timerTask?.text = "00:00"
        }






        val btnAdder:FloatingActionButton = view.findViewById(R.id.Adder)
        btnAdder.setOnClickListener{
            adding = true
            isclicked = false
            settingCard(true)
        }


        val btnRem:ImageButton = view.findViewById(R.id.Remover)
        btnRem.setOnClickListener{
            if (!adding && !isclicked) {
                Toast.makeText(context, "WARNING!! I CANT DO THAT", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            lifecycleScope.launchWhenResumed{ val respone = try {apolloclientTask?.mutate(DeleteTaskMutation(myTaskFromMainActivity[clickposition].id!!))?.toDeferred()?.await()}
            catch (e:Exception){
                null
            }}
            exampleList.removeAt(clickposition)
            myTaskFromMainActivity.removeAt(clickposition)
            MyApplication.globalTask = myTaskFromMainActivity
            adapter.notifyDataSetChanged()
            settingCard(false)
            titleTask?.setText("")
            textInput?.setText("")
            Spinner?.setSelection(0)
            dateTask?.text = "DD-MM-YYYY"
            timerTask?.text = "00:00"

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

        // Finally, data bind the spinner object with adapter
        Spinner?.adapter  = aadapter;

        // Set an on item selected listener for spinner object
        Spinner?.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(parent:AdapterView<*>, view: View, position: Int, id: Long){
                // Display the selected item text on text view


                if (adding || isclicked)imageViewcardPresent?.setImageResource(when (parent.getItemAtPosition(position).toString().toLowerCase()) {
                    "meeting" -> R.drawable.meeting
                    "shop list" -> R.drawable.shoplist
                    "to do" -> R.drawable.todo
                    "other" -> R.drawable.qmark
                    else -> R.drawable.circle
                }
                )

            }

            override fun onNothingSelected(parent: AdapterView<*>){
                // Another interface callback
            }
        }
        /////////////////////////////////////////////////////////////////////////////////////////

        /////////////////////     DATE AND TIME        //////////////////

        button_date = view.findViewById(R.id.button_date)
        button_time = view.findViewById(R.id.button_time)
        dateTask = view.findViewById(R.id.dateTask)
        timerTask = view.findViewById(R.id.timerTask)

        val now = Calendar.getInstance()

        button_date?.setOnClickListener {
            val datePicker = context?.let { it1 ->
                DatePickerDialog(
                    it1, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        val selectedDate = Calendar.getInstance()
                        selectedDate.set(Calendar.YEAR, year)
                        selectedDate.set(Calendar.MONTH, month)
                        selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        dateTask?.text = formate.format(selectedDate.time)
                    },
                    now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
                )
            }

            datePicker!!.show()
            try {
                if (button_date?.text != "Show Dialog") {
                    val date = timeFormat.parse(button_date?.text.toString())
                    now.time = date!!
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        button_time?.setOnClickListener {
        val timePicker = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY,hourOfDay)
                selectedTime.set(Calendar.MINUTE,minute)
                timerTask?.text = timeFormat.format(selectedTime.time)
            },
                now.get(Calendar.HOUR_OF_DAY),now.get(Calendar.MINUTE),true)
            timePicker.show()

        }
        settingCard(false)

        //////////////////////////////////////////////////////////////////////////////////////////////





    }

    fun settingCard(set:Boolean)
    {
        if (!isclicked)
        {
            titleTask?.setText("")
            textInput?.setText("")
            Spinner?.setSelection(0)
            dateTask?.text = "DD-MM-YYYY"
            timerTask?.text = "00:00"
        }



        titleTask?.isEnabled = set
        textInput?.isEnabled = set
        button_date?.isEnabled = set
        button_time?.isEnabled = set




        Spinner?.setEnabled(set)
        if(!set) Spinner?.setAlpha(0.5f)
        else Spinner?.setAlpha(1f)
        Spinner?.isClickable = set



        if (!set) imageViewcardPresent?.setImageResource(R.drawable.ic_baseline_emoji_emotions_24)

    }

    override fun onItemClick(position: Int) {
        Log.d("Klik", "Position $position")
        isclicked = true
        //adding = false
        clickposition = position
        printingCard(position)
        settingCard(true)


    }

    @SuppressLint("SetTextI18n")
    fun printingCard(position: Int)
    {
        titleTask?.setText(myTaskFromMainActivity[position].title)
        textInput?.setText(myTaskFromMainActivity[position].text)
        imageViewcardPresent?.setImageResource(when (myTaskFromMainActivity[position].type?.toLowerCase()) {
            "meeting" -> R.drawable.meeting
            "shop list" -> R.drawable.shoplist
            "to do" -> R.drawable.todo
            "other" -> R.drawable.qmark
            else -> R.drawable.circle
        }
        )
        if (myTaskFromMainActivity[position].date != "Urgent") {
            dateTask?.text =
                "${myTaskFromMainActivity[position].year}-${myTaskFromMainActivity[position].month}-${myTaskFromMainActivity[position].day}"
            timerTask?.text =
                "${myTaskFromMainActivity[position].hour}:${myTaskFromMainActivity[position].minute}"
        }
        else
        {
            dateTask?.text = "Urgent"
            timerTask?.text = "  !!!"
        }



        Spinner?.setSelection(when (myTaskFromMainActivity[position].type?.toLowerCase()){
            "meeting" -> 1
            "shop list" -> 2
            "to do" -> 3
            "other" -> 4
            else -> 0
        })

        myTaskFromMainActivity[position]
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